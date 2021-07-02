package io.aext.core.service.controller;

import static io.aext.core.base.constant.SystemConstant.*;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.aext.core.base.controller.BaseController;
import io.aext.core.base.enums.MemberStatus;
import io.aext.core.base.model.entity.Member;
import io.aext.core.base.model.vo.ECActiveConfirmVO;
import io.aext.core.base.model.vo.ECFindPasswordVO;
import io.aext.core.base.model.vo.ECVerifyCodeVO;
import io.aext.core.base.model.vo.ValidResultVO;
import io.aext.core.base.security.LimitedAccess;
import io.aext.core.base.service.DataCacheService;
import io.aext.core.base.service.EmailContentBuilder;
import io.aext.core.base.service.EmailSenderService;
import io.aext.core.base.service.LocaleMessageSourceService;
import io.aext.core.base.service.MemberService;
import io.aext.core.base.util.IpUtils;
import io.aext.core.base.util.SHA2;
import io.aext.core.base.util.ValueValidate;
import io.aext.core.service.ServiceProperty;
import io.aext.core.service.model.param.PasswordModifyParam;
import io.aext.core.service.model.param.PasswordResetParam;
import io.aext.core.service.model.param.ActivateParam;
import io.aext.core.service.model.param.LoginParam;
import io.aext.core.service.model.param.PasswordForgetParam;
import io.aext.core.service.model.param.ReactivateParam;
import io.aext.core.service.model.param.RegisterParam;
import io.aext.core.service.model.param.VerifyParam;
import io.aext.core.service.model.vo.MemberVO;
import io.aext.core.service.security.Auth;
import io.aext.core.service.security.JwtManager;
import io.aext.core.service.security.MemberDetails;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/06/27
 */
@RestController
@RequestMapping(value = { "/api/v1/member" })
@Auth(id = 1000, name = "member")
public class MemberController extends BaseController {
	@Autowired
	ServiceProperty serviceProperty;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	DataCacheService dataCacheService;

	@Autowired
	StringRedisTemplate redisTemplate;

	@Autowired
	LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	MemberService memberService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	EmailContentBuilder mailContentBuilder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtManager jwtManager;

	/**
	 * Register by email or sms.
	 */
	@RequestMapping("/register")
	@ResponseBody
	@LimitedAccess(frequency = 10, second = 30, heavyFrequency = 10, heavySecond = 60, heavyDelay = 86400)
	public ResponseEntity<?> register(@Valid RegisterParam param) {
		ValidResultVO vrVO = new ValidResultVO();
		if (memberService.isUsernameExist(param.getUsername())) {
			vrVO.add("field", "username", getMessageML("USERNAME_ALREADY_EXISTS"));
		}
		if (memberService.isEmailExist(param.getEmail())) {
			vrVO.add("field", "email", getMessageML("EMAIL_CAN_NOT_USE"));
		}
		if (vrVO.isError()) {
			return error(getMessageML("PARAMS_INVALID"), vrVO.getData());
		}

		// Check sent
		String keyAct = REGISTER_ACTIVATE_CODE_PREFIX + param.getUsername();
		Optional<String> valueToken = dataCacheService.readString(keyAct);
		if (valueToken.isPresent()) {
			return error(getMessageML("EMAIL_ALREADY_SEND"));
		}

		// Creating user's account
		Member member = new Member();
		member.setUsername(param.getUsername());
		member.setPassword(passwordEncoder.encode(param.getPassword()));
		member.setEmail(param.getEmail());
		member.setRegistTime(Instant.now());
		member.setMemberLevel(EnumSet.of(MemberStatus.REGISTERD));
		memberService.update(member);

		String token = sendActivateEmail(member);

		dataCacheService.update(keyAct, token, 600);

		return success(getMessageML("SEND_REGISTER_ACTIVATE_CODE_SUCCESS"));
	}

