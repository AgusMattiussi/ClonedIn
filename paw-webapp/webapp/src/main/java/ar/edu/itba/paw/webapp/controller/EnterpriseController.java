package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.EnterpriseDTO;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.paginatedOkResponse;

@Path("api/enterprises")
@Component
public class EnterpriseController {

    private static final int ENTERPRISES_PER_PAGE = 15;
    private static final String S_ENTERPRISES_BY_PAGE = "15";
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseController.class);
    private static final String USER_OR_PROFILE_OWNER = "hasAuthority('USER') or @securityValidator.isEnterpriseProfileOwner(#id)";
    private static final String PROFILE_OWNER = "hasAuthority('ENTERPRISE') AND @securityValidator.isEnterpriseProfileOwner(#id)";

    @Autowired
    private EnterpriseService enterpriseService;

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
                enterpriseForm.getPassword(), enterpriseForm.getLocation(), enterpriseForm.getCategory(), enterpriseForm.getWorkersEnum(),
                enterpriseForm.getYearFounded(), enterpriseForm.getWebsite(), enterpriseForm.getDescription());

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

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize(PROFILE_OWNER)
    public Response updateEnterprise(@PathParam("id") @Min(1) final long id,
                                     @Valid @NotNull final EditEnterpriseForm form) {
        enterpriseService.updateEnterpriseInformation(id, form.getName(), form.getDescription(), form.getLocation(),
                form.getCategory(), form.getWebsite(), form.getYearFounded(), form.getWorkersEnum());

        final URI uri = uriInfo.getAbsolutePath();
        return Response.seeOther(uri).build();
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
    @PreAuthorize(USER_OR_PROFILE_OWNER)
    public Response getProfileImage(@PathParam("id") @Min(1) final long id,
                                    @QueryParam("w") @Min(50) @Max(800) @DefaultValue("220") final int width,
                                    @QueryParam("h") @Min(50) @Max(800) @DefaultValue("220") final int height,
                                    @Context Request request) throws IOException {
        Image profileImage = enterpriseService.getProfileImage(id).orElse(null);
        if(profileImage == null)
            return Response.noContent().build();
            // throw new ImageNotFoundException(id, Role.ENTERPRISE);

        Response.ResponseBuilder responseBuilder = ResponseUtils.getBuilderForCachedResponse(request, profileImage.getEntityTag());

        // Cache Hit
        if(responseBuilder != null){
            return responseBuilder.build();
        }

        // Cache Miss
        return Response.ok(profileImage.getResized(width, height))
                .type(profileImage.getMimeType()) // @Produces
                .tag(profileImage.getEntityTag())
                .build();
    }
}