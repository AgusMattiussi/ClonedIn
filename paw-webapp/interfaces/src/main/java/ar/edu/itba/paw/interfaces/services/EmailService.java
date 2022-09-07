package ar.edu.itba.paw.interfaces.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body, String contactInfo);
}
