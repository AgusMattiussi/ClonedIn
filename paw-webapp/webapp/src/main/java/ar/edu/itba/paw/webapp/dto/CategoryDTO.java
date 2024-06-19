package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Category;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static ar.edu.itba.paw.webapp.utils.ClonedInUrls.*;

public class CategoryDTO {

    private String name;
    private long id;
    private CategoryDTOLinks links;

    public static CategoryDTO fromCategory(UriInfo uriInfo, final Category category) {
        final CategoryDTO dto = new CategoryDTO();
        dto.name = category.getName();
        dto.id = category.getId();

        dto.links = new CategoryDTOLinks(uriInfo, category);

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CategoryDTOLinks getLinks() {
        return links;
    }

    public void setLinks(CategoryDTOLinks links) {
        this.links = links;
    }

    public static class CategoryDTOLinks {
        private URI self;
        private URI jobOffersInCategory;
        private URI usersInCategory;
        private URI enterprisesInCategory;

        public CategoryDTOLinks(){}

        public CategoryDTOLinks(final UriInfo uriInfo, final Category category) {
            final UriBuilder categoryUriBuilder = uriInfo.getAbsolutePathBuilder()
                    .replacePath(CATEGORIES_URL)
                    .path(String.valueOf(category.getId()));
            this.self = categoryUriBuilder.build();

            this.jobOffersInCategory = uriInfo.getAbsolutePathBuilder()
                    .replacePath(JOB_OFFERS_URL)
                    .queryParam("categoryName", category.getName())
                    .build();

            this.usersInCategory = uriInfo.getAbsolutePathBuilder()
                    .replacePath(USERS_URL)
                    .queryParam("categoryName", category.getName())
                    .build();

            this.enterprisesInCategory = uriInfo.getAbsolutePathBuilder()
                    .replacePath(ENTERPRISES_URL)
                    .queryParam("categoryName", category.getName())
                    .build();
        }

        public URI getSelf() {
            return self;
        }

        public void setSelf(URI self) {
            this.self = self;
        }

        public URI getJobOffersInCategory() {
            return jobOffersInCategory;
        }

        public void setJobOffersInCategory(URI jobOffersInCategory) {
            this.jobOffersInCategory = jobOffersInCategory;
        }

        public URI getUsersInCategory() {
            return usersInCategory;
        }

        public void setUsersInCategory(URI usersInCategory) {
            this.usersInCategory = usersInCategory;
        }

        public URI getEnterprisesInCategory() {
            return enterprisesInCategory;
        }

        public void setEnterprisesInCategory(URI enterprisesInCategory) {
            this.enterprisesInCategory = enterprisesInCategory;
        }
    }
}
