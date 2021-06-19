package io.aext.core.service.controller;

import java.net.URI;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.aext.core.base.constant.MemberStatus;
import io.aext.core.base.controller.BaseController;
import io.aext.core.base.model.entity.Member;
import io.aext.core.base.service.LocaleMessageSourceService;
import io.aext.core.base.service.MemberService;
import io.aext.core.base.service.email.EmailSenderService;
import io.aext.core.base.service.email.MCActiveConfirm;
import io.aext.core.base.service.email.MCVerifyCode;
import io.aext.core.base.service.email.MailContentBuilder;
import io.aext.core.base.util.SHA2;
import io.aext.core.base.util.ValueValidate;
import io.aext.core.service.ServiceProperty;
import io.aext.core.service.payload.Login;
import io.aext.core.service.payload.Register;
import io.aext.core.service.security.Auth;
import io.aext.core.service.security.JwtManager;
import io.aext.core.service.security.MemberDetails;
import io.aext.core.service.vo.MemberVO;

import static io.aext.core.base.constant.SysConstant.*;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/6/5
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
	 * Register by email.
	 */
	@RequestMapping("/register/email")
	@ResponseBody
	public ResponseEntity<?> registerByEmail(@Valid Register register, BindingResult bindingResult) {
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
		if (!register.getMethod().toUpperCase().equals("EMAIL")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					localeMessageSourceService.getMessage("SYSTEM_ERROR"));
		}

		// Read cache
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Object cache = Optional.ofNullable(valueOperations.get(EMAIL_ACTIVE_CODE_PREFIX + register.getUsername()))
				.orElse("N");
		if (!cache.toString().equals("N")) {
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
		memberService.save(member);

		// Confirmation Mail
		String token = SHA2.getSHA512Short(0, 8).toUpperCase();

		URI locationConfirm = ServletUriComponentsBuilder.fromUriString(serviceProperty.getFrontDomain())
				.path(serviceProperty.getFrontConfirm()).query("name={name}&token={token}")
				.buildAndExpand(register.getUsername(), token).encode().toUri();

		if (serviceProperty.isDev()) {
			String path = getRequestMappingPath("registerConfirmEmail(");
			locationConfirm = ServletUriComponentsBuilder.fromCurrentContextPath().path(path)
					.query("name={name}&token={token}").buildAndExpand(register.getUsername(), token).encode().toUri();
		}

		// Send Mail
		String subject = localeMessageSourceService.getMessage("EMAIL_ACTIVATION_CONFIRM_TITLE");
		String[] mailList = { register.getEmail() };
		MCActiveConfirm content = new MCActiveConfirm();
		content.setSubject(subject);
		content.setConfirmUrl(locationConfirm.toString());
		Optional<String> mailContent = mailContentBuilder.generateMailContent(content);
		if (mailContent.isPresent()) {
			emailSenderService.sendComplexEmail(mailList, serviceProperty.getMailUsername(),
					serviceProperty.getCompany(), subject, mailContent.get());
		}

		valueOperations.set(EMAIL_ACTIVE_CODE_PREFIX + register.getUsername(), token, 10, TimeUnit.MINUTES);

		return success(localeMessageSourceService.getMessage("SEND_REGISTER_EMAIL_SUCCESS"));
	}

	@RequestMapping(value = { "/register/confirm/email" }, method = { RequestMethod.GET })
	public ResponseEntity<?> registerConfirmEmail(
			@Valid @RequestParam(value = "username", required = true) String username,
			@Valid @RequestParam(value = "token", required = true) String token) {
		// Read cache
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Object cache = Optional.ofNullable(valueOperations.get(EMAIL_ACTIVE_CODE_PREFIX + username)).orElse("N");
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
		memberService.save(member.get());

		valueOperations.getOperations().delete(EMAIL_ACTIVE_CODE_PREFIX + username);

		return success();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/send/guest/email/verify", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> sendEmailVerifyGuest(String email) {
		try {
			Optional<Member> member = memberService.findByEmail(email);
			if (member.isPresent()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						localeMessageSourceService.getMessage("EMAIL_CAN_NOT_USE"));
			}

			if (!StringUtils.hasText(email) || !ValueValidate.validateEmail(email)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						localeMessageSourceService.getMessage("EMAIL_CAN_NOT_USE"));
			}

			ValueOperations valueOperations = redisTemplate.opsForValue();
			Object cache = Optional.ofNullable(valueOperations.get(EMAIL_GUEST_VERIFY_CODE_PREFIX + email)).orElse("N");
			if (!cache.toString().equals("N")) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						localeMessageSourceService.getMessage("EMAIL_ALREADY_SEND"));
			}
			String code = SHA2.getSHA256VerifyLen6();

			// Send Mail
			String subject = localeMessageSourceService.getMessage("EMAIL_VERIFICATION_CODE_TITLE");
			String[] mailList = { email };
			MCVerifyCode mcVerifyCode = new MCVerifyCode();
			mcVerifyCode.setSubject(subject);
			mcVerifyCode.setCode(code);
			Optional<String> mailContent = mailContentBuilder.generateMailContent(mcVerifyCode);
			if (mailContent.isPresent()) {
				emailSenderService.sendComplexEmail(mailList, serviceProperty.getMailUsername(),
						serviceProperty.getCompany(), subject, mailContent.get());
			}

			valueOperations.set(EMAIL_GUEST_VERIFY_CODE_PREFIX + email, code, 10, TimeUnit.MINUTES);

			return success();
		} catch (Exception e) {
			String meesage = localeMessageSourceService.getMessage("SYSTEM_ERROR");
			if (e instanceof ResponseStatusException) {
				meesage = ((ResponseStatusException) e).getReason();
			}

			return error(meesage, ExceptionUtils.getStackTrace(e));
		}
	}

	@RequestMapping("/login")
	@ResponseBody
	public ResponseEntity<?> loginEmail(@Valid Login login, BindingResult bindingResult) {

//		Optional<Member> member = memberService.findByUsername(login.getUsername());
//		if (member.isEmpty() || !passwordEncoder.matches(login.getPassword(), member.get().getPassword())) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//					localeMessageSourceService.getMessage("LOGIN_FAILED"));
//		}
//		Authentication token = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
//		Authentication authentication = authenticationManager.authenticate(token);
//		SecurityContextHolder.getContext().setAuthentication(authentication);

//		MemberDetails memberDetails = new MemberDetails(member.get().getId(), member.get().getUsername(),
//				member.get().getEmail(), member.get().getPassword(), true, null);

//		return success("OK", authentication);

		Authentication token = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		MemberDetails member = (MemberDetails) authentication.getPrincipal();

		// Jwt
//		SecurityContextHolder.getContext().setAuthentication(authentication);	

		MemberVO memberVO = new MemberVO();
		memberVO.setId(member.getUserid())
				//
				.setUsername(member.getUsername())
				//
				.setToken(jwtManager.generate(member.getUsername()))
				//
				.setResourceIds(null);

		return success("ok", memberVO);
	}

	@RequestMapping("/logout")
	@ResponseBody
	public ResponseEntity<?> logout() {
		SecurityContextHolder.clearContext();
		return success("OK");
	}

//	@GetMapping("/test")
	@ResponseBody
	@PutMapping("/test")
	@Auth(id = 1, name = "create new user")
	public ResponseEntity<?> test() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authentication.getPrincipal();
		return success("OK", authentication);
	}

	@GetMapping("/test2")
	@ResponseBody
	@DeleteMapping
	@Auth(id = 2, name = "delete the user")
	public ResponseEntity<?> test2() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authentication.getPrincipal();
		return success("OK", authentication);
	}
}
