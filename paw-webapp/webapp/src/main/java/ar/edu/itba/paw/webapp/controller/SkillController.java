package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.SkillService;
import ar.edu.itba.paw.models.exceptions.SkillNotFoundException;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.SkillDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.paginatedOkResponse;

@Path("skills")
@Component
public class SkillController {


    private static final Logger LOGGER = LoggerFactory.getLogger(SkillController.class);
    private static final int SKILLS_BY_PAGE = 20;

    @Context
    private UriInfo uriInfo;
    @Autowired
    private SkillService skillService;

    @GET
    @Produces(ClonedInMediaType.SKILL_LIST_V1)
    public Response listSkills(@QueryParam("page") @DefaultValue("1") @Min(1) final int page) {
        final List<SkillDTO> skills = skillService.getAllSkills(page-1, SKILLS_BY_PAGE).stream()
                .map(skill -> SkillDTO.fromSkill(uriInfo, skill)).collect(Collectors.toList());

        if (skills.isEmpty())
            return Response.noContent().build();

        long skillCount = skillService.getSkillCount();
        long maxPages = skillCount / SKILLS_BY_PAGE + 1;

        return paginatedOkResponse(uriInfo, Response.ok(new GenericEntity<List<SkillDTO>>(skills) {}),
                page, maxPages);
    }

    @GET
    @Path("/{id}")
    @Produces(ClonedInMediaType.SKILL_V1)
    public Response getById(@PathParam("id") @Min(1) final long id) {
        SkillDTO skillDTO = skillService.findById(id).map(skill -> SkillDTO.fromSkill(uriInfo, skill))
                .orElseThrow(() -> new SkillNotFoundException(id));
        return Response.ok(skillDTO).build();
    }
}
