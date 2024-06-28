package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import java.util.Locale;

public interface EmailService {

    void sendContactEmail(User user, Enterprise enterprise, JobOffer jobOffer, String message, Locale locale);

    void sendRegisterUserConfirmationEmail(User user, Locale locale);

    void sendRegisterEnterpriseConfirmationEmail(String email, String enterpriseName, Locale locale);

    void sendAcceptJobOfferEmail(Enterprise enterprise, String username, String email, String jobOfferPosition, Locale locale);

    void sendRejectJobOfferEmail(Enterprise enterprise, String username, String email, String jobOfferPosition, Locale locale);

    void sendCloseJobOfferEmailToAllApplicants(JobOffer jobOffer, Locale locale);

    void sendCloseJobOfferEmail(User user, String enterpriseName, String jobOfferPosition, Locale locale);

    void sendCancelJobOfferEmail(User user, String enterpriseName, String jobOfferPosition, Locale locale);

    void sendApplicationEmail(Enterprise enterprise, User user, String jobOfferPosition, Locale locale);

    void sendCancelApplicationEmail(Enterprise enterprise, User user, String jobOfferPosition, Locale locale);

    void sendAcceptApplicationEmail(User user, String enterpriseName, String email, String jobOfferPosition, Locale locale);

    void sendRejectApplicationEmail(User user, String enterpriseName, String email, String jobOfferPosition, Locale locale);
}
