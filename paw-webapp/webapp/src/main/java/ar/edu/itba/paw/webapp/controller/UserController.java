package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
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

        final PaginatedResource<User> users = us.getUsersListByFilters(categoryName, educationLevel, searchTerm, minExpYears,
                maxExpYears, location, skillDescription, page, USERS_PER_PAGE);

        if (users.isEmpty())
            return Response.noContent().build();

        List<UserDTO> userDTOS = users.getPage().stream().map(u -> UserDTO.fromUser(uriInfo,u)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<UserDTO>>(userDTOS) {}), page, users.getMaxPages());
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@NotNull @Valid final UserForm userForm) {
        final User user = us.create(userForm.getEmail(), userForm.getPassword(), userForm.getName(), userForm.getCity(),
                userForm.getCategory(), userForm.getPosition(), userForm.getAboutMe(), userForm.getLevel());

        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(user.getId().toString()).build();
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

        PaginatedResource<Contact> applications = contactService.getContactsForUser(id, FilledBy.USER, status,
                sortBy, page, APPLICATIONS_PER_PAGE);

        if(applications.isEmpty())
            return Response.noContent().build();

        List<ContactDTO> contactDTOs = applications.getPage().stream()
                .map(contact -> ContactDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactDTOs) {}), page,
                applications.getMaxPages());
    }


    @POST
    @Path("/{id}/applications")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response applyToJobOffer(@PathParam("id") @Min(1) final long id,
                                    @NotNull @Valid ApplyToJobOfferForm applyToJobOfferForm) {

        contactService.addContact(id, applyToJobOfferForm.getJobOfferId(), FilledBy.USER);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(applyToJobOfferForm.getJobOfferId())).build();
        return Response.created(uri).build();
    }


    @PUT
    @Path("/{id}/applications/{jobOfferId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response cancelApplication(@PathParam("id") @Min(1) final long id,
                                      @PathParam("jobOfferId") @Min(1) final long jobOfferId) {

        contactService.cancelJobOffer(id, jobOfferId);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(jobOfferId)).build();
        return Response.ok().location(uri).build();
    }


    @PUT
    @Path("/{id}/notifications/{jobOfferId}")
    @PreAuthorize(PROFILE_OWNER)
    public Response updateContactStatus(@PathParam("id") final long id,
                                         @PathParam("jobOfferId") final long jobOfferId,
                                         @NotNull @QueryParam("newStatus") final JobOfferStatus newStatus) {

        contactService.updateContactStatus(id, jobOfferId, newStatus, Role.USER);

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
                                     @QueryParam("status") final JobOfferStatus status) {

        PaginatedResource<Contact> notifications = contactService.getContactsForUser(id, FilledBy.ENTERPRISE, status, sortBy,
                        page, PAGE_SIZE);

        if(notifications.isEmpty())
            return Response.noContent().build();

        List<ContactDTO> contactDTOs = notifications.getPage().stream()
                .map(contact -> ContactDTO.fromContact(uriInfo, contact)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ContactDTO>>(contactDTOs) {}), page,
                notifications.getMaxPages());
    }


    @GET
    @Path("/{id}/experiences")
    @Produces(ClonedInMediaType.EXPERIENCE_LIST_V1)
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getExperiences(@PathParam("id") final long id,
                                   @QueryParam("page") @DefaultValue("1") @Min(1) final int page) {

        PaginatedResource<Experience> experiences = experienceService.findByUser(id, page, EXPERIENCES_PER_PAGE);

        if(experiences.isEmpty())
            return Response.noContent().build();

        List<ExperienceDTO> experienceDTOs = experiences.getPage().stream()
                .map(exp -> ExperienceDTO.fromExperience(uriInfo, exp)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<ExperienceDTO>>(experienceDTOs) {}),
                page, experiences.getMaxPages());
    }

    @GET
    @Path("/{id}/experiences/{expId}")
    @Produces(ClonedInMediaType.EXPERIENCE_V1)
    @PreAuthorize(ENTERPRISE_OR_EXPERIENCE_OWNER)
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

        Experience experience = experienceService.create(id, experienceForm.getMonthFrom(), experienceForm.getYearFrom(),
                experienceForm.getMonthTo(), experienceForm.getYearTo(), experienceForm.getCompany(), experienceForm.getJob(),
                experienceForm.getJobDesc());

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

        PaginatedResource<Education> educations = educationService.findByUser(id, page, EDUCATIONS_PER_PAGE);

        if(educations.isEmpty())
            return Response.noContent().build();

        List<EducationDTO> educationDTOs = educations.getPage().stream()
                .map(ed -> EducationDTO.fromEducation(uriInfo, ed)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<EducationDTO>>(educationDTOs) {}), page,
                educations.getMaxPages());
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

        Education education = educationService.add(id, educationForm.getMonthFrom(), educationForm.getYearFrom(),
                educationForm.getMonthTo(), educationForm.getYearTo(), educationForm.getDegree(), educationForm.getCollege(),
                educationForm.getComment());

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


    @GET
    @Path("/{id}/skills")
    @Produces(ClonedInMediaType.USER_SKILL_LIST_V1)
    @PreAuthorize(ENTERPRISE_OR_PROFILE_OWNER)
    public Response getSkills(@PathParam("id") @Min(1) final long id,
                              @QueryParam("page") @DefaultValue("1") @Min(1) final int page) {

        PaginatedResource<Skill> skills = userSkillService.getSkillsForUser(id, page, SKILLS_PER_PAGE);

        if (skills.isEmpty())
            return Response.noContent().build();

        List<SkillDTO> skillDTOs = skills.getPage().stream()
                .map(skill -> SkillDTO.fromSkill(uriInfo, skill)).collect(Collectors.toList());

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<SkillDTO>>(skillDTOs) {}), page,
                skills.getMaxPages());
    }

    @POST
    @Path("/{id}/skills")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response addSkill(@PathParam("id") @Min(1) final long id,
                             @NotNull @Valid final SkillForm skillForm){

        UserSkill userSkill = userSkillService.addSkillToUser(skillForm.getSkill(), id);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(userSkill.getSkill().getId())).build();
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

        us.updateUserInformation(id, editUserForm.getName(), editUserForm.getAboutMe(), editUserForm.getLocation(),
                editUserForm.getPosition(), editUserForm.getCategory(), editUserForm.getLevel(),
                editUserForm.getVisibilityAsEnum());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();
        return Response.ok().location(uri).build();
    }


    @PUT
    @Path("/{id}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PreAuthorize(PROFILE_OWNER)
    public Response updateImage(@PathParam("id") final long id,
                                @Size(max = Image.IMAGE_MAX_SIZE_BYTES) @FormDataParam("image") byte[] bytes)  {

        us.updateProfileImage(id, bytes);

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