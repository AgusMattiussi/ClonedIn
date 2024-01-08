package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
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

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.okResponseWithPagination;

//TODO: Edit Enterprise

@Path("enterprises")
@Component
@Transactional
public class EnterpriseController {

    public static final int PAGE_SIZE = 10;
    private static final int CONTACTS_PER_PAGE = 10;
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
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEnterprise(@NotNull @Valid final EnterpriseForm enterpriseForm) {

        String categoryName = enterpriseForm.getCategory();
        Category category = categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        Enterprise enterprise = enterpriseService.create(enterpriseForm.getEmail(), enterpriseForm.getName(),
                enterpriseForm.getPassword(), enterpriseForm.getCity(), category, enterpriseForm.getWorkers(),
                enterpriseForm.getYear(), enterpriseForm.getLink(), enterpriseForm.getAboutUs());

        emailService.sendRegisterEnterpriseConfirmationEmail(enterpriseForm.getEmail(), enterpriseForm.getName(), LocaleContextHolder.getLocale());

        LOGGER.debug("A new enterprise was registered under id: {}", enterprise.getId());
        LOGGER.info("A new enterprise was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(enterprise.getId())).build();
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
                                 @QueryParam("searchTerm") final String searchTerm,
                                 @QueryParam("position") final String position,
                                 @QueryParam("minSalary") @Min(0) final BigDecimal minSalary,
                                 @QueryParam("maxSalary") @Min(0) final BigDecimal maxSalary) {

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));

        Category category = categoryName != null ? categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName)) : null;

        List<JobOfferDTO> jobOffers = jobOfferService.getJobOffersListByFilters(category, modality, enterprise.getName(),
                        searchTerm, position, minSalary, maxSalary, page - 1, PAGE_SIZE)
                .stream().map(jobOffer -> JobOfferDTO.fromJobOffer(uriInfo, jobOffer)).collect(Collectors.toList());

        long jobOffersCount = jobOfferService.getActiveJobOffersCount(category, modality, enterprise.getName(),
                        searchTerm, position, minSalary, maxSalary);

        long maxPages = jobOffersCount / CONTACTS_PER_PAGE + 1;

        return okResponseWithPagination(uriInfo, Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOffers) {}), page, maxPages);
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
    @Transactional //TODO:Hace falta?
    @PreAuthorize(JOB_OFFER_OWNER)
    public Response updateJobOfferAvailability(@PathParam("id") @Min(1) final long id,
                                               @PathParam("joid") @Min(1) final long joid,
                                               @QueryParam("availability") @NotNull final JobOfferAvailability availability) {

        JobOffer jobOffer = jobOfferService.findById(joid).orElseThrow(() -> new JobOfferNotFoundException(joid));

        switch (availability) {
            case ACTIVE:
                throw new IllegalArgumentException("Cannot update job offer availability to ACTIVE");
            case CLOSED:
                jobOfferService.closeJobOffer(jobOffer);
                break;
            case CANCELLED:
                jobOfferService.cancelJobOffer(jobOffer);
                break;
        }

        //TODO: Chequear si este path funciona
        final URI uri = uriInfo.getAbsolutePath();
        return Response.ok().location(uri).build();
    }

    @POST
    @Path("/{id}/jobOffers")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response createJobOffer(@PathParam("id") @Min(1) final long id,
                                   @Valid @NotNull final JobOfferForm jobOfferForm) {
        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));

        Category category = categoryService.findByName(jobOfferForm.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(jobOfferForm.getCategory()));

        JobOffer jobOffer = jobOfferService.create(enterprise, category, jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(),
                jobOfferForm.getSalary(), jobOfferForm.getModality());

        //TODO: Agregar mas skills a la job offer
        List<String> formSkills = Arrays.asList(jobOfferForm.getSkill1(), jobOfferForm.getSkill2(), jobOfferForm.getSkill3(), jobOfferForm.getSkill4());
        List<Skill> skills = skillService.findMultipleByDescriptionOrCreate(formSkills);
        jobOfferSkillService.addSkillToJobOffer(skills, jobOffer);

        LOGGER.debug("A new job offer was registered under id: {}", jobOffer.getId());
        LOGGER.info("A new job offer was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOffer.getId())).build();
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

        return okResponseWithPagination(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactList) {}), page, maxPages);
    }

    @POST
    @Path("/{id}/contacts")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response contactUser(@PathParam("id") @Min(1) final long id,
                                @Valid @NotNull final ContactForm contactForm){

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));

        if(contactService.alreadyContacted(contactForm.getUserId(), contactForm.getJobOfferId()))
            throw new AlreadyContactedException(contactForm.getUserId(), contactForm.getJobOfferId());

        JobOffer jobOffer = jobOfferService.findById(contactForm.getJobOfferId())
                .orElseThrow(() -> new JobOfferNotFoundException(contactForm.getJobOfferId()));

        User user = userService.findById(contactForm.getUserId())
                .orElseThrow(() -> new UserNotFoundException(contactForm.getUserId()));

        emailService.sendContactEmail(user, enterprise, jobOffer, contactForm.getMessage(), LocaleContextHolder.getLocale());

        Contact contact = contactService.addContact(enterprise, user, jobOffer, FilledBy.ENTERPRISE);

        // TODO: Chequear si funciona este link
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(contact.getJobOffer().getId()))
                .path(String.valueOf(contact.getUser().getId()))
                .build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("/{id}/image")
    @Consumes({ MediaType.MULTIPART_FORM_DATA})
    @PreAuthorize(PROFILE_OWNER)
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
//    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getProfileImage(@PathParam("id") @Min(1) final long id) throws IOException {
        Image profileImage = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id)).getImage();
        if(profileImage == null)
            throw new ImageNotFoundException(id, Role.ENTERPRISE);

        return Response.ok(profileImage.getBytes())
                .type(profileImage.getMimeType()) // Replaces @Produces dynamically
                .build();
    }
}