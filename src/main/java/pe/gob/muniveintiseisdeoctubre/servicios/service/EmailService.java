package pe.gob.muniveintiseisdeoctubre.servicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pe.gob.muniveintiseisdeoctubre.servicios.util.Constants;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    HttpServletRequest request;

    @Autowired
    Constants constants;

    public void sendMail(String to, String subject, String body) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        Context templateContext = new Context();
        templateContext.setVariable("body", body);
        templateContext.setVariable("logocorreo", constants.baseurl + '/' + constants.mailurl + "logocorreo.png");
        templateContext.setVariable("resetpass", constants.baseurl + '/' + constants.mailurl + "resetpass.png");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(templateEngine.process("mail", templateContext), true);
        mailSender.send(msg);
    }

    public void sendPasswordReset(String to, String subject, String body, String user) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context templateContext = new Context();
        templateContext.setVariable("logocorreo", constants.baseurl + '/' + constants.mailurl + "logocorreo.png");
        templateContext.setVariable("urlnewpass", constants.baseurl + '/' + constants.mailurl + "newpass.png");
        templateContext.setVariable("namemunicipalidad", constants.nameMunicipalidad);
        templateContext.setVariable("shortnameMunicipalidad", constants.shortnameMunicipalidad);

        if (body.length() > 6 && user != null) {
            String url = getAppUrl() + "/api/auth/generate-password?code=" + body;
            templateContext.setVariable("url", url);
            templateContext.setVariable("user", user);
        } else {
            templateContext.setVariable("newpass", body);
        }
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(templateEngine.process("password-mail", templateContext), true);
        mailSender.send(msg);
    }

    private String getAppUrl() {
        return constants.baseurl;
    }
}
