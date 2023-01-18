package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Enterprise;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class EnterpriseDTO {

    private String name;
    private String description;
    private URI self;
    //TODO: Completar con info relevante para empresa


    public static EnterpriseDTO fromEnterprise(final UriInfo uriInfo, final Enterprise enterprise) {
        final EnterpriseDTO dto = new EnterpriseDTO();
        dto.name = enterprise.getEmail();
        dto.description = enterprise.getDescription();

        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("enterprises").path(String.valueOf(enterprise.getId()));
        dto.self = userUriBuilder.build();

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return "EnterpriseDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", self=" + self +
                '}';
    }
}
