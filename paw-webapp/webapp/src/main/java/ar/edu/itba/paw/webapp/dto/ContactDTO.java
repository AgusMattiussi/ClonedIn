package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Contact;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;

// TODO: Agregar endpoints para poner 'self'?
public class ContactDTO {

    private String status;
    private int filledBy;
    private String date;
    private ContactDTOLinks links;

    public static ContactDTO fromContact(final UriInfo uriInfo, final Contact contact) {
        final ContactDTO dto = new ContactDTO();
        dto.status = contact.getStatus();
        dto.filledBy = contact.getFilledBy();
        dto.date = contact.getDate();

        dto.links = new ContactDTOLinks(uriInfo, contact);

        return dto;
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

    public ContactDTOLinks getLinks() {
        return links;
    }

    public void setLinks(ContactDTOLinks links) {
        this.links = links;
    }

    public static class ContactDTOLinks {
        private URI user;
        private URI enterprise;
        private URI jobOffer;

        public ContactDTOLinks() {
        }

        public ContactDTOLinks(UriInfo uriInfo, Contact contact) {
            UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder()
                    .replacePath(USERS_URL)
                    .path(contact.getUser().getId().toString());
            this.user = userUriBuilder.build();

            UriBuilder enterpriseUriBuilder = uriInfo.getAbsolutePathBuilder()
                    .replacePath(ENTERPRISES_URL)
                    .path(contact.getEnterprise().getId().toString());
            this.enterprise = enterpriseUriBuilder.build();

            UriBuilder jobOfferUriBuilder = uriInfo.getAbsolutePathBuilder()
                    .replacePath(JOB_OFFERS_URL)
                    .path(contact.getJobOffer().getId().toString());
            this.jobOffer = jobOfferUriBuilder.build();
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
    }
}
