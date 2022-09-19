package ar.edu.itba.paw.interfaces.services;

import java.util.Map;

public interface EmailService {
    String USERNAME_FIELD = "username";
    String MESSAGE_FIELD = "message";
    String CONTACT_INFO_FIELD = "contactInfo";
    void sendEmail(String to, String subject, String body, Map<String, Object> variables);
}
