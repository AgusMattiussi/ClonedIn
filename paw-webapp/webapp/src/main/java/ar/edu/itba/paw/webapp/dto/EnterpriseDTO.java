package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Enterprise;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class EnterpriseDTO {

    private static final String ENTERPRISES_URL = "webapp_war/enterprises";
    private static final String CATEGORIES_URL = "webapp_war/categories";

    private long id;
    private String name;
    private String email;
    private String location;
    private String workers;
    private int year;
    private String website;
    private String description;
    private EnterpriseDTOLinks links;

    public static EnterpriseDTO fromEnterprise(final UriInfo uriInfo, final Enterprise enterprise) {
        final EnterpriseDTO dto = new EnterpriseDTO();
        dto.id = enterprise.getId();
        dto.name = enterprise.getName();
        dto.email = enterprise.getEmail();
        dto.location = enterprise.getLocation();
        dto.workers = enterprise.getWorkers();
        dto.year = enterprise.getYear();
        dto.website = enterprise.getLink();
        dto.description = enterprise.getDescription();

        dto.links = new EnterpriseDTOLinks(uriInfo, enterprise);

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public EnterpriseDTOLinks getLinks() {
        return links;
    }

    public void setLinks(EnterpriseDTOLinks links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "EnterpriseDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static class EnterpriseDTOLinks {
        private URI category;
        private URI self;
        private URI image;

        public EnterpriseDTOLinks() {
        }

        public EnterpriseDTOLinks(UriInfo uriInfo, Enterprise enterprise) {
            UriBuilder enterpriseUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(ENTERPRISES_URL)
                .path(String.valueOf(enterprise.getId()));

            this.self = enterpriseUriBuilder.build();

            this.image = enterpriseUriBuilder.clone().path("image").build();

            UriBuilder categoryUriBuilder = uriInfo.getAbsolutePathBuilder()
                    .replacePath(CATEGORIES_URL)
                    .path(String.valueOf(enterprise.getCategory().getId()));
            this.category = categoryUriBuilder.build();
        }

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
        }

        public URI getImage() {
            return image;
        }

        public void setImage(URI image) {
            this.image = image;
        }

        public URI getCategory() {
            return category;
        }

        public void setCategory(URI category) {
            this.category = category;
        }
    }
}
