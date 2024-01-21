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
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
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

//TODO: Edit Enterprise

@Path("enterprises")
@Component
public class EnterpriseController {

    public static final int ENTERPRISES_PER_PAGE = 15;
    private static final int CONTACTS_PER_PAGE = 10;
    private static final int JOB_OFFERS_PER_PAGE = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseController.class);

    private static final String USER_OR_PROFILE_OWNER = "hasAuthority('USER') or @securityValidator.isEnterpriseProfileOwner(#id)";
    private static final String PROFILE_OWNER = "hasAuthority('ENTERPRISE') AND @securityValidator.isEnterpriseProfileOwner(#id)";
    private static final String JOB_OFFER_OWNER = "hasAuthority('ENTERPRISE') AND @securityValidator.isEnterpriseProfileOwner(#id) AND @securityValidator.isJobOfferOwner(#joid)";

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private JobOfferSkillService jobOfferSkillService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Context
    private UriInfo uriInfo;

    @Autowired
    public EnterpriseController(final EnterpriseService enterpriseService, final CategoryService categoryService,
                                final JobOfferService jobOfferService, final SkillService skillService,
                                final JobOfferSkillService jobOfferSkillService, final ContactService contactService,
                                final UserService userService, final EmailService emailService) {
        this.enterpriseService = enterpriseService;
        this.categoryService = categoryService;
        this.jobOfferService = jobOfferService;
        this.skillService = skillService;
        this.jobOfferSkillService = jobOfferSkillService;
        this.contactService = contactService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @GET
    @Produces(ClonedInMediaType.ENTERPRISE_LIST_V1)
    @PreAuthorize("hasAuthority('USER')")
    public Response getEnterprises(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                   @QueryParam("categoryName") final String categoryName,
                                   @QueryParam("location") final String location,
                                   @QueryParam("workers") final EmployeeRanges workers,
                                   @QueryParam("enterpriseName") final String enterpriseName,
                                   @QueryParam("searchTerm") final String searchTerm) {

        PaginatedResource<Enterprise> enterprises = enterpriseService.getEnterpriseListByFilters(categoryName, location,
                workers, enterpriseName, searchTerm, page, ENTERPRISES_PER_PAGE);

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


    @GET
    @Path("/{id}/jobOffers")
    @Produces(ClonedInMediaType.JOB_OFFER_LIST_V1)
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getJobOffers(@PathParam("id") @Min(1) final long id,
                                 @QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                 @QueryParam("category") final String categoryName,
                                 @QueryParam("modality") final JobOfferModality modality,
                                 @QueryParam(SKILL_DESCRIPTION_PARAM) final String skillDescription,
                                 @QueryParam("searchTerm") final String searchTerm,
                                 @QueryParam("position") final String position,
                                 @QueryParam("minSalary") @Min(0) final BigDecimal minSalary,
                                 @QueryParam("maxSalary") @Min(0) final BigDecimal maxSalary) {

        PaginatedResource<JobOffer> jobOffers = jobOfferService.getJobOffersListByFilters(categoryName, modality, skillDescription,
                        id, searchTerm, position, minSalary, maxSalary, false, page, JOB_OFFERS_PER_PAGE);

        List<JobOfferDTO> jobOfferDTOS = jobOffers.getPage().stream()
                .map(jobOffer -> JobOfferDTO.fromJobOffer(uriInfo, jobOffer)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOfferDTOS) {}), page,
                jobOffers.getMaxPages());
    }

    //TODO: Mover esta logica al JobOfferController?
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
    public Response getContacts(@PathParam("id") @Min(1) final long id,
                                    @QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                    @QueryParam("status") final JobOfferStatus status,
                                    @QueryParam("filledBy") @DefaultValue("any") FilledBy filledBy,
                                    @QueryParam("sortBy") @DefaultValue("any") final SortBy sortBy,
                                    @QueryParam("userId") @Min(1) final Long userId,
                                    @QueryParam("jobOfferId") @Min(1) final Long jobOfferId) {

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));
        User user = userId != null ? userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)) : null;
        JobOffer jobOffer = jobOfferId != null ? jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId)) : null;

        List<ContactDTO> contactList = contactService.getContactsForEnterprise(enterprise, jobOffer, user, filledBy, status, sortBy,
                page - 1, CONTACTS_PER_PAGE).stream().map(c -> ContactDTO.fromContact(uriInfo, c)).collect(Collectors.toList());

        long contactCount = contactService.getContactsCountForEnterprise(enterprise, jobOffer, user, filledBy, status);
        long maxPages = contactCount / CONTACTS_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactList) {}), page, maxPages);
    }

    @POST
    @Path("/{id}/contacts")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    @Transactional
    public Response contactUser(@PathParam("id") @Min(1) final long id,
                                @Valid @NotNull final ContactForm contactForm){

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));

        if(contactService.alreadyContacted(contactForm.getUserId(), contactForm.getJobOfferId()))
            throw new AlreadyContactedException(contactForm.getUserId(), contactForm.getJobOfferId());

        JobOffer jobOffer = jobOfferService.findById(contactForm.getJobOfferId())
                .orElseThrow(() -> new JobOfferNotFoundException(contactForm.getJobOfferId()));

        if(jobOffer.getEnterpriseID() != id)
            throw new NotJobOfferOwnerException(id, contactForm.getJobOfferId());

        User user = userService.findById(contactForm.getUserId())
                .orElseThrow(() -> new UserNotFoundException(contactForm.getUserId()));

        emailService.sendContactEmail(user, enterprise, jobOffer, contactForm.getMessage(), LocaleContextHolder.getLocale());

        Contact contact = contactService.addContact(enterprise, user, jobOffer, FilledBy.ENTERPRISE);

        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(contact.getJobOffer().getId()))
                .path(String.valueOf(contact.getUser().getId()))
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
                                    @QueryParam("status") final JobOfferStatus status,
                                    @QueryParam("filledBy") @DefaultValue("any") FilledBy filledBy,
                                    @QueryParam("sortBy") @DefaultValue("any") final SortBy sortBy) {

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));
        JobOffer jobOffer = jobOfferService.findById(joid).orElseThrow(() -> new JobOfferNotFoundException(joid));

        List<ContactDTO> contactList = contactService.getContactsForEnterprise(enterprise, jobOffer, null, filledBy, status, sortBy,
                page - 1, CONTACTS_PER_PAGE).stream().map(c -> ContactDTO.fromContact(uriInfo, c)).collect(Collectors.toList());

        long contactCount = contactService.getContactsCountForEnterprise(enterprise, jobOffer, null, filledBy, status);
        long maxPages = contactCount / CONTACTS_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactList) {}), page, maxPages);
    }

    @GET
    @Path("/{id}/contacts/{joid}/{userId}")
    @Produces(ClonedInMediaType.CONTACT_LIST_V1)
    @PreAuthorize(JOB_OFFER_OWNER)
    public Response getContactsByJobOffer(@PathParam("id") @Min(1) final long id,
                                    @PathParam("joid") @Min(1) final long joid,
                                    @PathParam("userId") @Min(1) final long userId) {

        ContactDTO contactDTO = contactService.findByPrimaryKey(userId, joid).map(c -> ContactDTO.fromContact(uriInfo, c))
                .orElseThrow(() -> new ContactNotFoundException(userId, joid));

        return Response.ok(contactDTO).build();
    }

    //TODO: Evaluar que se puede hacer en cada caso, segun FilledBy
   @PUT
   @Path("/{id}/contacts/{joid}/{userId}")
   @PreAuthorize(JOB_OFFER_OWNER)
   public Response updateContactStatus(@PathParam("id") @Min(1) final long id,
                                       @PathParam("userId") @NotNull @Min(1) final long userId,
                                       @PathParam("joid") @NotNull @Min(1) final long joid,
                                       @QueryParam("status") @NotNull final JobOfferStatus status) {

        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        JobOffer jobOffer = jobOfferService.findById(joid).orElseThrow(() -> new JobOfferNotFoundException(joid));
        Contact contact = contactService.findByPrimaryKey(user.getId(), jobOffer.getId())
                .orElseThrow(() -> new ContactNotFoundException(user.getId(), jobOffer.getId()));


       if (status == JobOfferStatus.PENDING)
           throw new IllegalArgumentException("Cannot update contact status to PENDING");

       boolean successful = false;
       switch (status) {
           case ACCEPTED:
               if(contact.getFilledByEnum() == FilledBy.ENTERPRISE)
                   throw new IllegalStateException("Cannot accept a contact that was filled by this enterprise");
               successful = contactService.acceptJobOffer(user, jobOffer);
               break;
           case DECLINED:
               if(contact.getFilledByEnum() == FilledBy.ENTERPRISE)
                   throw new IllegalStateException("Cannot decline a contact that was filled by this enterprise");
               successful = contactService.rejectJobOffer(user, jobOffer);
               break;
           case CANCELLED:
               successful = contactService.cancelJobOffer(user, jobOffer);
               break;
           case CLOSED:
               successful = contactService.closeJobOffer(user, jobOffer);
               break;
       }

       if(!successful)
           throw new IllegalStateException(String.format("Could not update contact status to '%s'", status.getStatus()));

       return Response.noContent().build();
   }

    @PUT
    @Path("/{id}/image")
    @Consumes({ MediaType.MULTIPART_FORM_DATA})
    @PreAuthorize(PROFILE_OWNER)
    @Transactional
    public Response uploadImage(@PathParam("id") @Min(1) final long id,
                                @Size(max = Image.IMAGE_MAX_SIZE_BYTES) @FormDataParam("image") byte[] bytes)  {
        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));
        Image image = imageService.uploadImage(bytes);
        enterpriseService.updateProfileImage(enterprise, image);

        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.ok().location(uri).build();
    }


    @GET
    @Path("/{id}/image")
//     @Produces set dynamically
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getProfileImage(@PathParam("id") @Min(1) final long id) throws IOException {
        Image profileImage = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id)).getImage();
        if(profileImage == null)
            return Response.noContent().build();

        return Response.ok(profileImage.getBytes())
                .type(profileImage.getMimeType()) // Replaces @Produces dynamically
                .build();
    }
}