	@PostMapping(value = { "/reactivate" })
	@ResponseBody
	@Auth(id = 1, name = "active again.")
	public ResponseEntity<?> reactivate(@Validated ReactivateParam param) {
		// Get member
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MemberDetails md = (MemberDetails) authentication.getPrincipal();
		Optional<Member> omember = memberService.findByUsername(md.getUsername());
		if (omember.isEmpty()) {
			throwResponseStatusException("SYSTEM_ERROR");
		}
		Member member = omember.get();

		// Read token
		String keyToken = REGISTER_ACTIVATE_CODE_PREFIX + member.getUsername();
		Optional<String> valueToken = dataCacheService.readString(keyToken);
		if (valueToken.isEmpty()) {
			throwResponseStatusException(getMessageML("EMAIL_ALREADY_SEND"));
		}

		String token = sendActivateEmail(member);

		dataCacheService.update(keyToken, token, 600);

		return success(getMessageML("SEND_REGISTER_ACTIVATE_CODE_SUCCESS"));
	}

	String sendActivateEmail(Member member) {
		// Confirmation Mail
		String token = SHA2.getSHA512ShortByNow(0, 8).toUpperCase();

		URI locationConfirm = ServletUriComponentsBuilder.fromUriString(serviceProperty.getFrontDomain())
				.path(serviceProperty.getFrontConfirm()).query("username={username}&token={token}")
				.buildAndExpand(member.getUsername(), token).encode().toUri();

		if (serviceProperty.isDev()
		//
//				&& true == false
		//
		) {
			String path = getRequestMappingPath("activate(");
			locationConfirm = ServletUriComponentsBuilder.fromCurrentContextPath().path(path)
					.query("username={username}&token={token}").buildAndExpand(member.getUsername(), token).encode()
					.toUri();
		}

		// Send Mail
		String subject = getMessageML("TITLE_ACTIVATION_AEXT_ACCOUNT");
		String[] mailList = { member.getEmail() };
		ECActiveConfirmVO content = new ECActiveConfirmVO();
		content.setSubject(subject);
		content.setConfirmUrl(locationConfirm.toString());
		Optional<String> mailContent = mailContentBuilder.generateMailContent(content);
		if (mailContent.isPresent()) {
			emailSenderService.sendComplexEmail(mailList, serviceProperty.getMailUsername(),
					serviceProperty.getCompany(), subject, mailContent.get());
		}

		return token;
	}

	@RequestMapping(value = { "/activate" }, method = { RequestMethod.GET })
	@LimitedAccess(frequency = 5, second = 30, heavyFrequency = 10, heavySecond = 60, heavyDelay = 86400)
	public ResponseEntity<?> activate(@Valid ActivateParam param) {
		// Check token
		String keyAct = REGISTER_ACTIVATE_CODE_PREFIX + param.getUsername();
		Optional<String> valueToken = dataCacheService.readString(keyAct);
		if (valueToken.isEmpty() || !valueToken.get().equals(param.getToken())) {
			throwResponseStatusException(getMessageML("INVALID_LINK"));
		}

		// Get member
		Optional<Member> member = memberService.findByUsername(param.getUsername());
		if (!member.isPresent()) {
			throwResponseStatusException(getMessageML("INVALID_LINK"));
		}

		member.get().getMemberLevel().add(MemberStatus.VERIFIED_EMAIL);
		memberService.update(member.get());

		dataCacheService.delete(keyAct);

		return success(getMessageML("ACCOUNT_ACTIVATE_SUCCESS"));
	}

