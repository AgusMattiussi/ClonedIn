package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Contact;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class ContactDTO {

    //TODO: URIs para User, enterprise y jobOffer?

    private String user;
    private String enterprise;
    private String jobOfferDesc;
    private long jobOfferId;
    private String status;
    private int filledBy;
    private String date;
    private URI self;

    public static ContactDTO fromContact(final UriInfo uriInfo, final Contact contact) {
        final ContactDTO dto = new ContactDTO();
        dto.user = contact.getUser().getName();
        dto.enterprise = contact.getEnterprise().getName();
        dto.jobOfferDesc = contact.getJobOffer().getDescription();
        dto.jobOfferId = contact.getJobOffer().getId();
        dto.status = contact.getStatus();
        dto.filledBy = contact.getFilledBy();
        dto.date = contact.getDate();

        //FIXME: Revisar si esta bien formado
        final UriBuilder contactUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath("joid").path(String.valueOf(contact.getJobOffer().getId()))
                .replacePath("uid").path(String.valueOf(contact.getUser().getId()));
        dto.self = contactUriBuilder.build();

        return dto;
    }
}
