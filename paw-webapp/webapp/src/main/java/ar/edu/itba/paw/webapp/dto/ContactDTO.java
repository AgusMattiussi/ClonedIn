package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;

public class ContactDTO {

    private String id;
    private String status;
    private String filledBy;
    private String date;
    private ContactDTOLinks links;
    // TODO: Por favor eliminemos estos campos de abajo
    private String userName;
    private Integer userYearsOfExp;
    private Long userId;


    public static ContactDTO fromContact(final UriInfo uriInfo, final Contact contact, boolean preFetchUserInfo) {
        final ContactDTO dto = new ContactDTO();
        dto.id = contact.getContactId();
        dto.status = contact.getStatus();
        dto.filledBy = FilledBy.fromInt(contact.getFilledBy()).getAsString();
        dto.date = contact.getDate();

        if(preFetchUserInfo) {
            User user = contact.getUser();
            dto.userName = user.getName();
            // TODO: Por que no se podria pedir en GET @ /users/{id}?
            dto.userYearsOfExp = user.getYearsOfExperience();
            dto.userId = user.getId();
        }

        dto.links = new ContactDTOLinks(uriInfo, contact, preFetchUserInfo);

        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static class ContactDTOLinks {
        private URI self;
        private URI user;
        private URI enterprise;
        private URI jobOffer;
        private URI userCategory;

        public ContactDTOLinks() {
        }

        public ContactDTOLinks(UriInfo uriInfo, Contact contact, boolean preFetchUserInfo) {
            UriBuilder contactUriBuilder = uriInfo.getAbsolutePathBuilder()
                    .replacePath(CONTACTS_URL)
                    .path(contact.getContactId());
            this.self = contactUriBuilder.build();

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

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
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