	@PostMapping(value = "/verify")
	@ResponseBody
	@Auth(id = 2, name = "send verify code.")
	public ResponseEntity<?> verify(VerifyParam param) {
		// Get member
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MemberDetails md = (MemberDetails) authentication.getPrincipal();
		Optional<Member> member = memberService.findByUsername(md.getUsername());
		if (member.isEmpty()) {
			throwResponseStatusException("SYSTEM_ERROR");
		}
		if (!StringUtils.hasText(member.get().getEmail()) || !ValueValidate.validateEmail(member.get().getEmail())) {
			throwResponseStatusException("EMAIL_CAN_NOT_USE");
		}

		String keyToken = EMAIL_VERIFY_CODE_PREFIX + param.getAction() + "_" + member.get().getUsername();
		Optional<String> valueToken = dataCacheService.readString(keyToken);
		if (valueToken.isPresent()) {
			throwResponseStatusException("VERIVY_CODE_HAD_BEEN_SENT");
		}
		String code = SHA2.getSHA256VerifyLen6();

		if (param.isMethodEmail()) {
			// Send Mail
			String subject = localeMessageSourceService.getMessage("EMAIL_VERIFICATION_CODE_TITLE");
			String[] mailList = { member.get().getEmail() };
			ECVerifyCodeVO mcVerifyCode = new ECVerifyCodeVO();
			mcVerifyCode.setSubject(subject);
			mcVerifyCode.setCode(code);
			Optional<String> mailContent = mailContentBuilder.generateMailContent(mcVerifyCode);
			if (mailContent.isPresent()) {
				emailSenderService.sendComplexEmail(mailList, serviceProperty.getMailUsername(),
						serviceProperty.getCompany(), subject, mailContent.get());
			}
		}

		dataCacheService.update(keyToken, code, 600);

		return success(getMessageML("VERIVY_CODE_HAD_BEEN_SENT"));
	}

	@RequestMapping("/login")
	@ResponseBody
	public ResponseEntity<?> loginEmail(@Valid LoginParam login, BindingResult bindingResult) {
		Authentication token = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		MemberDetails member = (MemberDetails) authentication.getPrincipal();

		MemberVO memberVO = new MemberVO();
		memberVO.setId(member.getUserid())
				//
				.setUsername(member.getUsername())
				//
				.setToken(jwtManager.generate(member.getUsername()))
				//
				.setResourceIds(null);

		return success(memberVO);
	}

