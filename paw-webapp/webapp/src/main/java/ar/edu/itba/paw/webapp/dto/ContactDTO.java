package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.enums.FilledBy;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;

// TODO: Agregar endpoints para poner 'self'?
public class ContactDTO {

    private String status;
    private String filledBy;
    private String date;
    private ContactDTOLinks links;
    private String userName;
    private Integer userYearsOfExp;


    public static ContactDTO fromContact(final UriInfo uriInfo, final Contact contact, boolean preFetchUserInfo) {
        final ContactDTO dto = new ContactDTO();
        dto.status = contact.getStatus();
        dto.filledBy = FilledBy.fromInt(contact.getFilledBy()).getAsString();
        dto.date = contact.getDate();

        if(preFetchUserInfo) {
            dto.userName = contact.getUser().getName();
            dto.userYearsOfExp = contact.getUser().getYearsOfExperience();
        }

        dto.links = new ContactDTOLinks(uriInfo, contact, preFetchUserInfo);

        return dto;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilledBy() {
        return filledBy;
    }

    public void setFilledBy(String filledBy) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserYearsOfExp() {
        return userYearsOfExp;
    }

    public void setUserYearsOfExp(Integer userYearsOfExp) {
        this.userYearsOfExp = userYearsOfExp;
    }

    public static class ContactDTOLinks {
        private URI user;
        private URI enterprise;
        private URI jobOffer;
        private URI userCategory;

        public ContactDTOLinks() {
        }

        public ContactDTOLinks(UriInfo uriInfo, Contact contact, boolean preFetchUserInfo) {
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

            if(preFetchUserInfo) {
                Long categoryId = contact.getUser().getCategory().getId();
                final UriBuilder categoryUriBuilder = uriInfo.getAbsolutePathBuilder()
                        .replacePath(CATEGORIES_URL)
                        .path(categoryId.toString());
                this.userCategory = categoryUriBuilder.build();
            }
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

        public URI getUserCategory() {
            return userCategory;
        }

        public void setUserCategory(URI userCategory) {
            this.userCategory = userCategory;
        }
    }
}
