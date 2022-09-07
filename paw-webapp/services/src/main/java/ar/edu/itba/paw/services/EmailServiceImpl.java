package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Override
    public void sendEmail(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@clonedin.com");
//        message.setTo(); TODO: mail usuario
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
