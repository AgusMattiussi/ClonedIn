package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.utils.ClonedInUrls;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.AuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;
import static ar.edu.itba.paw.webapp.utils.ResponseUtils.paginatedOkResponse;


@Path("users")
@Component
public class UserController {

    private static final int PAGE_SIZE = 10;
    private static final int JOB_OFFERS_PER_PAGE = 3;
    private static final int APPLICATIONS_PER_PAGE = 5;
    private static final int NOTIFICATIONS_PER_PAGE = 5;
    private static final int EXPERIENCES_PER_PAGE = 3;
    private static final int USERS_PER_PAGE = 12;
    private static final int EDUCATIONS_PER_PAGE = 3;
    private static final int SKILLS_PER_PAGE = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String ENTERPRISE_OR_PROFILE_OWNER = "(hasAuthority('ENTERPRISE') and @securityValidator.isUserVisible(#id)) or @securityValidator.isUserProfileOwner(#id)";
    private static final String ENTERPRISE_OR_EXPERIENCE_OWNER = "(hasAuthority('ENTERPRISE') and @securityValidator.isUserVisible(#id)) or (@securityValidator.isUserProfileOwner(#id) and @securityValidator.isExperienceOwner(#expId))";
    private static final String ENTERPRISE_OR_EDUCATION_OWNER = "(hasAuthority('ENTERPRISE') and @securityValidator.isUserVisible(#id)) or (@securityValidator.isUserProfileOwner(#id) and @securityValidator.isEducationOwner(#edId))";
    private static final String PROFILE_OWNER = "hasAuthority('USER') AND @securityValidator.isUserProfileOwner(#id)";
    private static final String EXPERIENCE_OWNER = "hasAuthority('USER') and @securityValidator.isUserProfileOwner(#id) and @securityValidator.isExperienceOwner(#expId)";
    private static final String EDUCATION_OWNER = "hasAuthority('USER') and @securityValidator.isUserProfileOwner(#id) and @securityValidator.isEducationOwner(#edId)";
    private static final String ENTERPRISE = "hasAuthority('ENTERPRISE')";

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
    @Autowired
    private EmailService emailService;
    @Autowired
    private ImageService imageService;
    @Autowired
    protected AuthenticationManager authenticationManager;

    @Context
    private UriInfo uriInfo;

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


