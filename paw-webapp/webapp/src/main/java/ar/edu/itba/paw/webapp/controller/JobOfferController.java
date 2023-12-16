package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.ws.rs.GET;

@Path("jobOffers")
@Component
public class JobOfferController {

    private static final int JOB_OFFERS_PER_PAGE = 3;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String USER_OR_JOB_OFFER_OWNER = "hasAuthority('USER') or @securityValidator.isJobOfferOwner(#id)";

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JobOfferService jobOfferService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Context
    private UriInfo uriInfo;

    /*//TODO: arreglar para distinguir por usuario
    @GET
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response JobOfferList(@QueryParam("page") @DefaultValue("1") final int page, @Valid final UserFilterForm filterForm,
                                 final ContactOrderForm contactOrderForm*//*, HttpServletRequest request*//*) {

        Optional<Category> optCategory = categoryService.findByName(filterForm.getCategory());
        if (!optCategory.isPresent()) {
            //TODO: Desarrollar errores
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        long categoryID;
        try {
            categoryID = Long.parseLong(filterForm.getCategory());
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid CategoryID {} in 'home'", filterForm.getCategory());
            categoryID = 0;
        }

        BigDecimal minSalaryBigDec = filterForm.getMinSalary();
        BigDecimal maxSalaryBigDec = filterForm.getMaxSalary();
        Category category = categoryService.findById(categoryID).orElse(null);
        String modality = filterForm.getModality();
        String searchTerm = filterForm.getTerm();
        String minSalary = minSalaryBigDec == null ? "" : String.valueOf(minSalaryBigDec);
        String maxSalary = maxSalaryBigDec == null ? "" : String.valueOf(maxSalaryBigDec);

        final long jobOffersCount = jobOfferService.getActiveJobOffersCount(category, modality, searchTerm, minSalaryBigDec, maxSalaryBigDec);

        final List<JobOfferDTO> jobOfferList = jobOfferService.getJobOffersListByFilters(category, modality, searchTerm, minSalaryBigDec,
                maxSalaryBigDec, page - 1, JOB_OFFERS_PER_PAGE).stream().map(u -> JobOfferDTO.fromJobOffer(uriInfo,u)).collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOfferList) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();
    }*/

    //TODO: arreglar para distinguir por usuario
    //TODO: Cambiar a BeanParam
    //TODO: Esta bien BigDecimal en QueryParam? Si no pedir double o similar y convertirlo
    //TODO: Si la category no existe, bad request o resultado vacio?
    // FIXME: Falla cuando minSalary o maxSalary no son numeros
    @GET
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize("hasAuthority('USER')")
    public Response jobOfferList(@QueryParam("page") @DefaultValue("1") final int page,
                                 @QueryParam("category") final String categoryName,
                                 @QueryParam("minSalary") final BigDecimal minSalary,
                                 @QueryParam("maxSalary") final BigDecimal maxSalary,
                                 @QueryParam("modality") @DefaultValue("") final String modality,
                                 @QueryParam("query") @DefaultValue("") final String searchTerm) {

        Category category = null;
        if(categoryName != null && !categoryName.isEmpty()) {
            category = categoryService.findByName(categoryName).orElseThrow(()
                    -> new CategoryNotFoundException(String.format("Category '%s' not found or does not exist", categoryName)));
        }

        final long jobOffersCount = jobOfferService.getActiveJobOffersCount(category, modality, searchTerm, minSalary, maxSalary);
        long maxPages = jobOffersCount/JOB_OFFERS_PER_PAGE + 1;

        final List<JobOfferDTO> jobOfferList = jobOfferService.getJobOffersListByFilters(category, modality, searchTerm, minSalary,
                maxSalary, page - 1, JOB_OFFERS_PER_PAGE).stream().map(job -> JobOfferDTO.fromJobOffer(uriInfo,job))
                .collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOfferList) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last").build();
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(USER_OR_JOB_OFFER_OWNER)
    public Response getById(@PathParam("id") final long id) {
        JobOfferDTO jobOffer = jobOfferService.findById(id).map(job -> JobOfferDTO.fromJobOffer(uriInfo,job))
                .orElseThrow(() -> new NotFoundException(String.format("JobOffer with id '%d' not found or does not exist", id)));
        return Response.ok(jobOffer).build();
    }
}