	/*
	 * Standard JWT no concept of logout. This is special.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/logout")
	@ResponseBody
	public ResponseEntity<?> logout() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		MemberVO memberVO = new MemberVO();
		if (principal.getClass().equals(String.class)) {
			String user = (String) principal;
			memberVO.setUsername(user);
		} else if (principal.getClass().equals(MemberDetails.class)) {
			MemberDetails user = (MemberDetails) principal;
			memberVO.setUsername(user.getUsername());
			ValueOperations valueOperations = redisTemplate.opsForValue();
			valueOperations.set(JWT_LOGOUT_PREFIX + user.getUsername() + user.getJwtHash(), "1", 7200,
					TimeUnit.MINUTES);
		}

		SecurityContextHolder.clearContext();

		return success(memberVO);
	}

	@PostMapping(value = { "/password/modify" })
	@ResponseBody
	@Auth(id = 5, name = "reset password after login.")
	public ResponseEntity<?> passwordModify(@Validated PasswordModifyParam param) {
		// Get member from context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MemberDetails md = (MemberDetails) authentication.getPrincipal();

		// Get member from database
		Optional<Member> omember = memberService.findByUsername(md.getUsername());
		if (omember.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("SYSTEM_ERROR"));
		}
		Member member = omember.get();

		// Check email sent
		String keyToken = PASSWORD_RESET_PREFIX + member.getUsername();
		String valueToken = readRedisValueAsString(keyToken);
		if (!valueToken.toString().equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("EMAIL_ALREADY_SEND"));
		}
		String token = sendFindPasswordEmail(member);

		updateRedisValueAsString(keyToken, token, 600);

		return success(getMessageML("PROCESS_SUCCESS"));
	}

	@PostMapping(value = { "/password/forget" })
	@ResponseBody
	public ResponseEntity<?> passwordForget(HttpServletRequest request, @Validated PasswordForgetParam param) {
		String ip = IpUtils.getIpAddr(request);
		String keyCap = CAPTCHA_TOKEN_PREFIX + ip;
		String tokenCap = readRedisValueAsString(keyCap);
		if (!tokenCap.equals(param.getToken())) {
			return error(getMessageML("CAPTCHA_INVALID"));
		}

		// 0:username 1:email 2:sms
		int[] type = new int[3];
		String identityFeatures = param.getIdentityFeatures();

		// Check if email
		boolean isEmail = ValueValidate.validateEmail(identityFeatures);
		if (isEmail) {
			type[1] = 1;
		}

		boolean isUsername = ValueValidate.validateUserName(identityFeatures);
		if (isUsername) {
			type[0] = 1;
		}

		int dips = Arrays.stream(type).sum();

		Optional<Member> omember = null;

		if (dips <= 0) {
			return error(getMessageML("INPUT_INVALID"));
		} else if (isEmail) {
			omember = memberService.findByEmail(identityFeatures);
		} else if (isUsername) {
			omember = memberService.findByUsername(identityFeatures);
		}
		if (omember.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("FIND_ERROR"));
		}
		Member member = omember.get();

		// Read cache
		String keyV = PASSWORD_RESET_PREFIX + member.getUsername();
		String tokenV = readRedisValueAsString(keyV);
		if (!tokenV.equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("TRY_IT_LATER"));
		}

		String token = sendFindPasswordEmail(member);

		updateRedisValueAsString(keyV, token, 600);

		return success(getMessageML("PROCESS_SUCCESS"));
	}

	String sendFindPasswordEmail(Member member) {
		String token = SHA2.getSHA512ShortByNow(0, 8).toUpperCase();

		URI locationConfirm = ServletUriComponentsBuilder.fromUriString(serviceProperty.getFrontDomain())
				.path(serviceProperty.getFrontPassword()).query("username={username}&token={token}")
				.buildAndExpand(member.getUsername(), token).encode().toUri();

		if (serviceProperty.isDev()
		//
//					&& true == false
		//
		) {
			String path = getRequestMappingPath("passwordReset(");
			locationConfirm = ServletUriComponentsBuilder.fromCurrentContextPath().path(path)
					.query("username={username}&token={token}").buildAndExpand(member.getUsername(), token).encode()
					.toUri();
		}

		// Send Mail
		String subject = localeMessageSourceService.getMessage("RESET_PASSWORD_TITLE");
		String[] mailList = { member.getEmail() };
		ECFindPasswordVO content = new ECFindPasswordVO();
		content.setSubject(subject);
		content.setConfirmUrl(locationConfirm.toString());
		Optional<String> mailContent = mailContentBuilder.generateMailContent(content);
		if (mailContent.isPresent()) {
			emailSenderService.sendComplexEmail(mailList, serviceProperty.getMailUsername(),
					serviceProperty.getCompany(), subject, mailContent.get());
		}

		return token;
	}

	@PostMapping(value = { "/password/reset" })
	@ResponseBody
	public ResponseEntity<?> passwordReset(@Valid PasswordResetParam param) {
		// Read cache
		String keyV = PASSWORD_RESET_PREFIX + param.getUsername();
		String tokenV = readRedisValueAsString(keyV);
		if (!tokenV.equals(param.getToken())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("TOKEN_INVALID"));
		}

		Optional<Member> omember = memberService.findByUsername(param.getUsername());
		if (omember.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("SYSTEM_ERROR"));
		}
		Member member = omember.get();

		member.setPassword(passwordEncoder.encode(param.getPassword()));
		memberService.update(member);

		deleteRedisValueAsString(keyV);

		return success(getMessageML("PROCESS_SUCCESS"));
	}

	@PutMapping("/test")
	@ResponseBody
	@Auth(id = 98, name = "create new user")
	public ResponseEntity<?> test() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authentication.getPrincipal();
		return success(authentication);
	}

	@GetMapping("/test2")
	@ResponseBody
	@DeleteMapping
	@Auth(id = 99, name = "delete the user")
	public ResponseEntity<?> test2() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authentication.getPrincipal();
		return success(authentication);
	}
}
