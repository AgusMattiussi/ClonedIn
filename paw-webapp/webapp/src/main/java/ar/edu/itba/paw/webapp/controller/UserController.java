package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


@Path("users")
@Component
@Transactional
public class UserController {

    private static final int PAGE_SIZE = 10;
    private static final int JOB_OFFERS_PER_PAGE = 3;
    private static final int APPLICATIONS_PER_PAGE = 3;
    private static final int NOTIFICATIONS_PER_PAGE = APPLICATIONS_PER_PAGE;
    private static final int USERS_PER_PAGE = 8;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String ENTERPRISE_OR_PROFILE_OWNER = "(hasAuthority('ENTERPRISE') and @securityValidator.isUserVisible(#id)) or @securityValidator.isUserProfileOwner(#id)";
    private static final String ENTERPRISE_OR_EXPERIENCE_OWNER = "(hasAuthority('ENTERPRISE') and @securityValidator.isUserVisible(#id)) or (@securityValidator.isUserProfileOwner(#id) and @securityValidator.isExperienceOwner(#expId))";
    private static final String ENTERPRISE_OR_EDUCATION_OWNER = "(hasAuthority('ENTERPRISE') and @securityValidator.isUserVisible(#id)) or (@securityValidator.isUserProfileOwner(#id) and @securityValidator.isEducationOwner(#edId))";
    private static final String PROFILE_OWNER = "hasAuthority('USER') AND @securityValidator.isUserProfileOwner(#id)";
    private static final String EXPERIENCE_OWNER = "hasAuthority('USER') and @securityValidator.isUserProfileOwner(#id) and @securityValidator.isExperienceOwner(#expId)";
    private static final String EDUCATION_OWNER = "hasAuthority('USER') and @securityValidator.isUserProfileOwner(#id) and @securityValidator.isEducationOwner(#edId)";


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService us;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private EducationService educationService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private UserSkillService userSkillService;
    //private final EmailService emailService;
    @Autowired
    protected AuthenticationManager authenticationManager;


    @Context
    private UriInfo uriInfo;
    private final ImageService imageService;

    // TODO: REVISAR EL TEMA DE LOS PERMISOS DE CADA USUARIOS PARA CADA METODO

    @Autowired
    public UserController(final UserService userService, final CategoryService categoryService, final JobOfferService jobOfferService,
                          final EnterpriseService enterpriseService, final ContactService contactService, final ExperienceService experienceService,
                          final EducationService educationService, final SkillService skillService, final UserSkillService userSkillService,
                          ImageService imageService) {
        this.us = userService;
        this.categoryService = categoryService;
        this.jobOfferService = jobOfferService;
        this.enterpriseService = enterpriseService;
        this.contactService = contactService;
        this.experienceService = experienceService;
        this.educationService = educationService;
        this.skillService = skillService;
        this.userSkillService = userSkillService;
        this.imageService = imageService;
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

    @GET
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize("hasAuthority('ENTERPRISE')")
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page,
                              @QueryParam("categoryName") @DefaultValue("") final String categoryName,
                              @QueryParam("educationLevel") @DefaultValue("") final String educationLevel,
                              @QueryParam("searchTerm") @DefaultValue("") final String searchTerm,
                              @QueryParam("minExpYears") @DefaultValue("0") final int minExpYears,
                              @QueryParam("maxExpYears") @DefaultValue("100") final int maxExpYears,
                              @QueryParam("location") @DefaultValue("") final String location,
                              @QueryParam("skillDescription") @DefaultValue("") final String skillDescription) {

        Category category = categoryService.findByName(categoryName).orElse(null);

        final List<UserDTO> allUsers = us.getUsersListByFilters(category, educationLevel, searchTerm, minExpYears, maxExpYears,
                                     location, skillDescription, page-1, USERS_PER_PAGE)
                .stream().map(u -> UserDTO.fromUser(uriInfo,u)).collect(Collectors.toList());

        if (allUsers.isEmpty()) {
            return Response.noContent().build();
        }

        final long userCount = us.getUsersCountByFilters(category, educationLevel, searchTerm, minExpYears, maxExpYears,
                                     location, skillDescription);
        long maxPages = userCount/USERS_PER_PAGE + 1;

        return responseWithPaginationLinks(Response.ok(new GenericEntity<List<UserDTO>>(allUsers) {}), page, maxPages).build();
    }


    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    public Response createUser (@Valid final UserForm userForm) {
        Category category = categoryService.findByName(userForm.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(userForm.getCategory()));

        final User user = us.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(),
                category, userForm.getPosition(), userForm.getAboutMe(), userForm.getLevel());

