package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;

public class JobOfferDTO {

    private Long id;
    private Enterprise enterprise;
    private Category category;
    private String position;
    private String description;
    private BigDecimal salary;
    private String modality;
    private String available;
    private URI self;

    public static JobOfferDTO fromJobOffer(final UriInfo uriInfo, final JobOffer jobOffer) {
        final JobOfferDTO dto = new JobOfferDTO();
        dto.id = jobOffer.getId();
        dto.enterprise = jobOffer.getEnterprise();
        dto.category = jobOffer.getCategory();
        dto.position = jobOffer.getPosition();
        dto.description = jobOffer.getDescription();
        dto.salary = jobOffer.getSalary();
        dto.modality = jobOffer.getModality();

        final UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("jobOffers").path(String.valueOf(jobOffer.getId()));
        dto.self = uriBuilder.build();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
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

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
