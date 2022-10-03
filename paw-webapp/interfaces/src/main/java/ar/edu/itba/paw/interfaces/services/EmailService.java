package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;

import java.util.Map;

public interface EmailService {
    void sendEmail(String to, String subject, String body, Map<String, Object> variables);
    void sendContactEmail(User user, Enterprise enterprise, JobOffer jobOffer, String message);
    void sendRegisterConfirmationEmail(String email, String username);
    void sendReplyJobOfferEmail(Enterprise enterprise, String username, String jobOfferPosition, String answer);
    void sendCloseJobOfferEmail(User user, String enterpriseName, String jobOfferPosition);
    void sendCancelJobOfferEmail(User user, String enterpriseName, String jobOfferPosition);

}
