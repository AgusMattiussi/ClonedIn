package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Category;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class CategoryDTO {
    private String name;
    private long id;
    private URI self;

    public static CategoryDTO fromCategory(UriInfo uriInfo, final Category category) {
        final CategoryDTO dto = new CategoryDTO();
        dto.name = category.getName();
        dto.id = category.getId();

        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("webapp_war/categories")
                .path(String.valueOf(category.getId()));
        dto.self = userUriBuilder.build();

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

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