        //TODO: Mail
        //emailService.sendRegisterUserConfirmationEmail(u, LocaleContextHolder.getLocale());
        //authWithAuthManager(request, userForm.getEmail(), userForm.getPassword());

        LOGGER.debug("A new user was registered under id: {}", user.getId());
        LOGGER.info("A new user was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build();
        return Response.created(uri).build();
    }


    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getById(@PathParam("id") final long id) {
        UserDTO user = us.findById(id).map(u -> UserDTO.fromUser(uriInfo,u))
                .orElseThrow(() -> new UserNotFoundException(id));
        return Response.ok(user).build();
    }

    /* TODO:
    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final long id) {
        //us.deleteById(id);
        return Response.noContent().build();
    }*/


    @GET
    @Path("/{id}/applications")
    @PreAuthorize(PROFILE_OWNER)
    public Response getApplications(@PathParam("id") final long id,
                                    @QueryParam("page") @DefaultValue("1") final int page,
                                    @QueryParam("sortBy") @DefaultValue("any") final SortBy sortBy,
                                    @QueryParam("status") final JobOfferStatus status) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<ContactDTO> applications = contactService.getContactsForUser(user, FilledBy.USER, status.getStatus(), sortBy, page-1,
                        APPLICATIONS_PER_PAGE).stream().map(contact -> ContactDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        long applicationsCount = contactService.getContactsCountForUser(id, FilledBy.USER, status.getStatus());
        long maxPages = applicationsCount/APPLICATIONS_PER_PAGE + 1;

        return responseWithPaginationLinks(Response.ok(new GenericEntity<List<ContactDTO>>(applications) {}), page, maxPages).build();
    }

