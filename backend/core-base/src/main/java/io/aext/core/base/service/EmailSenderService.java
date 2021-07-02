package io.aext.core.base.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

/**
 * @author rojar
 *
 * @date 2021-07-02
 */
public interface EmailSenderService {
	@Async
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000l, maxDelay = 2))
	void sendSimpleEmail(SimpleMailMessage email);

	@Async
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000l, maxDelay = 2))
	void sendComplexEmail(String[] to, String from, String author, String subject, String text);
}
