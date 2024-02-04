package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.ContactDTO;
import ar.edu.itba.paw.webapp.dto.EnterpriseDTO;
import ar.edu.itba.paw.webapp.dto.JobOfferDTO;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.SKILL_DESCRIPTION_PARAM;
import static ar.edu.itba.paw.webapp.utils.ResponseUtils.paginatedOkResponse;

@Path("api/enterprises")
@Component
public class EnterpriseController {

    private static final int ENTERPRISES_PER_PAGE = 15;
    private static final String S_ENTERPRISES_BY_PAGE = "15";
    private static final int CONTACTS_PER_PAGE = 10;
    private static final String S_CONTACTS_BY_PAGE = "10";
    private static final int JOB_OFFERS_PER_PAGE = 5;
    private static final String S_JOB_OFFERS_BY_PAGE = "5";
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseController.class);

    private static final String USER_OR_PROFILE_OWNER = "hasAuthority('USER') or @securityValidator.isEnterpriseProfileOwner(#id)";
    private static final String PROFILE_OWNER = "hasAuthority('ENTERPRISE') AND @securityValidator.isEnterpriseProfileOwner(#id)";
    private static final String JOB_OFFER_OWNER = "hasAuthority('ENTERPRISE') AND @securityValidator.isEnterpriseProfileOwner(#id) AND @securityValidator.isJobOfferOwner(#joid)";

    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private ContactService contactService;
    @Context
    private UriInfo uriInfo;


    @GET
    @Produces(ClonedInMediaType.ENTERPRISE_LIST_V1)
    @PreAuthorize("hasAuthority('USER')")
    public Response getEnterprises(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                   @QueryParam("pageSize") @DefaultValue(S_ENTERPRISES_BY_PAGE)
                                        @Min(1) @Max(2*ENTERPRISES_PER_PAGE) final int pageSize,
                                   @QueryParam("categoryName") final String categoryName,
                                   @QueryParam("location") final String location,
                                   @QueryParam("workers") final EmployeeRanges workers,
                                   @QueryParam("enterpriseName") final String enterpriseName,
                                   @QueryParam("searchTerm") final String searchTerm) {

        PaginatedResource<Enterprise> enterprises = enterpriseService.getEnterpriseListByFilters(categoryName, location,
                workers, enterpriseName, searchTerm, page, pageSize);

        if(enterprises.isEmpty())
            return Response.noContent().build();

        List<EnterpriseDTO> enterpriseDTOList = enterprises.getPage().stream()
                .map(e -> EnterpriseDTO.fromEnterprise(uriInfo, e)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<EnterpriseDTO>>(enterpriseDTOList) {}),
                page, enterprises.getMaxPages());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEnterprise(@NotNull @Valid final EnterpriseForm enterpriseForm) {
        Enterprise enterprise = enterpriseService.create(enterpriseForm.getEmail(), enterpriseForm.getName(),
                enterpriseForm.getPassword(), enterpriseForm.getCity(), enterpriseForm.getCategory(), enterpriseForm.getWorkersEnum(),
                enterpriseForm.getYear(), enterpriseForm.getLink(), enterpriseForm.getAboutUs());

        LOGGER.debug("A new enterprise was registered under id: {}", enterprise.getId());
        LOGGER.info("A new enterprise was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(enterprise.getId().toString()).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}")
    @Produces(ClonedInMediaType.ENTERPRISE_V1)
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getById(@PathParam("id") @Min(1) final long id) {
        EnterpriseDTO enterpriseDTO = enterpriseService.findById(id).map(e -> EnterpriseDTO.fromEnterprise(uriInfo, e))
                .orElseThrow(() -> new EnterpriseNotFoundException(id));
        return Response.ok(enterpriseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response updateEnterprise(@PathParam("id") @Min(1) final long id,
                                     @Valid @NotNull final EditEnterpriseForm form) {
        enterpriseService.updateEnterpriseInformation(id, form.getName(), form.getAboutUs(), form.getCity(),
                form.getCategory(), form.getLink(), form.getYear(), form.getWorkersEnum());

        final URI uri = uriInfo.getAbsolutePath();
        return Response.ok().location(uri).build();
    }


    @GET
    @Path("/{id}/jobOffers")
    @Produces(ClonedInMediaType.JOB_OFFER_LIST_V1)
    @PreAuthorize(PROFILE_OWNER)
    public Response getJobOffers(@PathParam("id") @Min(1) final long id,
                                 @QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                 @QueryParam("pageSize") @DefaultValue(S_JOB_OFFERS_BY_PAGE)
                                        @Min(1) @Max(2*JOB_OFFERS_PER_PAGE) final int pageSize,
                                 @QueryParam("category") final String categoryName,
                                 @QueryParam("modality") final JobOfferModality modality,
                                 @QueryParam(SKILL_DESCRIPTION_PARAM) final String skillDescription,
                                 @QueryParam("searchTerm") final String searchTerm,
                                 @QueryParam("position") final String position,
                                 @QueryParam("minSalary") @Min(0) final BigDecimal minSalary,
                                 @QueryParam("maxSalary") @Min(0) final BigDecimal maxSalary,
                                 @QueryParam("sortBy") @DefaultValue("predeterminado") final JobOfferSorting sorting,
                                 @QueryParam("onlyActive") @DefaultValue("false") final boolean onlyActive) {

        PaginatedResource<JobOffer> jobOffers = jobOfferService.getJobOffersListByFilters(categoryName, modality, skillDescription,
                        id, searchTerm, position, minSalary, maxSalary, sorting, onlyActive, page, pageSize);

        List<JobOfferDTO> jobOfferDTOS = jobOffers.getPage().stream()
                .map(jobOffer -> JobOfferDTO.fromJobOffer(uriInfo, jobOffer)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOfferDTOS) {}), page,
                jobOffers.getMaxPages());
    }

    @GET
    @Path("/{id}/jobOffers/{joid}")
    @Produces(ClonedInMediaType.JOB_OFFER_V1)
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getJobOfferById(@PathParam("id") @Min(1) final long id,
                                    @PathParam("joid") @Min(1) final long joid) {
        JobOfferDTO jobOffer = jobOfferService.findById(joid).map(jo -> JobOfferDTO.fromJobOffer(uriInfo,jo))
                .orElseThrow(() -> new JobOfferNotFoundException(joid));
        return Response.ok(jobOffer).build();
    }

    @PUT
    @Path("/{id}/jobOffers/{joid}")
    @PreAuthorize(JOB_OFFER_OWNER)
    public Response updateJobOfferAvailability(@PathParam("id") @Min(1) final long id,
                                               @PathParam("joid") @Min(1) final long joid,
                                               @QueryParam("availability") @NotNull final JobOfferAvailability availability) {
        jobOfferService.updateJobOfferAvailability(joid, availability);

        final URI uri = uriInfo.getAbsolutePath();
        return Response.ok().location(uri).build();
    }

    @POST
    @Path("/{id}/jobOffers")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response createJobOffer(@PathParam("id") @Min(1) final long id,
                                   @Valid @NotNull final JobOfferForm jobOfferForm) {
        JobOffer jobOffer = jobOfferService.create(id, jobOfferForm.getCategory(), jobOfferForm.getJobPosition(),
                jobOfferForm.getJobDescription(), jobOfferForm.getSalary(), jobOfferForm.getModality(),
                jobOfferForm.getSkillsList());

        LOGGER.debug("A new job offer was registered under id: {}", jobOffer.getId());
        LOGGER.info("A new job offer was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(jobOffer.getId().toString()).build();
        return Response.created(uri).build();
    }


    @GET
    @Path("/{id}/contacts")
    @Produces(ClonedInMediaType.CONTACT_LIST_V1)
    @PreAuthorize(PROFILE_OWNER)
    @Transactional
    public Response getContacts(@PathParam("id") @Min(1) final long id,
                                    @QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                    @QueryParam("pageSize") @DefaultValue(S_CONTACTS_BY_PAGE)
                                        @Min(1) @Max(2*CONTACTS_PER_PAGE) final int pageSize,
                                    @QueryParam("status") final JobOfferStatus status,
                                    @QueryParam("filledBy") @DefaultValue("any") FilledBy filledBy,
                                    @QueryParam("sortBy") @DefaultValue("any") final ContactSorting sortBy,
                                    @QueryParam("userId") @Min(1) final Long userId,
                                    @QueryParam("jobOfferId") @Min(1) final Long jobOfferId) {

        PaginatedResource<Contact> contacts = contactService.getContactsForEnterprise(id, jobOfferId, userId, filledBy,
                status, sortBy, page, pageSize);

        if(contacts.isEmpty())
            return Response.noContent().build();

        List<ContactDTO> contactDTOList = contacts.getPage().stream()
                .map(c -> ContactDTO.fromContact(uriInfo, c, true)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactDTOList) {}), page,
                contacts.getMaxPages());
    }

    @POST
    @Path("/{id}/contacts")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response contactUser(@PathParam("id") @Min(1) final long id,
                                @Valid @NotNull final ContactForm contactForm){

        Contact contact = contactService.addContact(id, contactForm.getUserId(), contactForm.getJobOfferId(),
                FilledBy.ENTERPRISE, contactForm.getMessage());

        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(contact.getJobOffer().getId().toString())
                .path(contact.getUser().getId().toString())
                .build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}/contacts/{joid}")
    @Produces(ClonedInMediaType.CONTACT_LIST_V1)
    @PreAuthorize(JOB_OFFER_OWNER)
    @Transactional
    public Response getContactsByJobOffer(@PathParam("id") @Min(1) final long id,
                                    @PathParam("joid") @Min(1) final long joid,
                                    @QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                    @QueryParam("pageSize") @DefaultValue(S_CONTACTS_BY_PAGE)
                                        @Min(1) @Max(2*CONTACTS_PER_PAGE) final int pageSize,
                                    @QueryParam("status") final JobOfferStatus status,
                                    @QueryParam("filledBy") @DefaultValue("any") FilledBy filledBy,
                                    @QueryParam("sortBy") @DefaultValue("any") final ContactSorting sortBy) {
        PaginatedResource<Contact> contacts = contactService.getContactsForEnterprise(id, joid, null, filledBy,
                status, sortBy, page, pageSize);

        if(contacts.isEmpty())
            return Response.noContent().build();

        List<ContactDTO> contactDTOs = contacts.getPage().stream()
                .map(c -> ContactDTO.fromContact(uriInfo, c, true)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactDTOs) {}), page,
                contacts.getMaxPages());
    }

    @GET
    @Path("/{id}/contacts/{joid}/{userId}")
    @Produces(ClonedInMediaType.CONTACT_LIST_V1)
    @PreAuthorize(JOB_OFFER_OWNER)
    @Transactional
    public Response getContactsByJobOffer(@PathParam("id") @Min(1) final long id,
                                    @PathParam("joid") @Min(1) final long joid,
                                    @PathParam("userId") @Min(1) final long userId) {

        ContactDTO contactDTO = contactService.findByPrimaryKey(userId, joid).map(c -> ContactDTO.fromContact(uriInfo, c, true))
                .orElseThrow(() -> new ContactNotFoundException(userId, joid));

        return Response.ok(contactDTO).build();
    }

    @PUT
    @Path("/{id}/contacts/{joid}/{userId}")
    @PreAuthorize(JOB_OFFER_OWNER)
    public Response updateContactStatus(@PathParam("id") @Min(1) final long id,
                                       @PathParam("userId") @NotNull @Min(1) final long userId,
                                       @PathParam("joid") @NotNull @Min(1) final long joid,
                                       @QueryParam("status") @NotNull final JobOfferStatus status) {

        contactService.updateContactStatus(userId, joid, status, Role.ENTERPRISE);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/image")
    @Consumes({ MediaType.MULTIPART_FORM_DATA})
    @PreAuthorize(PROFILE_OWNER)
    public Response uploadImage(@PathParam("id") @Min(1) final long id,
                                @Size(max = Image.IMAGE_MAX_SIZE_BYTES) @FormDataParam("image") byte[] bytes)  {
        enterpriseService.updateProfileImage(id, bytes);

        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.ok().location(uri).build();
    }


    @GET
    @Path("/{id}/image")
    public Response getProfileImage(@PathParam("id") @Min(1) final long id,
                                    @QueryParam("w") @Min(50) @Max(800) @DefaultValue("220") final int width,
                                    @QueryParam("h") @Min(50) @Max(800) @DefaultValue("220") final int height) throws IOException {
        Image profileImage = enterpriseService.getProfileImage(id).orElse(null);
        if(profileImage == null)
            return Response.noContent().build();

        return Response.ok(profileImage.getResized(width, height))
                .type(profileImage.getMimeType()) // @Produces
                .tag(profileImage.getEntityTag())
                .cacheControl(ResponseUtils.imageCacheControl())
                .build();
    }
}