package io.aext.ocean.backend.controller;

import static io.aext.ocean.backend.constant.SystemConstant.*;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.aext.ocean.backend.ServiceProperty;
import io.aext.ocean.backend.enums.MemberStatus;
import io.aext.ocean.backend.model.param.PasswordModifyParam;
import io.aext.ocean.backend.model.param.PasswordResetParam;
import io.aext.ocean.backend.model.entity.Member;
import io.aext.ocean.backend.model.param.LoginParam;
import io.aext.ocean.backend.model.param.PasswordForgetParam;
import io.aext.ocean.backend.model.param.ReactivateParam;
import io.aext.ocean.backend.model.param.RegisterParam;
import io.aext.ocean.backend.model.param.VerifyParam;
import io.aext.ocean.backend.model.vo.MemberVO;
import io.aext.ocean.backend.security.Auth;
import io.aext.ocean.backend.security.JwtManager;
import io.aext.ocean.backend.security.MemberDetails;
import io.aext.ocean.backend.service.LocaleMessageSourceService;
import io.aext.ocean.backend.service.MemberService;
import io.aext.ocean.backend.service.email.EmailSenderService;
import io.aext.ocean.backend.service.email.MCActiveConfirm;
import io.aext.ocean.backend.service.email.MCFindPassword;
import io.aext.ocean.backend.service.email.MCVerifyCode;
import io.aext.ocean.backend.service.email.MailContentBuilder;
import io.aext.ocean.backend.util.IpUtils;
import io.aext.ocean.backend.util.SHA2;
import io.aext.ocean.backend.util.ValueValidate;

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
	StringRedisTemplate redisTemplate;

	@Autowired
	LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	MemberService memberService;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	MailContentBuilder mailContentBuilder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtManager jwtManager;

	/**
	 * Register by email or sms.
	 */
	@RequestMapping("/register")
	@ResponseBody
	public ResponseEntity<?> register(HttpServletRequest request, @Valid RegisterParam register,
			BindingResult bindingResult) {
		// check IP
		String ip = IpUtils.getIpAddr(request);
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Object cache = Optional.ofNullable(valueOperations.get(IP_REGISTER_DELAY_PREFIX + ip)).orElse("N");
		if (!cache.toString().equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Try it next time.");
		}

		if (bindingResult.hasErrors()) {
			Map<String, List<Map<String, String>>> data = buildBindingResultData(bindingResult);
			String meesage = localeMessageSourceService.getMessage("REGISTRATION_FAILED");

			return error(meesage, data);
		}

		if (memberService.isUsernameExist(register.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("USERNAME_ALREADY_EXISTS"));
		}
		if (memberService.isUsernameExist(register.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("EMAIL_CAN_NOT_USE"));
		}
		if (!register.getMethod().toUpperCase().equals("EMAIL")
				//
				&& !register.getMethod().toUpperCase().equals("SMS")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("SYSTEM_ERROR"));
		}

		// Check sent
		Object cacheAct = Optional.ofNullable(valueOperations.get(EMAIL_ACTIVATE_CODE_PREFIX + register.getUsername()))
				.orElse("N");
		if (!cacheAct.toString().equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("EMAIL_ALREADY_SEND"));
		}

		// Creating user's account
		Member member = new Member();
		member.setUsername(register.getUsername());
		member.setPassword(passwordEncoder.encode(register.getPassword()));
		member.setEmail(register.getEmail());
		member.setRegistTime(Instant.now());
		member.setMemberLevel(EnumSet.of(MemberStatus.REGISTERD));
		memberService.update(member);

		String token = sendActivateEmail(member);

		valueOperations.set(EMAIL_ACTIVATE_CODE_PREFIX + register.getUsername(), token, 10, TimeUnit.MINUTES);
		valueOperations.set(IP_REGISTER_DELAY_PREFIX + ip, "1", 60, TimeUnit.MINUTES);

		return success(localeMessageSourceService.getMessage("SEND_REGISTER_EMAIL_SUCCESS"));
	}

	@RequestMapping(value = { "/activate" }, method = { RequestMethod.GET })
	public ResponseEntity<?> activate(
			//
			@Valid @RequestParam(value = "username", required = true) String username,
			@Valid @RequestParam(value = "token", required = true) String token) {
		// Read cache
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Object cache = Optional.ofNullable(valueOperations.get(EMAIL_ACTIVATE_CODE_PREFIX + username)).orElse("N");
		if (cache.toString().equals("N") || !cache.toString().equals(token)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("INVALID_LINK"));
		}

		Optional<Member> member = memberService.findByUsername(username);
		if (!member.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("INVALID_LINK"));
		}

		member.get().getMemberLevel().add(MemberStatus.VERIFIED_EMAIL);
		memberService.update(member.get());

		valueOperations.getOperations().delete(EMAIL_ACTIVATE_CODE_PREFIX + username);

		return success();
	}

	@PostMapping(value = { "/reactivate" })
	@ResponseBody
	@Auth(id = 1, name = "active again.")
	public ResponseEntity<?> reactivate(@Validated ReactivateParam param, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, List<Map<String, String>>> data = buildBindingResultData(bindingResult);
			return error(getMessageML("PARAMS_INVALID"), data);
		}

		if (!param.isMethodValid()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("SYSTEM_ERROR"));
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MemberDetails md = (MemberDetails) authentication.getPrincipal();

		Optional<Member> omember = memberService.findByUsername(md.getUsername());
		if (omember.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("SYSTEM_ERROR"));
		}
		Member member = omember.get();

		// Read cache
		String key = EMAIL_ACTIVATE_CODE_PREFIX + member.getUsername();
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Object tokenStored = Optional.ofNullable(valueOperations.get(key)).orElse("N");
		if (!tokenStored.toString().equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, getMessageML("EMAIL_ALREADY_SEND"));
		}

		String token = sendActivateEmail(member);

		valueOperations.set(EMAIL_ACTIVATE_CODE_PREFIX + member.getUsername(), token, 10, TimeUnit.MINUTES);

		return success("Check email.");
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
		String subject = localeMessageSourceService.getMessage("EMAIL_ACTIVATION_CONFIRM_TITLE");
		String[] mailList = { member.getEmail() };
		MCActiveConfirm content = new MCActiveConfirm();
		content.setSubject(subject);
		content.setConfirmUrl(locationConfirm.toString());
		Optional<String> mailContent = mailContentBuilder.generateMailContent(content);
		if (mailContent.isPresent()) {
			emailSenderService.sendComplexEmail(mailList, serviceProperty.getMailUsername(),
					serviceProperty.getCompany(), subject, mailContent.get());
		}

		return token;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/verify")
	@ResponseBody
	@Auth(id = 2, name = "send verify code.")
	public ResponseEntity<?> verify(VerifyParam param, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, List<Map<String, String>>> data = buildBindingResultData(bindingResult);
			String meesage = localeMessageSourceService.getMessage("SYSTEM_ERROR");

			return error(meesage, data);
		}

		String method = param.getMethod().toUpperCase();

		if (!method.equals("EMAIL")
				//
				&& !method.equals("SMS")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("SYSTEM_ERROR"));
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MemberDetails md = (MemberDetails) authentication.getPrincipal();

		Optional<Member> member = memberService.findByUsername(md.getUsername());
		if (member.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("MEMBER_NOT_EXISTS"));
		}
		if (!StringUtils.hasText(member.get().getEmail()) || !ValueValidate.validateEmail(member.get().getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("EMAIL_CAN_NOT_USE"));
		}

		String key = EMAIL_VERIFY_CODE_PREFIX + param.getType() + "_" + member.get().getUsername();
		ValueOperations valueOperations = redisTemplate.opsForValue();
		Object cache = Optional.ofNullable(
				//
				valueOperations.get(key)).orElse("N");
		if (!cache.toString().equals("N")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("EMAIL_ALREADY_SEND"));
		}
		String code = SHA2.getSHA256VerifyLen6();

		if (method.equals("EMAIL")) {
			// Send Mail
			String subject = localeMessageSourceService.getMessage("EMAIL_VERIFICATION_CODE_TITLE");
			String[] mailList = { member.get().getEmail() };
			MCVerifyCode mcVerifyCode = new MCVerifyCode();
			mcVerifyCode.setSubject(subject);
			mcVerifyCode.setCode(code);
			Optional<String> mailContent = mailContentBuilder.generateMailContent(mcVerifyCode);
			if (mailContent.isPresent()) {
				emailSenderService.sendComplexEmail(mailList, serviceProperty.getMailUsername(),
						serviceProperty.getCompany(), subject, mailContent.get());
			}
		}

		valueOperations.set(key, code, 10, TimeUnit.MINUTES);

		return success();
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
		MCFindPassword content = new MCFindPassword();
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