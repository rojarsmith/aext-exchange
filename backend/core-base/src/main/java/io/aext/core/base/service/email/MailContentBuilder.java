package io.aext.core.base.service.email;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author rojar
 *
 * @date 2021-06-12
 */
@Service
public class MailContentBuilder {
	private TemplateEngine templateEngine;

	public MailContentBuilder(@Autowired TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public <T extends MCBase> Optional<String> generateMailContent(T content) {
		Locale locale = LocaleContextHolder.getLocale();
		Context context = new Context(locale);
		if (content instanceof MCVerifyCode) {
			MCVerifyCode data = (MCVerifyCode) content;
			context.setVariable("data", data);
			return Optional.ofNullable(templateEngine.process("mailVerifyCode", context));
		} else if (content instanceof MCActiveConfirm) {
			MCActiveConfirm data = (MCActiveConfirm) content;
			context.setVariable("data", data);
			return Optional.ofNullable(templateEngine.process("mailActivateConfirm", context));
		}

		return Optional.empty();
	}
}
