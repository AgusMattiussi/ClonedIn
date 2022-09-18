package ar.edu.itba.paw.interfaces.services;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, String body, Map<String, Object> variables);
}
