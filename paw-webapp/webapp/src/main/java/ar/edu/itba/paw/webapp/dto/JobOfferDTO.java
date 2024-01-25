package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.JobOffer;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;

public class JobOfferDTO {

    private long id;
    private String position;
    private String description;
    private Double salary;
    private String modality;
    private String available;
    private JobOfferDTOLinks links;


    public static JobOfferDTO fromJobOffer(final UriInfo uriInfo, final JobOffer jobOffer) {
        final JobOfferDTO dto = new JobOfferDTO();
        dto.id = jobOffer.getId();
        dto.position = jobOffer.getPosition();
        dto.description = jobOffer.getDescription();
        dto.salary = jobOffer.getSalary() != null ? jobOffer.getSalary().doubleValue() : null;
        dto.modality = jobOffer.getModality();
        dto.available = jobOffer.getAvailable();

        dto.links = new JobOfferDTOLinks(uriInfo, jobOffer);

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public JobOfferDTOLinks getLinks() {
        return links;
    }

    public void setLinks(JobOfferDTOLinks links) {
        this.links = links;
    }

    public static class JobOfferDTOLinks {

        private URI self;
        private URI enterprise;
        private URI category;
        private URI skills;

        public JobOfferDTOLinks() {
        }

        public JobOfferDTOLinks(final UriInfo uriInfo, final JobOffer jobOffer) {
            this.self = uriInfo.getAbsolutePathBuilder()
                    .replacePath(JOB_OFFERS_URL)
                    .path(String.valueOf(jobOffer.getId()))
                    .build();

            this.enterprise = uriInfo.getAbsolutePathBuilder()
                    .replacePath(ENTERPRISES_URL)
                    .path(String.valueOf(jobOffer.getEnterprise().getId()))
                    .build();

            this.category = uriInfo.getAbsolutePathBuilder()
                    .replacePath(CATEGORIES_URL)
                    .path(String.valueOf(jobOffer.getCategory().getId()))
                    .build();

            this.skills = uriInfo.getAbsolutePathBuilder()
                    .replacePath(JOB_OFFERS_URL)
                    .path(String.valueOf(jobOffer.getId()))
                    .path("skills")
                    .build();
        }

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
        }

        public URI getEnterprise() {
            return enterprise;
        }

        public void setEnterprise(URI enterprise) {
            this.enterprise = enterprise;
        }

        public URI getCategory() {
            return category;
        }

        public void setCategory(URI category) {
            this.category = category;
        }

        public URI getSkills() {
            return skills;
        }

        public void setSkills(URI skills) {
            this.skills = skills;
        }
    }
}
