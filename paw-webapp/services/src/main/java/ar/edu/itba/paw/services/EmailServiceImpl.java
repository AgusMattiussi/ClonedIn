package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Service
// In the future, we should store the user Locale in the database
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    @Lazy
    private ContactService contactService;

    private static final int MULTIPART_MODE = MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;
    private static final String ENCODING = StandardCharsets.UTF_8.name();
    private static final String baseUrl = "http://pawserver.it.itba.edu.ar/paw-2022b-4/";
    private static final String REGISTER_SUCCESS_TEMPLATE = "registerSuccess.html";
    private static final String CONTACT_TEMPLATE = "contactEmail.html";
    private static final String ANSWER_TEMPLATE = "answerEmail.html";
    private static final String APPLICATION_TEMPLATE = "applicationEmail.html";
    private static final String CLOSE = "close";
    private static final String CANCEL = "cancel";
    private static final String ACCEPT = "acceptMsg";
    private static final String REJECT = "rejectMsg";
    private static final String ACCEPT_APPLICATION = "accept";
    private static final String REJECT_APPLICATION = "reject";
    private static final String SEND_APPLICATION = "sendApplication";
    private static final String CANCEL_APPLICATION = "cancelApplication";

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    @Async
    public void sendEmail(String to, String subject, String template, Map<String, Object> variables) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE, ENCODING);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom("noreply.cloned.in@gmail.com");
            mimeMessageHelper.setSubject(subject);
            variables.put("baseUrl", baseUrl);
            mimeMessageHelper.setText(getHtmlBody(template, variables), true);

            mailSender.send(mimeMessage);
        } catch(MessagingException messagingException) {
            LOGGER.error("MessagingException: error sending email in EmailServiceImpl");
        }
    }

    private String getHtmlBody(String template, Map<String, Object> variables) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        return templateEngine.process(template, thymeleafContext);
    }

    @Async
    @Override
    public void sendContactEmail(User user, Enterprise enterprise, JobOffer jobOffer, String message, Locale locale) {
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getName());
        mailMap.put("profileUrl", String.format("%s/users/%d/notifications", baseUrl, user.getId()));
        mailMap.put("jobDesc", jobOffer.getDescription());
        mailMap.put("jobPos", jobOffer.getPosition());
        mailMap.put("salary", jobOffer.getSalary() != null? String.format("$%s", jobOffer.getSalary().toString()) :
                messageSource.getMessage("contactMail.noSalaryMsg", null, locale));
        mailMap.put("modality", jobOffer.getModality());
        mailMap.put("enterpriseName", enterprise.getName());
        mailMap.put("enterpriseEmail", enterprise.getEmail());
        mailMap.put("message", message);
        mailMap.put("congratulationsMsg", messageSource.getMessage("contactMail.congrats", null, locale));
        mailMap.put("enterpriseMsg", messageSource.getMessage("contactMail.enterprise", null, locale));
        mailMap.put("positionMsg", messageSource.getMessage("contactMail.position", null, locale));
        mailMap.put("descriptionMsg", messageSource.getMessage("contactMail.description", null, locale));
        mailMap.put("salaryMsg", messageSource.getMessage("contactMail.salary", null, locale));
        mailMap.put("modalityMsg", messageSource.getMessage("contactMail.modality", null, locale));
        mailMap.put("additionalCommentsMsg", messageSource.getMessage("contactMail.additionalComments", null, locale));
        mailMap.put("buttonMsg", messageSource.getMessage("contactMail.button", null, locale));
        String subject = messageSource.getMessage("contactMail.subject", null, locale) + enterprise.getName();
        sendEmail(user.getEmail(), subject, CONTACT_TEMPLATE, mailMap);
    }

    @Async
    @Override
    public void sendRegisterUserConfirmationEmail(User user, Locale locale) {
        sendRegisterConfirmationEmail(user.getEmail(), user.getName(), locale, "jobOffers");
    }

    @Async
    @Override
    public void sendRegisterEnterpriseConfirmationEmail(String email, String enterpriseName, Locale locale) {
        sendRegisterConfirmationEmail(email, enterpriseName, locale, "users");
    }

    private void sendRegisterConfirmationEmail(String email, String username, Locale locale, String callToActionMsg) {
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", username);
        mailMap.put("welcomeMsg", messageSource.getMessage("registerMail.welcomeMsg", null, locale));
        mailMap.put("bodyMsg", messageSource.getMessage("registerMail.bodyMsg", null, locale));
        mailMap.put("buttonMsg", messageSource.getMessage("registerMail.button", null, locale));
        mailMap.put("callToActionUrl", baseUrl + callToActionMsg);
        String subject = messageSource.getMessage("registerMail.subject", null, locale);
        sendEmail(email, subject, REGISTER_SUCCESS_TEMPLATE, mailMap);
    }

    @Async
    @Override
    public void sendAcceptJobOfferEmail(Enterprise enterprise, String username, String email, String jobOfferPosition, Locale locale) {
        sendReplyJobOfferEmail(enterprise, username, email, jobOfferPosition, ACCEPT, locale);
    }

    @Async
    @Override
    public void sendRejectJobOfferEmail(Enterprise enterprise, String username, String email, String jobOfferPosition, Locale locale) {
        sendReplyJobOfferEmail(enterprise, username, email, jobOfferPosition, REJECT, locale);
    }

    @Override
    @Async
    public void sendCloseJobOfferEmailToAllApplicants(JobOffer jobOffer, Locale locale) {
        List<Contact> contactBatch;
        int batchSize = 20;
        int page = 0;
        do {
            contactBatch = contactService.getContactsForJobOffer(jobOffer, FilledBy.ANY, page, batchSize);

            for(Contact contact : contactBatch)
                sendCloseJobOfferEmail(contact.getUser(), jobOffer.getEnterprise().getName(), jobOffer.getPosition(), locale);

            page++;
        } while (!contactBatch.isEmpty());
    }

    private void sendReplyJobOfferEmail(Enterprise enterprise, String username, String email, String jobOfferPosition, String answerMsg, Locale locale) {
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", username);
        mailMap.put("answerMsg", messageSource.getMessage(answerMsg, null, locale));
        mailMap.put("contactMsg", answerMsg.compareTo(ACCEPT)==0?
                messageSource.getMessage("contactMsg", null, locale) + email :
                messageSource.getMessage("nonContactMsg", null, locale));
        mailMap.put("jobOffer", jobOfferPosition);
        mailMap.put("contactsUrl", String.format("%senterprises/%d/contacts", baseUrl, enterprise.getId()));
        mailMap.put("buttonMsg", messageSource.getMessage("answerMail.button", null, locale));

        String subject = messageSource.getMessage("answerMail.subject", null, locale);

        sendEmail(enterprise.getEmail(), subject, ANSWER_TEMPLATE, mailMap);
    }

    @Async
    @Override
    public void sendCloseJobOfferEmail(User user, String enterpriseName, String jobOfferPosition, Locale locale) {
        sendFinishJobOfferCycleEmail(user, enterpriseName, jobOfferPosition, CLOSE, locale);
    }

    @Async
    @Override
    public void sendCancelJobOfferEmail(User user, String enterpriseName, String jobOfferPosition, Locale locale) {
        sendFinishJobOfferCycleEmail(user, enterpriseName, jobOfferPosition, CANCEL, locale);
    }

    private void sendFinishJobOfferCycleEmail(User user, String enterpriseName, String jobOfferPosition, String action, Locale locale) {
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", enterpriseName);
        mailMap.put("answerMsg", messageSource.getMessage(action + "Msg", null, locale));
        mailMap.put("jobOffer", jobOfferPosition);
        mailMap.put("contactsUrl", String.format("%susers/%d/notifications", baseUrl, user.getId()));
        mailMap.put("buttonMsg", messageSource.getMessage("closeMail.button", null, locale));

        String subject = messageSource.getMessage(action + "Mail.subject", null, locale);

        sendEmail(user.getEmail(), subject, ANSWER_TEMPLATE, mailMap);
    }



    @Async
    @Override
    public void sendApplicationEmail(Enterprise enterprise, User user, String jobOfferPosition, Locale locale) {
        sendApplicationEmailHandler(enterprise, user, jobOfferPosition, SEND_APPLICATION, locale);
    }

    @Async
    @Override
    public void sendCancelApplicationEmail(Enterprise enterprise, User user, String jobOfferPosition, Locale locale) {
        sendApplicationEmailHandler(enterprise, user, jobOfferPosition, CANCEL_APPLICATION, locale);
    }

    private void sendApplicationEmailHandler(Enterprise enterprise, User user, String jobOfferPosition, String action, Locale locale) {
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", user.getName());
        mailMap.put("jobOffer", jobOfferPosition);
        mailMap.put("profileUrl", String.format("%susers/%d", baseUrl, user.getId()));
        mailMap.put("bodyMsg", messageSource.getMessage(action + "Mail.bodyMsg", null, locale));
        mailMap.put("buttonMsg", messageSource.getMessage(action + "Mail.button", null, locale));

        String subject = messageSource.getMessage(action + "Mail.subject", null, locale);

        sendEmail(enterprise.getEmail(), subject, APPLICATION_TEMPLATE, mailMap);
    }

    @Async
    @Override
    public void sendAcceptApplicationEmail(User user, String enterpriseName, String email, String jobOfferPosition, Locale locale) {
        sendReplyApplicationEmail(user, enterpriseName, email, jobOfferPosition, ACCEPT_APPLICATION, locale);
    }

    @Async
    @Override
    public void sendRejectApplicationEmail(User user, String enterpriseName, String email, String jobOfferPosition, Locale locale) {
        sendReplyApplicationEmail(user, enterpriseName, email, jobOfferPosition, REJECT_APPLICATION, locale);
    }

    private void sendReplyApplicationEmail(User user, String enterpriseName, String email, String jobOfferPosition, String action, Locale locale) {
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", enterpriseName);
        mailMap.put("answerMsg", messageSource.getMessage(action + "ApplicationMail.bodyMsg", null, locale));
        mailMap.put("jobOffer", jobOfferPosition);
        mailMap.put("contactsUrl", String.format("%susers/%d/applications", baseUrl, user.getId()));
        mailMap.put("buttonMsg", messageSource.getMessage("replyApplicationMail.button", null, locale));

        String subject = messageSource.getMessage(action + "ApplicationMail.subject", null, locale);

        sendEmail(email, subject, ANSWER_TEMPLATE, mailMap);
    }
}