    @GET
    @Produces(ClonedInMediaType.USER_LIST_V1)
    @PreAuthorize(ENTERPRISE)
    public Response listUsers(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                              @QueryParam("categoryName") final String categoryName,
                              @QueryParam("educationLevel") final String educationLevel,
                              @QueryParam("searchTerm") final String searchTerm,
                              @QueryParam("minExpYears") @Min(0) final Integer minExpYears,
                              @QueryParam("maxExpYears") @Min(0) final Integer maxExpYears,
                              @QueryParam("location") final String location,
                              @QueryParam(SKILL_DESCRIPTION_PARAM) final String skillDescription) {

        Category category = categoryService.findByName(categoryName).orElse(null);

        final List<UserDTO> allUsers = us.getUsersListByFilters(category, educationLevel, searchTerm, minExpYears, maxExpYears,
                                     location, skillDescription, page-1, USERS_PER_PAGE)
                .stream().map(u -> UserDTO.fromUser(uriInfo,u))
                .collect(Collectors.toList());

        if (allUsers.isEmpty())
            return Response.noContent().build();

        final long userCount = us.getUsersCountByFilters(category, educationLevel, searchTerm, minExpYears, maxExpYears,
                                     location, skillDescription);
        long maxPages = userCount/USERS_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<UserDTO>>(allUsers) {}), page, maxPages);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser (@NotNull @Valid final UserForm userForm) {
        Category category = categoryService.findByName(userForm.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(userForm.getCategory()));

        final User user = us.register(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(),
                category, userForm.getPosition(), userForm.getAboutMe(), userForm.getLevel());

        emailService.sendRegisterUserConfirmationEmail(user, LocaleContextHolder.getLocale());

        LOGGER.debug("A new user was registered under id: {}", user.getId());
        LOGGER.info("A new user was registered");

        final URI uri = uriInfo.getAbsolutePathBuilder().path(user.getId().toString()).build();
        return Response.created(uri).build();
    }


    @GET
    @Path("/{id}")
    @Produces(ClonedInMediaType.USER_V1)
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getById(@PathParam("id") @Min(1) final long id) {
        UserDTO user = us.findById(id).map(u -> UserDTO.fromUser(uriInfo,u))
                .orElseThrow(() -> new UserNotFoundException(id));
        return Response.ok(user).links().build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response getApplications(@PathParam("id") final long id,
                                    @QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                    @QueryParam("sortBy") @DefaultValue(SortBy.ANY_VALUE) final SortBy sortBy,
                                    @QueryParam("status") final JobOfferStatus status) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<ContactDTO> applications = contactService.getContactsForUser(user, FilledBy.USER, status.getStatus(), sortBy, page-1,
                        APPLICATIONS_PER_PAGE).stream().map(contact -> ContactDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        long applicationsCount = contactService.getContactsCountForUser(id, FilledBy.USER, status.getStatus());
        long maxPages = applicationsCount/APPLICATIONS_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(applications) {}), page, maxPages);
    }


    @POST
    @Path("/{id}/applications")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response applyToJobOffer(@PathParam("id") @Min(1) final long id,
                                    @NotNull @Valid ApplyToJobOfferForm applyToJobOfferForm) {

        long jobOfferId = applyToJobOfferForm.getJobOfferId();

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));
        long enterpriseId = jobOffer.getEnterpriseID();
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));


        if(contactService.alreadyContacted(id, jobOfferId)) {
            LOGGER.error("User with ID={} has already applied to job offer with ID={}", id, jobOfferId);
            throw new AlreadyAppliedException(id, jobOfferId);
        }

        contactService.addContact(enterprise, user, jobOffer, FilledBy.USER);
        emailService.sendApplicationEmail(enterprise, user, jobOffer.getPosition(), LocaleContextHolder.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOfferId)).build();
        return Response.created(uri).build();
    }


    @PUT
    @Path("/{id}/applications/{jobOfferId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response cancelApplication(@PathParam("id") @Min(1) final long id,
                                      @PathParam("jobOfferId") @Min(1) final long jobOfferId) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        if(!contactService.cancelJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.CANCELLED, jobOfferId, id);

        emailService.sendCancelApplicationEmail(jobOffer.getEnterprise(), user, jobOffer.getPosition(), LocaleContextHolder.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOfferId)).build();
        return Response.ok().location(uri).build();
    }


    @PUT
    @Path("/{id}/notifications/{jobOfferId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response updateJobOfferStatus(@PathParam("id") final long id,
                                         @PathParam("jobOfferId") final long jobOfferId,
                                         @NotNull @QueryParam("newStatus") final JobOfferStatus newStatus) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        if(newStatus != JobOfferStatus.ACCEPTED && newStatus != JobOfferStatus.DECLINED)
            throw new JobOfferStatusException(newStatus, jobOfferId, id);

        if (newStatus == JobOfferStatus.ACCEPTED && !contactService.acceptJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.ACCEPTED, jobOfferId, id);
        else if (newStatus == JobOfferStatus.DECLINED && !contactService.rejectJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.DECLINED, jobOfferId, id);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOfferId)).build();
        return Response.ok().location(uri).build();
    }


    @GET
    @Path("/{id}/notifications")
    @Produces(ClonedInMediaType.CONTACT_LIST_V1)
    @PreAuthorize(PROFILE_OWNER)
    public Response getNotifications(@PathParam("id") final long id,
                                     @QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                     @QueryParam("sortBy") @DefaultValue(SortBy.ANY_VALUE) final SortBy sortBy,
                                     @QueryParam("status") final String status) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<ContactDTO> notifications = contactService.getContactsForUser(user, FilledBy.ENTERPRISE, status, sortBy, page-1, PAGE_SIZE)
                .stream().map(contact -> ContactDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        long notificationsCount = contactService.getContactsCountForUser(id, FilledBy.ENTERPRISE, status);
        long maxPages = notificationsCount/NOTIFICATIONS_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(notifications) {}), page, maxPages);
    }


    @GET
    @Path("/{id}/experiences")
    @Produces(ClonedInMediaType.EXPERIENCE_LIST_V1)
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getExperiences(@PathParam("id") final long id,
                                   @QueryParam("page") @DefaultValue("1") @Min(1) final int page) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<ExperienceDTO> experiences = experienceService.findByUser(user, page - 1, EXPERIENCES_PER_PAGE)
                .stream().map(exp -> ExperienceDTO.fromExperience(uriInfo, exp)).collect(Collectors.toList());

        if(experiences.isEmpty())
            return Response.noContent().build();

        long experienceCount = experienceService.getExperienceCountForUser(user);
        long maxPages = experienceCount/EXPERIENCES_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ExperienceDTO>>(experiences) {}), page, maxPages);
    }

    @GET
    @Path("/{id}/experiences/{expId}")
    @Produces(ClonedInMediaType.EXPERIENCE_V1)
    @PreAuthorize(ENTERPRISE_OR_EXPERIENCE_OWNER)
    @Transactional
    public Response getExperienceById(@PathParam("id") @Min(1) final long id,
                                      @PathParam("expId") @Min(1) final long expId) {

        ExperienceDTO experienceDTO = experienceService.findById(expId).map(exp -> ExperienceDTO.fromExperience(uriInfo, exp))
                .orElseThrow(() -> new ExperienceNotFoundException(expId));

        return Response.ok(experienceDTO).build();
    }

    @POST
    @Path("/{id}/experiences")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response addExperience(@PathParam("id") final long id,
                                  @NotNull @Valid ExperienceForm experienceForm){

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
    public Response deleteExperienceById(@PathParam("id") @Min(1) final long id,
                                         @PathParam("expId") @Min(1) final long expId) {
        experienceService.deleteExperience(expId);
        return Response.ok().build();
    }


    @GET
    @Path("/{id}/educations")
    @Produces(ClonedInMediaType.EDUCATION_LIST_V1)
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getEducations(@PathParam("id") @Min(1) final long id,
                                  @QueryParam("page") @DefaultValue("1") @Min(1) final int page) {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<EducationDTO> educations = educationService.findByUser(user, page - 1, EDUCATIONS_PER_PAGE)
                .stream().map(ed -> EducationDTO.fromEducation(uriInfo, ed)).collect(Collectors.toList());

        if(educations.isEmpty())
            return Response.noContent().build();

        long educationCount = educationService.getEducationCountForUser(user);
        long maxPages = educationCount/EDUCATIONS_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<EducationDTO>>(educations) {}), page, maxPages);
    }

    @GET
    @Path("/{id}/educations/{edId}")
    @Produces(ClonedInMediaType.EDUCATION_V1)
    @PreAuthorize(ENTERPRISE_OR_EDUCATION_OWNER)
    public Response getEducationById(@PathParam("id") @Min(1) final long id,
                                     @PathParam("edId") @Min(1) final long edId) {

        EducationDTO educationDTO = educationService.findById(edId).map(ed -> EducationDTO.fromEducation(uriInfo, ed))
                .orElseThrow(() -> new EducationNotFoundException(edId));

        return Response.ok(educationDTO).build();
    }

    @POST
    @Path("/{id}/educations")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response addEducation(@PathParam("id") @Min(1) final long id,
                                 @NotNull @Valid final EducationForm educationForm){
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
    public Response deleteEducationById(@PathParam("id") @Min(1) final long id,
                                        @PathParam("edId") @Min(1) final long edId) {
        educationService.deleteEducation(edId);
        return Response.noContent().build();
    }

    // TODO: Paginar?
    @GET
    @Path("/{id}/skills")
    @Produces(ClonedInMediaType.USER_SKILL_LIST_V1)
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    @Transactional
    public Response getSkills(@PathParam("id") @Min(1) final long id,
                              @QueryParam("page") @DefaultValue("1") @Min(1) final int page) {
        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        List<Skill> skills = user.getSkills();
        if (skills == null || skills.isEmpty())
            return Response.noContent().build();

        List<SkillDTO> skillDTOs = skills.stream().map(skill -> SkillDTO.fromSkill(uriInfo, skill))
                .collect(Collectors.toList());

        long skillCount = userSkillService.getSkillCountForUser(user);
        long maxPages = skillCount/SKILLS_PER_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<SkillDTO>>(skillDTOs) {}), page, maxPages);
    }

    @POST
    @Path("/{id}/skills")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    @Transactional
    public Response addSkill(@PathParam("id") @Min(1) final long id,
                             @NotNull @Valid final SkillForm skillForm){
        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Skill skill = skillService.findByDescriptionOrCreate(skillForm.getSkill());

        if(userSkillService.alreadyExists(skill, user))
            throw new IllegalArgumentException(String.format("User with ID=%d already has skill '%s'", id, skillForm.getSkill()));

        userSkillService.addSkillToUser(skill, user);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(skill.getId())).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("/{id}/skills/{skillId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response deleteSkillFromUserById(@PathParam("id") @Min(1) final long id,
                                            @PathParam("skillId") @Min(1) final long skillId) {
        userSkillService.deleteSkillFromUser(id, skillId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @PreAuthorize(PROFILE_OWNER)
    public Response editUser( @PathParam("id") @Min(1) final long id,
                              @NotNull @Valid final EditUserForm editUserForm) {

        Category category = null;

        String formCategory = editUserForm.getCategory();
        if(formCategory != null && !formCategory.isEmpty()) {
            category = categoryService.findByName(editUserForm.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(editUserForm.getCategory()));
        }

        us.updateUserInformation(id, editUserForm.getName(), editUserForm.getAboutMe(), editUserForm.getLocation(),
                editUserForm.getPosition(), category, editUserForm.getLevel());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.ok().location(uri).build();
    }

    @PUT
    @Path("/{id}/visibility")
    @PreAuthorize(PROFILE_OWNER)
    public Response updateVisibility(@PathParam("id") @Min(1) final long id,
                                     @NotNull @QueryParam("visibility") final Visibility visibility) {

        if (visibility == Visibility.INVISIBLE)
            us.hideUserProfile(id);
        else
            us.showUserProfile(id);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.ok().location(uri).build();
    }


    @PUT
    @Path("/{id}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PreAuthorize(PROFILE_OWNER)
    public Response uploadImage(@PathParam("id") final long id,
                                @Size(max = Image.IMAGE_MAX_SIZE_BYTES) @FormDataParam("image") byte[] bytes)  {

        User user = us.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Image image = imageService.uploadImage(bytes);
        us.updateProfileImage(user, image);

        final URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response.ok().location(uri).build();
    }


    @GET
    @Path("/{id}/image")
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getProfileImage(@PathParam("id") @Min(1) final long id) throws IOException {

        Image profileImage = us.findById(id).orElseThrow(() -> new UserNotFoundException(id)).getImage();
        if(profileImage == null)
            return Response.noContent().build();

        return Response.ok(profileImage.getBytes())
                .type(profileImage.getMimeType())
                .build();
    }

}