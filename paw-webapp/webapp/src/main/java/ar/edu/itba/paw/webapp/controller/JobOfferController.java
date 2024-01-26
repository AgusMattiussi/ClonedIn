package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.JobOfferModality;
import ar.edu.itba.paw.models.enums.JobOfferSorting;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;


import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.ws.rs.GET;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.SKILL_DESCRIPTION_PARAM;
import static ar.edu.itba.paw.webapp.utils.ResponseUtils.paginatedOkResponse;

@Path("jobOffers")
@Component
public class JobOfferController {

    private static final int JOB_OFFERS_PER_PAGE = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(JobOfferController.class);

    private static final String USER_OR_JOB_OFFER_OWNER = "hasAuthority('USER') or @securityValidator.isJobOfferOwner(#id)";

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    protected AuthenticationManager authenticationManager;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(ClonedInMediaType.JOB_OFFER_LIST_V1)
    @PreAuthorize("hasAuthority('USER')")
    public Response jobOfferList(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                 @QueryParam("categoryName") final String categoryName,
                                 @QueryParam("minSalary") @Min(0) final BigDecimal minSalary,
                                 @QueryParam("maxSalary") @Min(0) final BigDecimal maxSalary,
                                 @QueryParam("modality") final JobOfferModality modality,
                                 @QueryParam("position") final String position,
                                 @QueryParam("searchTerm") final String searchTerm,
                                 @QueryParam(SKILL_DESCRIPTION_PARAM) final String skillDescription,
                                 @QueryParam("enterpriseId") final Long enterpriseId,
                                 @QueryParam("sortBy") @DefaultValue("predeterminado") final JobOfferSorting sortBy){
        PaginatedResource<JobOffer> jobOffers = jobOfferService.getJobOffersListByFilters(categoryName, modality, skillDescription,
                        enterpriseId, searchTerm, position, minSalary, maxSalary, sortBy, true, page, JOB_OFFERS_PER_PAGE);

        if(jobOffers.isEmpty())
            return Response.noContent().build();

        List<JobOfferDTO> jobOfferDTOS = jobOffers.getPage().stream()
                .map(jobOffer -> JobOfferDTO.fromJobOffer(uriInfo, jobOffer)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOfferDTOS) {}),
                page, jobOffers.getMaxPages());
    }

    @GET
    @Path("/{id}")
    @Produces(ClonedInMediaType.JOB_OFFER_V1)
    @PreAuthorize(USER_OR_JOB_OFFER_OWNER)
    public Response getById(@PathParam("id") final long id) {
        JobOfferDTO jobOffer = jobOfferService.findById(id).map(job -> JobOfferDTO.fromJobOffer(uriInfo,job))
                .orElseThrow(() -> new JobOfferNotFoundException(id));
        return Response.ok(jobOffer).build();
    }

    @GET
    @Path("/{id}/skills")
    @Produces(ClonedInMediaType.SKILL_V1)
    @PreAuthorize(USER_OR_JOB_OFFER_OWNER)
    public Response getSkills(@PathParam("id") final long id) {
        List<Skill> skills = jobOfferService.getSkills(id);
        if (skills == null || skills.isEmpty())
            return Response.noContent().build();

        List<SkillDTO> skillDTOs = skills.stream().map(skill -> SkillDTO.fromSkill(uriInfo,skill))
                .collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<SkillDTO>>(skillDTOs) {}).build();
    }
}
