package io.aext.service.registration.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.netflix.appinfo.InstanceInfo;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class EurekaStateListener {
	private final static Logger logger = LoggerFactory.getLogger(EurekaStateListener.class);

	@Value("${spring.mail.username}")
	private String from;
	@Value("${service.company}")
	private String company;

	@Value("${service.admins.email}")
	private String admins;

	@Autowired
	private JavaMailSender javaMailSender;

	// replication
	// true if this is a replication event from other replica nodes, false
	// otherwise.
	// replication = true, from another eureka server.
	// replication = false, from eureka client.
	@EventListener(condition = "#event.replication==false")
	public void listen(EurekaInstanceCanceledEvent event) {
		String msg = "Your service " + event.getAppName() + "\n" + event.getServerId() + " has been shutdown.";
		logger.info(msg);

		String[] adminList = admins.split(",");
		for (int i = 0; i < adminList.length; i++) {
			sendEmailMsg(adminList[i], msg, "[Service]Shutdown Notice");
		}
	}

	@EventListener(condition = "#event.replication==false")
	public void listen(EurekaInstanceRegisteredEvent event) {
		InstanceInfo instanceInfo = event.getInstanceInfo();
		String msg = "Service " + instanceInfo.getAppName() + "\n" + instanceInfo.getHostName() + ":"
				+ instanceInfo.getPort() + " \nip: " + instanceInfo.getIPAddr() + " registered";
		logger.info(msg);

		String[] adminList = admins.split(",");
		for (int i = 0; i < adminList.length; i++) {
			sendEmailMsg(adminList[i], msg, "[Service]Service online notice");
		}
	}

	@EventListener
	public void listen(EurekaInstanceRenewedEvent event) {
		logger.info("Service {} renewed", event.getServerId() + "  " + event.getAppName());
	}

	@EventListener
	public void listen(EurekaRegistryAvailableEvent event) {
		logger.info("Registry start, {}", System.currentTimeMillis());
	}

	@EventListener
	public void listen(EurekaServerStartedEvent event) {
		logger.info("Server start, {}", System.currentTimeMillis());
	}

	@Async
	public void sendEmailMsg(String email, String msg, String subject) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = null;
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject(company + "-" + subject);
			Map<String, Object> model = new HashMap<>(16);
			model.put("msg", msg);
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
			cfg.setClassForTemplateLoading(this.getClass(), "/templates");
			Template template = cfg.getTemplate("simpleMessage.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			helper.setText(html, true);

			// Send mail.
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
