package com.example.ninhdemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(String to,String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    StandardCharsets.UTF_8.name());

            //load template email with content
//		    Context context = new Context();
//		    context.setVariable("name", messageDTO.getToName());
//		    context.setVariable("content", messageDTO.getContent());
//		    String html = templateEngine.process("welcome-email", context);

            ///send email
            helper.setTo(to); // email gui toi
            helper.setText(body, true); // noi dung
            helper.setSubject(subject); // tieu de
            helper.setFrom("leducninh29031996@gmail.com"); // nguoi gui
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
//		    logger.error("Email sent with error: " + e.getMessage());
        }
    }
}