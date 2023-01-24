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
}
