package io.aext.core.base.service.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
@Service
public class EmailSenderService {
	private JavaMailSender javaMailSender;

	@Autowired
	public EmailSenderService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Async
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000l, maxDelay = 2))
	public void sendSimpleEmail(SimpleMailMessage email) {
		try {
			javaMailSender.send(email);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Async
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000l, maxDelay = 2))
	public void sendComplexEmail(String[] to, String from, String author, String subject, String text) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(from, author);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);

			javaMailSender.send(message);
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
