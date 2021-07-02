package io.aext.core.base.service.impl;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import io.aext.core.base.model.vo.ECActiveConfirmVO;
import io.aext.core.base.model.vo.ECBaseVO;
import io.aext.core.base.model.vo.ECFindPasswordVO;
import io.aext.core.base.model.vo.ECVerifyCodeVO;
import io.aext.core.base.service.EmailContentBuilder;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-02
 */
@Service
public class EmailContentBuilderImpl implements EmailContentBuilder {
	private TemplateEngine templateEngine;

	public EmailContentBuilderImpl(@Autowired TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public <T extends ECBaseVO> Optional<String> generateMailContent(T content) {
		Locale locale = LocaleContextHolder.getLocale();
		Context context = new Context(locale);
		if (content instanceof ECVerifyCodeVO) {
			ECVerifyCodeVO data = (ECVerifyCodeVO) content;
			context.setVariable("data", data);
			return Optional.ofNullable(templateEngine.process("mailVerifyCode", context));
		} else if (content instanceof ECActiveConfirmVO) {
			ECActiveConfirmVO data = (ECActiveConfirmVO) content;
			context.setVariable("data", data);
			return Optional.ofNullable(templateEngine.process("mailActivateConfirm", context));
		} else if (content instanceof ECFindPasswordVO) {
			ECFindPasswordVO data = (ECFindPasswordVO) content;
			context.setVariable("data", data);
			return Optional.ofNullable(templateEngine.process("mailFindPassword", context));
		}

		return Optional.empty();
	}
}
