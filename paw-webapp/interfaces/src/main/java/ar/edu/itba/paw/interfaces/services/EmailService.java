package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;

import java.util.Map;

public interface EmailService {
    String USERNAME_FIELD = "username";
    String MESSAGE_FIELD = "message";
    String CONTACT_INFO_FIELD = "contactInfo";
    void sendEmail(String to, String subject, String body, Map<String, Object> variables);
    void sendContactEmail(User user, Enterprise enterprise, JobOffer jobOffer, String message);
    void sendRegisterConfirmationEmail(String email, String username);
    void sendReplyJobOfferEmail(String enterpriseEmail, String username, String jobOfferPosition, String answer);
}
