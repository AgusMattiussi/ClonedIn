package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.CategoryDTO;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.*;

@Path("api/categories")
@Component
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    private static final int CATEGORIES_BY_PAGE = 20;
    private static final String S_CATEGORIES_BY_PAGE = "20";

    @Context
    private UriInfo uriInfo;
    @Autowired
    private CategoryService categoryService;

    @GET
    @Produces(ClonedInMediaType.CATEGORY_LIST_V1)
    public Response listCategories(@QueryParam("page") @DefaultValue("1") @Min(1) final int page,
                                   @QueryParam("pageSize") @DefaultValue(S_CATEGORIES_BY_PAGE)
                                        @Min(1) @Max(2*CATEGORIES_BY_PAGE) final int pageSize,
                                   @QueryParam("searchTerm") final String searchTerm) {
        final PaginatedResource<Category> categories = categoryService.getAllCategories(searchTerm, page, pageSize);

        if (categories.getPage().isEmpty())
            return Response.noContent().build();

        final List<CategoryDTO> categoryDTOs = categories.getPage().stream()
                .map(c -> CategoryDTO.fromCategory(uriInfo, c)).collect(Collectors.toList());

        Response.ResponseBuilder responseBuilder = Response
                .ok(new GenericEntity<List<CategoryDTO>>(categoryDTOs) {})
                .cacheControl(unconditionalCache(CACHE_1_MONTH));

        return paginatedOkResponse(uriInfo, responseBuilder, page, categories.getMaxPages());
    }

    @GET
    @Path("/{id}")
    @Produces(ClonedInMediaType.CATEGORY_V1)
    public Response getById(@PathParam("id") @Min(1) final long id) {
        CategoryDTO categoryDTO = categoryService.findById(id).map(c -> CategoryDTO.fromCategory(uriInfo, c))
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return Response.ok(categoryDTO)
                .cacheControl(unconditionalCache(CACHE_1_YEAR))
                .build();
    }
}