    @POST
    @Path("/{id}/applications")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    @PreAuthorize(PROFILE_OWNER)
    public Response applyToJobOffer(@PathParam("id") final long id,
                                    @QueryParam("jobOfferId") final long jobOfferId){

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));
        long enterpriseId = jobOffer.getEnterpriseID();
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));


        if(contactService.alreadyContacted(id, jobOfferId)) {
            LOGGER.error("User with ID={} has already applied to job offer with ID={}", id, jobOfferId);
            throw new AlreadyAppliedException(id, jobOfferId);
        }

        contactService.addContact(enterprise, user, jobOffer, FilledBy.USER);

        // TODO: EMAIL
        //emailService.sendApplicationEmail(optEnterprise.get(), optUser.get(), optJobOffer.get().getPosition(), LocaleContextHolder.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOfferId)).build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("/{id}/applications/{jobOfferId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response cancelApplication(@PathParam("id") final long id, @PathParam("jobOfferId") final long jobOfferId) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        if(!contactService.cancelJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.CANCELLED, jobOfferId, id);

        //TODO: EMAIL
        //emailService.sendCancelApplicationEmail(optEnterprise.get(), optUser.get(), optJobOffer.get().getPosition(), LocaleContextHolder.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOfferId)).build();
        return Response.ok(uri).build();
    }

    @PUT
    @Path("/{id}/notifications/{jobOfferId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response updateJobOfferStatus(@PathParam("id") final long id, @PathParam("jobOfferId") final long jobOfferId,
                                         @QueryParam("newStatus") final JobOfferStatus newStatus) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        if(newStatus != JobOfferStatus.ACCEPTED && newStatus != JobOfferStatus.DECLINED)
            throw new JobOfferStatusException(newStatus, jobOfferId, id);

        if (newStatus == JobOfferStatus.ACCEPTED && !contactService.acceptJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.ACCEPTED, jobOfferId, id);
        else if (newStatus == JobOfferStatus.DECLINED && !contactService.rejectJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.DECLINED, jobOfferId, id);

        return Response.ok().build();
    }


    @GET
    @Path("/{id}/notifications")
    @PreAuthorize(PROFILE_OWNER)
    public Response getNotifications(@PathParam("id") final long id,
                                     @QueryParam("page") @DefaultValue("1") final int page,
                                     @QueryParam("sortBy") @DefaultValue("any") final SortBy sortBy,
                                     @QueryParam("status") final String status) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<ContactDTO> notifications = contactService.getContactsForUser(user, FilledBy.ENTERPRISE, status, sortBy, page-1, PAGE_SIZE)
                .stream().map(contact -> ContactDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        long notificationsCount = contactService.getContactsCountForUser(id, FilledBy.ENTERPRISE, status);
        long maxPages = notificationsCount/NOTIFICATIONS_PER_PAGE + 1;

        return responseWithPaginationLinks(Response.ok(new GenericEntity<List<ContactDTO>>(notifications) {}), page, maxPages).build();
    }

    @GET
    @Path("/{id}/experiences")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getExperiences(@PathParam("id") final long id, @QueryParam("page") @DefaultValue("1") final int page) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<ExperienceDTO> experiences = experienceService.findByUser(user)
                .stream().map(exp -> ExperienceDTO.fromExperience(uriInfo, exp)).collect(Collectors.toList());

        if(experiences.isEmpty())
            return Response.noContent().build();

        //TODO: Generar links (paginar)
        /*return Response.ok(new GenericEntity<List<ExperienceDTO>>(experiences) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();*/
        return Response.ok(new GenericEntity<List<ExperienceDTO>>(experiences) {}).build();
    }

    @GET
    @Path("/{id}/experiences/{expId}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(ENTERPRISE_OR_EXPERIENCE_OWNER)
    public Response getExperienceById(@PathParam("id") final long id, @PathParam("expId") final long expId) {

        ExperienceDTO experienceDTO = experienceService.findById(expId).map(exp -> ExperienceDTO.fromExperience(uriInfo, exp))
                .orElseThrow(() -> new ExperienceNotFoundException(expId));

        return Response.ok(experienceDTO).build();
    }

    @POST
    @Path("/{id}/experiences")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    @PreAuthorize(PROFILE_OWNER)
    public Response addExperience(@PathParam("id") final long id, @Valid final ExperienceForm experienceForm){

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        Experience experience = experienceService.create(user, experienceForm.getMonthFrom(), experienceForm.getYearFrom(),
                experienceForm.getMonthTo(), experienceForm.getYearTo(), experienceForm.getCompany(), experienceForm.getJob(),
                experienceForm.getJobDesc());

        LOGGER.debug("A new experience was registered under id: {}", experience.getId());
        LOGGER.info("A new experience was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(experience.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{id}/experiences/{expId}")
    @PreAuthorize(EXPERIENCE_OWNER)
    public Response deleteExperienceById(@PathParam("id") final long id, @PathParam("expId") final long expId) {
        experienceService.deleteExperience(expId);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/educations")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getEducations(@PathParam("id") final long id,
                                  @QueryParam("page") @DefaultValue("1") final int page) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<EducationDTO> educations = educationService.findByUser(user)
                .stream().map(ed -> EducationDTO.fromEducation(uriInfo, ed)).collect(Collectors.toList());

        //TODO: Generar links (paginar)
        /*return Response.ok(new GenericEntity<List<EducationDTO>>(educations) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();*/
        return Response.ok(new GenericEntity<List<EducationDTO>>(educations) {}).build();
    }

    @GET
    @Path("/{id}/educations/{edId}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(ENTERPRISE_OR_EDUCATION_OWNER)
    public Response getEducationById(@PathParam("id") final long id,
                                     @PathParam("edId") final long educationId) {

        EducationDTO educationDTO = educationService.findById(educationId).map(ed -> EducationDTO.fromEducation(uriInfo, ed))
                .orElseThrow(() -> new EducationNotFoundException(educationId));

        return Response.ok(educationDTO).build();
    }

    @POST
    @Path("/{id}/educations")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    @PreAuthorize(PROFILE_OWNER)
    public Response addEducation(@PathParam("id") final long id, @Valid final EducationForm educationForm){
        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        Education education = educationService.add(user, educationForm.getMonthFrom(), educationForm.getYearFrom(),
                educationForm.getMonthTo(), educationForm.getYearTo(), educationForm.getDegree(), educationForm.getCollege(),
                educationForm.getComment());

        LOGGER.debug("A new education was registered under id: {}", education.getId());
        LOGGER.info("A new education was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(education.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{id}/educations/{edId}")
    @PreAuthorize(EDUCATION_OWNER)
    public Response deleteEducationById(@PathParam("id") final long id, @PathParam("edId") final long educationId) {
        educationService.deleteEducation(educationId);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/skills")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getSkills(@PathParam("id") final long id,
                              @QueryParam("page") @DefaultValue("1") final int page) {
        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<UserSkillDTO> skills = userSkillService.getSkillsForUser(user)
                .stream().map(s -> UserSkillDTO.fromSkill(uriInfo, user, s)).collect(Collectors.toList());

        //TODO: Generar links (paginar)
        /*return Response.ok(new GenericEntity<List<UserSkillDTO>>(skills) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 999).build(), "last").build();*/
        return Response.ok(new GenericEntity<List<UserSkillDTO>>(skills) {}).build();
    }

    @GET
    @Path("/{id}/skills/{skillId}")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getSkillById(@PathParam("id") final long id, @PathParam("skillId") final long skillId) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if(!user.hasSkill(skillId))
            throw new IllegalArgumentException(String.format("User with ID=%d does not have skill with ID=%d", id, skillId));

        UserSkillDTO skillDTO = skillService.findById(skillId).map(s -> UserSkillDTO.fromSkill(uriInfo, user, s))
                .orElseThrow(() -> new SkillNotFoundException(skillId));

        return Response.ok(skillDTO).build();
    }

    @POST
    @Path("/{id}/skills")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
    @PreAuthorize(PROFILE_OWNER)
    public Response addSkill(@PathParam("id") final long id, @Valid final SkillForm skillForm){
        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Skill skill = skillService.findByDescriptionOrCreate(skillForm.getSkill());

        if(userSkillService.alreadyExists(skill, user))
            throw new IllegalArgumentException(String.format("User with ID=%d already has skill '%s'", id, skillForm.getSkill()));

        userSkillService.addSkillToUser(skill, user);

        LOGGER.debug("A new skill was registered under id: {}", skill.getId());
        LOGGER.info("A new skill was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(skill.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{id}/skills/{skillId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response deleteSkillFromUserById(@PathParam("id") final long id, @PathParam("skillId") final long skillId) {
        userSkillService.deleteSkillFromUser(id, skillId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @PreAuthorize(PROFILE_OWNER)
    public Response editUser( @PathParam("id") final long id, @Valid final EditUserForm editUserForm) {

        Category category = null;

        String formCategory = editUserForm.getCategory();
        if(formCategory != null && !formCategory.isEmpty()) {
            category = categoryService.findByName(editUserForm.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(editUserForm.getCategory()));
        }

        us.updateUserInformation(id, editUserForm.getName(), editUserForm.getAboutMe(), editUserForm.getLocation(),
                editUserForm.getPosition(), category, editUserForm.getLevel());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.ok(uri).build();
    }

    @PUT
    @Path("/{id}/visibility")
    @PreAuthorize(PROFILE_OWNER)
    public Response updateVisibility(@PathParam("id") final long id,
                                     @QueryParam("visibility") final Visibility visibility) {

        if (visibility == Visibility.INVISIBLE)
            us.hideUserProfile(id);
        else
            us.showUserProfile(id);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.ok(uri).build();
    }

    @PUT
    @Path("/{id}/image")
    @Produces({ MediaType.APPLICATION_JSON, })
    @PreAuthorize(PROFILE_OWNER)
    public Response uploadImage(@PathParam("id") final long id,
                                @Valid final ImageForm imageForm) {

        try {
            Image image = imageService.uploadImage(imageForm.getImage().getBytes());
            us.updateProfileImage(id, image);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid image");
        }

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.ok(uri).build();
    }

    @GET
    @Path("/{id}/image")
    //@Produces(value = {"image/webp"})
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getProfileImage(@PathParam("id") final long id) {

        Image profileImage = us.findById(id).orElseThrow(() -> new UserNotFoundException(id)).getImage();
        if(profileImage == null)
            throw new ImageNotFoundException();

        //TODO: Chequear que esto ande
        return Response.ok(ImageDTO.fromImage(uriInfo, profileImage)).build();
    }

}