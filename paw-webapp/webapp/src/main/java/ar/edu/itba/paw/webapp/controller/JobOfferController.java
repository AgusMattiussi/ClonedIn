package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.JobOfferModality;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.ws.rs.GET;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.okResponseWithPagination;

@Path("jobOffers")
@Component
public class JobOfferController {

    private static final int JOB_OFFERS_PER_PAGE = 3;

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

    //TODO: Agregar parametro "enterprise" para sacar la logica de job offers del EnterpriseController
    @GET
    @Produces(ClonedInMediaType.JOB_OFFER_LIST_V1)
    @PreAuthorize("hasAuthority('USER')")
    public Response jobOfferList(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                 @QueryParam("categoryName") final String categoryName,
                                 @QueryParam("minSalary") @Min(0) final BigDecimal minSalary,
                                 @QueryParam("maxSalary") @Min(0) final BigDecimal maxSalary,
                                 @QueryParam("modality") final JobOfferModality modality,
                                 @QueryParam("query") final String searchTerm) {

        Category category = categoryName != null ? categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName)) : null;

        final List<JobOfferDTO> jobOfferList = jobOfferService.getJobOffersListByFilters(category, modality, searchTerm,
                        minSalary, maxSalary, page - 1, JOB_OFFERS_PER_PAGE)
                .stream().map(job -> JobOfferDTO.fromJobOffer(uriInfo,job))
                .collect(Collectors.toList());

        if (jobOfferList.isEmpty())
            return Response.noContent().build();

        final long jobOffersCount = jobOfferService.getActiveJobOffersCount(category, modality, searchTerm,
                minSalary, maxSalary);
        long maxPages = jobOffersCount/JOB_OFFERS_PER_PAGE + 1;

        return okResponseWithPagination(uriInfo, Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOfferList) {}),
                page, maxPages);
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
        JobOffer jobOffer = jobOfferService.findById(id).orElseThrow(() -> new JobOfferNotFoundException(id));

        List<SkillDTO> skills = jobOffer.getSkills().stream().map(skill -> SkillDTO.fromSkill(uriInfo,skill))
                .collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<SkillDTO>>(skills) {}).build();
    }
}
