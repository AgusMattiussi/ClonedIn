package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private MessageSource messageSource;
    private static final int MULTIPART_MODE = MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;
    private static final String ENCODING = StandardCharsets.UTF_8.name();
    private final String baseUrl = "http://pawserver.it.itba.edu.ar/paw-2022b-4/";
    private static final String REGISTER_SUCCESS_TEMPLATE = "registerSuccess.html";
    private static final String CONTACT_TEMPLATE = "contactEmail.html";
    private static final String ANSWER_TEMPLATE = "answerEmail.html";
    private static final String CLOSE = "close";
    private static final String CANCEL = "cancel";
    private static final String ACCEPT = "acceptMsg";

    @Async
    @Override
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
            //TODO: LOG error
        }
    }

    private String getHtmlBody(String template, Map<String, Object> variables) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);
        return templateEngine.process(template, thymeleafContext);
    }

    @Async
    @Override
    public void sendContactEmail(User user, Enterprise enterprise, JobOffer jobOffer, String message) {
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", user.getName());
        mailMap.put("profileUrl", baseUrl + "notificationsUser/" + user.getId());
        mailMap.put("jobDesc", jobOffer.getDescription());
        mailMap.put("jobPos", jobOffer.getPosition());
        mailMap.put("salary", String.valueOf(jobOffer.getSalary()));
        mailMap.put("modality", jobOffer.getModality());
        mailMap.put("enterpriseName", enterprise.getName());
        mailMap.put("enterpriseEmail", enterprise.getEmail());
        mailMap.put("message", message);
        mailMap.put("congratulationsMsg", messageSource.getMessage("contactMail.congrats", null, LocaleContextHolder.getLocale()));
        mailMap.put("enterpriseMsg", messageSource.getMessage("contactMail.enterprise", null, LocaleContextHolder.getLocale()));
        mailMap.put("positionMsg", messageSource.getMessage("contactMail.position", null, LocaleContextHolder.getLocale()));
        mailMap.put("descriptionMsg", messageSource.getMessage("contactMail.description", null, LocaleContextHolder.getLocale()));
        mailMap.put("salaryMsg", messageSource.getMessage("contactMail.salary", null, LocaleContextHolder.getLocale()));
        mailMap.put("modalityMsg", messageSource.getMessage("contactMail.modality", null, LocaleContextHolder.getLocale()));
        mailMap.put("additionalCommentsMsg", messageSource.getMessage("contactMail.additionalComments", null, LocaleContextHolder.getLocale()));
        mailMap.put("buttonMsg", messageSource.getMessage("contactMail.button", null, LocaleContextHolder.getLocale()));
        String subject = messageSource.getMessage("contactMail.subject", null, LocaleContextHolder.getLocale()) + enterprise.getName();
        sendEmail(user.getEmail(), subject, CONTACT_TEMPLATE, mailMap);
    }

    @Async
    @Override
    public void sendRegisterConfirmationEmail(String email, String username) {
        final Map<String, Object> mailMap = new HashMap<>();
        mailMap.put("username", username);
        mailMap.put("welcomeMsg", messageSource.getMessage("registerMail.welcomeMsg", null, LocaleContextHolder.getLocale()));
        mailMap.put("bodyMsg", messageSource.getMessage("registerMail.bodyMsg", null, LocaleContextHolder.getLocale()));
        mailMap.put("buttonMsg", messageSource.getMessage("registerMail.button", null, LocaleContextHolder.getLocale()));
        String subject = messageSource.getMessage("registerMail.subject", null, LocaleContextHolder.getLocale());
        sendEmail(email, subject, REGISTER_SUCCESS_TEMPLATE, mailMap);
    }

    @Async
    @Override
    public void sendReplyJobOfferEmail(Enterprise enterprise, String username, String email, String jobOfferPosition, String answerMsg) {
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", username);
        mailMap.put("answerMsg", messageSource.getMessage(answerMsg, null, LocaleContextHolder.getLocale()));
        mailMap.put("contactMsg", answerMsg.compareTo(ACCEPT)==0?
                messageSource.getMessage("contactMsg", null, LocaleContextHolder.getLocale()) + email :
                messageSource.getMessage("nonContactMsg", null, LocaleContextHolder.getLocale()));
        mailMap.put("jobOffer", jobOfferPosition);
        mailMap.put("contactsUrl", baseUrl + "/contactsEnterprise/" + enterprise.getId());
        mailMap.put("buttonMsg", messageSource.getMessage("answerMail.button", null, LocaleContextHolder.getLocale()));

        String subject = messageSource.getMessage("answerMail.subject", null, LocaleContextHolder.getLocale());

        sendEmail(enterprise.getEmail(), subject, ANSWER_TEMPLATE, mailMap);
    }

    @Async
    @Override
    public void sendCloseJobOfferEmail(User user, String enterpriseName, String jobOfferPosition) {
        sendFinishJobOfferCycleEmail(user, enterpriseName, jobOfferPosition, CLOSE);
    }

    @Async
    @Override
    public void sendCancelJobOfferEmail(User user, String enterpriseName, String jobOfferPosition) {
        sendFinishJobOfferCycleEmail(user, enterpriseName, jobOfferPosition, CANCEL);
    }

    @Async
    void sendFinishJobOfferCycleEmail(User user, String enterpriseName, String jobOfferPosition, String action) {
        final Map<String, Object> mailMap = new HashMap<>();

        mailMap.put("username", enterpriseName);
        mailMap.put("answerMsg", messageSource.getMessage(action + "Msg", null, LocaleContextHolder.getLocale()));
        mailMap.put("jobOffer", jobOfferPosition);
        mailMap.put("contactsUrl", baseUrl + "/notificationsUser/" + user.getId());
        mailMap.put("buttonMsg", messageSource.getMessage("closeMail.button", null, LocaleContextHolder.getLocale()));

        String subject = messageSource.getMessage(action + "Mail.subject", null, LocaleContextHolder.getLocale());

        sendEmail(user.getEmail(), subject, ANSWER_TEMPLATE, mailMap);
    }

}
