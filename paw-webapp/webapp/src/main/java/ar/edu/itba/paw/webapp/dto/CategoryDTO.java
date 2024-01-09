package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Category;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

// TODO: JobOffersInCategory, UsersWithCategory
public class CategoryDTO {
    private static final String CATEGORY_URL = "webapp_war/categories";
    private static final String JOB_OFFERS_URL = "webapp_war/jobOffers";
    private static final String USERS_URL = "webapp_war/users";
    private static final String ENTERPRISES_URL = "webapp_war/enterprises";

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
            final UriBuilder categoryUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath(CATEGORY_URL)
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

            //TODO: GET @ /enterprises
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
