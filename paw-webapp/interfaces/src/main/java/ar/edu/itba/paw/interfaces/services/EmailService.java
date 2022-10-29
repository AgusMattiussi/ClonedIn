package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;

import java.util.Locale;
import java.util.Map;

public interface EmailService {
    void sendContactEmail(User user, Enterprise enterprise, JobOffer jobOffer, String message, Locale locale);
    void sendRegisterUserConfirmationEmail(User user, Locale locale);
    void sendRegisterEnterpriseConfirmationEmail(String email, String enterpriseName, Locale locale);
    void sendReplyJobOfferEmail(Enterprise enterprise, String username, String email, String jobOfferPosition, String answer, Locale locale);
    void sendCloseJobOfferEmail(User user, String enterpriseName, String jobOfferPosition, Locale locale);
    void sendCancelJobOfferEmail(User user, String enterpriseName, String jobOfferPosition, Locale locale);
    void sendApplicationEmail(Enterprise enterprise, User user, String jobOfferPosition, Locale locale);
}
