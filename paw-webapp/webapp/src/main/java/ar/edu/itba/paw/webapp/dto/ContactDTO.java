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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getJobOfferDesc() {
        return jobOfferDesc;
    }

    public void setJobOfferDesc(String jobOfferDesc) {
        this.jobOfferDesc = jobOfferDesc;
    }

    public long getJobOfferId() {
        return jobOfferId;
    }

    public void setJobOfferId(long jobOfferId) {
        this.jobOfferId = jobOfferId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFilledBy() {
        return filledBy;
    }

    public void setFilledBy(int filledBy) {
        this.filledBy = filledBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
