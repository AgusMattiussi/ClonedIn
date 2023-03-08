package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDTO {

    private String username;
    private URI self;
    //TODO: Completar con info relevante para usuarios
    /*private  URI assignedIssues;
    private URI reportedIssues;*/

    public static UserDTO fromUser(final UriInfo uriInfo, final User user) {
        final UserDTO dto = new UserDTO();
        dto.username = user.getEmail();

        System.out.println("\n\n" + uriInfo);
        System.out.println(uriInfo.getBaseUri());
        System.out.println(uriInfo.getPath());
        System.out.println(uriInfo.getAbsolutePathBuilder().build());

        //TODO: Revisar si hace falta eliminar el webapp_war para deployar
        final UriBuilder userUriBuilder = uriInfo.getBaseUriBuilder().replacePath("webapp_war/users").path(String.valueOf(user.getId()));
        dto.self = userUriBuilder.build();
        //final UriBuilder issuesUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("issues");
        //dto.assignedIssues = issuesUriBuilder.clone().queryParam("assignedTo", String.valueOf(user.getId())).build();
        //dto.reportedIssues = issuesUriBuilder.clone().queryParam("reportedTo", String.valueOf(user.getId())).build();
        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    /*public URI getAssignedIssues() {
        return assignedIssues;
    }

    public void setAssignedIssues(URI assignedIssues) {
        this.assignedIssues = assignedIssues;
    }

    public URI getReportedIssues() {
        return reportedIssues;
    }

    public void setReportedIssues(URI reportedIssues) {
        this.reportedIssues = reportedIssues;
    }*/

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", self=" + self +
                '}';
    }
}
