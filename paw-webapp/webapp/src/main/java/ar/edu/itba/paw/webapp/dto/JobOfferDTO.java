package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

public class JobOfferDTO {

    private static final String JOB_OFFERS_URL = "webapp_war/jobOffers";
    private static final String ENTERPRISES_URL = "webapp_war/enterprises";
    private static final String CATEGORIES_URL = "webapp_war/categories";

    private long id;
    private URI enterprise;
    private URI category;
    private String position;
    private String description;
    private double salary;
    private String modality;
    private String available;
    private List<String> skills;
    private URI self;

    public static JobOfferDTO fromJobOffer(final UriInfo uriInfo, final JobOffer jobOffer) {
        final JobOfferDTO dto = new JobOfferDTO();
        dto.id = jobOffer.getId();
        dto.position = jobOffer.getPosition();
        dto.description = jobOffer.getDescription();
        dto.salary = jobOffer.getSalary().doubleValue();
        dto.modality = jobOffer.getModality();


        List<Skill> jobOfferSkills = jobOffer.getSkills();
        dto.skills = jobOfferSkills.stream().map(Skill::getDescription).collect(java.util.stream.Collectors.toList());

        final UriBuilder jobOfferUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(JOB_OFFERS_URL)
                .path(String.valueOf(jobOffer.getId()));
        dto.self = jobOfferUriBuilder.build();

        UriBuilder enterpriseUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(ENTERPRISES_URL)
                .path(String.valueOf(jobOffer.getEnterprise().getId()));
        dto.enterprise = enterpriseUriBuilder.build();

        UriBuilder categoryUriBuilder = uriInfo.getAbsolutePathBuilder()
                .replacePath(CATEGORIES_URL)
                .path(String.valueOf(jobOffer.getCategory().getId()));
        dto.category = categoryUriBuilder.build();

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
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

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
