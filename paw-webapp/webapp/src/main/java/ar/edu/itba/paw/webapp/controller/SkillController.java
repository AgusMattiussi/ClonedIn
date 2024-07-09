package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.SkillService;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.exceptions.SkillNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.SkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.SKILL_DESCRIPTION_PARAM;
import static ar.edu.itba.paw.webapp.utils.ResponseUtils.*;

@Path("api/skills")
@Component
public class SkillController {

    private static final int SKILLS_BY_PAGE = 20;
    private static final String S_SKILLS_BY_PAGE = "20";

    @Context
    private UriInfo uriInfo;
    @Autowired
    private SkillService skillService;

    @GET
    @PreAuthorize("@securityValidator.isGetSkillsValid(#userId, #jobOfferId)")
    @Produces(ClonedInMediaType.SKILL_LIST_V1)
    public Response listSkills(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                               @QueryParam("pageSize") @DefaultValue(S_SKILLS_BY_PAGE)
                                        @Min(1) @Max(2*SKILLS_BY_PAGE) final int pageSize,
                               @QueryParam("userId") final Long userId,
                               @QueryParam("jobOfferId") final Long jobOfferId,
                               @QueryParam(SKILL_DESCRIPTION_PARAM) final String searchTerm) {
        final PaginatedResource<Skill> skills = skillService.getAllSkills(searchTerm, userId, jobOfferId, page, pageSize);

        if (skills.isEmpty())
            return Response.noContent().build();

        List<SkillDTO> skillDTOs = skills.getPage().stream()
                .map(skill -> SkillDTO.fromSkill(uriInfo, skill)).collect(Collectors.toList());

        Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<SkillDTO>>(skillDTOs) {});
        return paginatedOkResponse(uriInfo, responseBuilder, page, skills.getMaxPages());
    }

    @GET
    @Path("/{id}")
    @Produces(ClonedInMediaType.SKILL_V1)
    public Response getById(@PathParam("id") @Min(1) final long id) {
        SkillDTO skillDTO = skillService.findById(id).map(skill -> SkillDTO.fromSkill(uriInfo, skill))
                .orElseThrow(() -> new SkillNotFoundException(id));
        return Response.ok(skillDTO)
                .cacheControl(unconditionalCache(CACHE_1_YEAR))
                .build();
    }
}
