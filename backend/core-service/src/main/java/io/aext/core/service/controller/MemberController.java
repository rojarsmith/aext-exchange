package io.aext.core.service.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.aext.core.base.constant.CommonStatus;
import io.aext.core.base.constant.MemberLevelEnum;
import io.aext.core.base.controller.BaseController;
import io.aext.core.base.entity.Member;
import io.aext.core.base.service.LocaleMessageSourceService;
import io.aext.core.base.service.MemberService;
import io.aext.core.base.service.email.EmailSenderService;
import io.aext.core.base.service.email.MCVerifyCode;
import io.aext.core.base.service.email.MailContentBuilder;
import io.aext.core.base.util.SHA2;
import io.aext.core.base.util.ValueValidate;
import io.aext.core.service.payload.Register;

import static io.aext.core.base.constant.SysConstant.*;

/**
 * @author Rojar Smith
 * @Description:
 * @date 2021/6/5
 */
@RestController
@RequestMapping(value = { "/api/v1/member" })
public class MemberController extends BaseController {
	@Value("${spring.mail.username}")
	private String fromAddress;

	@Value("${service.company}")
	private String company;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	LocaleMessageSourceService localeMessageSourceService;

	@Autowired
	MemberService memberService;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	MailContentBuilder mailContentBuilder;

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
		Object cache = Optional.ofNullable(valueOperations.get(EMAIL_ACTIVE_CODE_PREFIX + register.getEmail()))
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
		member.setMemberLevel(MemberLevelEnum.GENERAL);
		member.setCommonStatus(CommonStatus.NORMAL);
		memberService.save(member);

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
				emailSenderService.sendComplexEmail(mailList, fromAddress, company, subject, mailContent.get());
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

}
