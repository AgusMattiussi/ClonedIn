package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.webapp.api.ClonedInMediaType;
import ar.edu.itba.paw.webapp.dto.CategoryDTO;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.okResponseWithPagination;

@Path("categories")
@Component
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    private static final int CATEGORIES_BY_PAGE = 20;

    @Context
    private UriInfo uriInfo;
    @Autowired
    private CategoryService categoryService;

    @GET
    @Produces(ClonedInMediaType.CATEGORY_LIST_V1)
    public Response listCategories(@QueryParam("page") @DefaultValue("1") @Min(1) final int page) {
        final List<CategoryDTO> categories = categoryService.getAllCategories(page-1, CATEGORIES_BY_PAGE).stream()
                .map(c -> CategoryDTO.fromCategory(uriInfo, c)).collect(Collectors.toList());

        if (categories.isEmpty())
            return Response.noContent().build();

        long categoryCount = categoryService.getCategoryCount();
        long maxPages = categoryCount / CATEGORIES_BY_PAGE + 1;

        return okResponseWithPagination(uriInfo, Response.ok(new GenericEntity<List<CategoryDTO>>(categories) {}),
                page, maxPages);
    }

    @GET
    @Path("/{id}")
    @Produces(ClonedInMediaType.CATEGORY_V1)
    public Response getById(@PathParam("id") @Min(1) final long id) {
        CategoryDTO categoryDTO = categoryService.findById(id).map(c -> CategoryDTO.fromCategory(uriInfo, c))
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return Response.ok(categoryDTO).build();
    }
}
