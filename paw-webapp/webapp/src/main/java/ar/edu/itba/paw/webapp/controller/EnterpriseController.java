package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.webapp.dto.ContactDTO;
import ar.edu.itba.paw.webapp.dto.EnterpriseDTO;
import ar.edu.itba.paw.webapp.dto.ImageDTO;
import ar.edu.itba.paw.webapp.dto.JobOfferDTO;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

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

    private Response.ResponseBuilder responseWithPaginationLinks(Response.ResponseBuilder responseBuilder, int currentPage, long maxPages) {
        if(currentPage > 1)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage - 1).build(), "prev");
        if(currentPage < maxPages)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage + 1).build(), "next");

        return responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", maxPages).build(), "last");
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response createEnterprise(@Valid final EnterpriseForm enterpriseForm) {

        String categoryName = enterpriseForm.getCategory();
        Category category = categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        Enterprise enterprise = enterpriseService.create(enterpriseForm.getEmail(), enterpriseForm.getName(),
                enterpriseForm.getPassword(), enterpriseForm.getCity(), category, enterpriseForm.getWorkers(),
                enterpriseForm.getYear(), enterpriseForm.getLink(), enterpriseForm.getAboutUs());

        // TODO: EMAIL
        emailService.sendRegisterEnterpriseConfirmationEmail(enterpriseForm.getEmail(), enterpriseForm.getName(), LocaleContextHolder.getLocale());

        LOGGER.debug("A new enterprise was registered under id: {}", enterprise.getId());
        LOGGER.info("A new enterprise was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(enterprise.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON,})
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getById(@PathParam("id") final long id) {
        EnterpriseDTO enterpriseDTO = enterpriseService.findById(id).map(e -> EnterpriseDTO.fromEnterprise(uriInfo, e))
                .orElseThrow(() -> new EnterpriseNotFoundException(id));
        return Response.ok(enterpriseDTO).build();
    }

    //TODO: Solo trae las que esten activas. Deberiamos traer todas si es el dueño del perfil?
    @GET
    @Path("/{id}/jobOffers")
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getJobOffers(@PathParam("id") final long id,
                                 @QueryParam("page") @DefaultValue("1") final int page,
                                 @QueryParam("category") final String categoryName,
                                 @QueryParam("modality") final JobOfferModality modality,
                                 @QueryParam("searchTerm") final String searchTerm,
                                 @QueryParam("position") final String position,
                                 @QueryParam("minSalary") final double minSalary,
                                 @QueryParam("maxSalary") final double maxSalary) {

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));

        Category category = null;
        if(categoryName != null)
            category = categoryService.findByName(categoryName).orElseThrow(() -> new CategoryNotFoundException(categoryName));

        List<JobOfferDTO> jobOffers = jobOfferService.getJobOffersListByFilters(category, modality.getModality(), enterprise.getName(),
                        searchTerm, position, BigDecimal.valueOf(minSalary), BigDecimal.valueOf(maxSalary), page - 1, PAGE_SIZE)
                .stream().map(jobOffer -> JobOfferDTO.fromJobOffer(uriInfo, jobOffer)).collect(Collectors.toList());

        long jobOffersCount = jobOfferService.getActiveJobOffersCount(category, modality.getModality(), enterprise.getName(),
                        searchTerm, position, BigDecimal.valueOf(minSalary), BigDecimal.valueOf(maxSalary));
        long maxPages = jobOffersCount / CONTACTS_PER_PAGE + 1;

        return responseWithPaginationLinks(Response.ok(new GenericEntity<List<JobOfferDTO>>(jobOffers) {}), page, maxPages).build();
    }

    @GET
    @Path("/{id}/jobOffers/{joid}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getJobOfferById(@PathParam("id") final long id, @PathParam("joid") final long joid) {
        // TODO: Deberiamos permitir que se devuelvan solo las Active?
        JobOfferDTO jobOffer = jobOfferService.findById(joid).map(jo -> JobOfferDTO.fromJobOffer(uriInfo,jo))
                .orElseThrow(() -> new JobOfferNotFoundException(joid));
        return Response.ok(jobOffer).build();
    }

    @PUT
    @Path("/{id}/jobOffers/{joid}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @Transactional
    @PreAuthorize(JOB_OFFER_OWNER)
    public Response updateJobOfferAvailability(@PathParam("id") final long id, @PathParam("joid") final long joid,
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
        return Response.ok(uri).build();
    }

    @POST
    @Path("/{id}/jobOffers")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @PreAuthorize(PROFILE_OWNER)
    public Response createJobOffer(@PathParam("id") final long id, @Valid final JobOfferForm jobOfferForm) {
        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));

        Category category = categoryService.findByName(jobOfferForm.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(jobOfferForm.getCategory()));

        JobOffer jobOffer = jobOfferService.create(enterprise, category, jobOfferForm.getJobPosition(), jobOfferForm.getJobDescription(),
                jobOfferForm.getSalary(), jobOfferForm.getModality());

        List<String> skills = Arrays.asList(jobOfferForm.getSkill1(), jobOfferForm.getSkill2(), jobOfferForm.getSkill3(), jobOfferForm.getSkill4());
        for(String skill : skills) {
            if(skill != null && !skill.isEmpty()) {
                Skill newSkill = skillService.findByDescriptionOrCreate(skill);
                jobOfferSkillService.addSkillToJobOffer(newSkill, jobOffer);
            }
        }

        LOGGER.debug("A new job offer was registered under id: {}", jobOffer.getId());
        LOGGER.info("A new job offer was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOffer.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}/contacts")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(PROFILE_OWNER)
    public Response getContacts(@PathParam("id") final long id,
                                    @QueryParam("page") @DefaultValue("1") final int page,
                                    @QueryParam("status") final JobOfferStatus status,
                                    @QueryParam("filledBy") @DefaultValue("any") FilledBy filledBy,
                                    @QueryParam("sortBy") @DefaultValue("any") final SortBy sortBy,
                                    @QueryParam("userId") final Long userId,
                                    @QueryParam("jobOfferId") final Long jobOfferId) {

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));
        User user = userId != null ? userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)) : null;
        JobOffer jobOffer = jobOfferId != null ? jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId)) : null;

        List<ContactDTO> contactList = contactService.getContactsForEnterprise(enterprise, jobOffer, user, filledBy, status.getStatus(), sortBy,
                page - 1, CONTACTS_PER_PAGE).stream().map(c -> ContactDTO.fromContact(uriInfo, c)).collect(Collectors.toList());


        long contactCount = contactService.getContactsCountForEnterprise(enterprise, jobOffer, user, filledBy, status.getStatus());
        long maxPages = contactCount / CONTACTS_PER_PAGE + 1;

        return responseWithPaginationLinks(Response.ok(new GenericEntity<List<ContactDTO>>(contactList) {}), page, maxPages).build();
    }

    @POST
    @Path("/{id}/contacts")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @PreAuthorize(PROFILE_OWNER)
    public Response contactUser(@PathParam("id") final long id, @Valid final ContactForm contactForm){

        Enterprise enterprise = enterpriseService.findById(id).orElseThrow(() -> new EnterpriseNotFoundException(id));

        if(contactService.alreadyContacted(contactForm.getUserId(), contactForm.getJobOfferId()))
            throw new AlreadyContactedException(contactForm.getUserId(), contactForm.getJobOfferId());

        JobOffer jobOffer = jobOfferService.findById(contactForm.getJobOfferId())
                .orElseThrow(() -> new JobOfferNotFoundException(contactForm.getJobOfferId()));

        User user = userService.findById(contactForm.getUserId())
                .orElseThrow(() -> new UserNotFoundException(contactForm.getUserId()));

        //TODO: EMAIL
        // emailService.sendContactEmail(optUser.get(), optEnterprise.get(), optJobOffer.get(), contactForm.getMessage(), LocaleContextHolder.getLocale());

        Contact contact = contactService.addContact(enterprise, user, jobOffer, FilledBy.ENTERPRISE);

        // TODO: Chequear si funciona este link
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(contact.getJobOffer().getId()))
                .path(String.valueOf(contact.getUser().getId()))
                .build();
        return Response.created(uri).build();
    }


    @GET
    @Path("/{id}/image")
    //@Produces(value = {"image/webp"})
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getProfileImage(@PathParam("id") final long id) {
        Image profileImage = enterpriseService.findById(id).orElseThrow(() -> new ExperienceNotFoundException(id)).getImage();
        if(profileImage == null)
            throw new ImageNotFoundException();

        //TODO: Chequear que esto ande
        return Response.ok(ImageDTO.fromImage(uriInfo, profileImage)).build();
    }
}