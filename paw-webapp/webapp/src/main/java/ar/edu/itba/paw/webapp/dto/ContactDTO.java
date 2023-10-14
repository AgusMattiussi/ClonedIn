package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Contact;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public class ContactDTO {

    private static final String USERS_URL = "webapp_war/users";
    private static final String ENTERPRISES_URL = "webapp_war/enterprises";
    private static final String JOB_OFFERS_URL = "webapp_war/jobOffers";

    private URI user;
    private URI enterprise;
    private URI jobOffer;
    private String status;
    private int filledBy;
    private String date;

    public static ContactDTO fromContact(final UriInfo uriInfo, final Contact contact) {
        final ContactDTO dto = new ContactDTO();
        dto.status = contact.getStatus();
        dto.filledBy = contact.getFilledBy();
        dto.date = contact.getDate();

        UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(USERS_URL)
                .path(contact.getUser().getId().toString());
        dto.user = userUriBuilder.build();

        UriBuilder enterpriseUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(ENTERPRISES_URL)
                .path(contact.getEnterprise().getId().toString());
        dto.enterprise = enterpriseUriBuilder.build();

        UriBuilder jobOfferUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(JOB_OFFERS_URL)
                .path(contact.getJobOffer().getId().toString());
        dto.jobOffer = jobOfferUriBuilder.build();

        return dto;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public URI getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(URI enterprise) {
        this.enterprise = enterprise;
    }

    public URI getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(URI jobOffer) {
        this.jobOffer = jobOffer;
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
}
