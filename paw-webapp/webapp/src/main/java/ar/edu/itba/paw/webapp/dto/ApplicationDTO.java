package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Contact;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ApplicationDTO {

    //TODO: Esto se puede cambiar por el ID
    private String userName;
    private String enterpriseName;
    private long jobOfferId;
    private int filledBy;
    private URI self;

    public static ApplicationDTO fromContact(final UriInfo uriInfo, final Contact contact) {
        final ApplicationDTO dto = new ApplicationDTO();
        dto.userName = contact.getUser().getName();
        dto.enterpriseName = contact.getEnterprise().getName();
        dto.jobOfferId = contact.getJobOffer().getId();

        //FIXME: Revisar si esta bien formado
        final UriBuilder contactUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("applications")
                .path(String.valueOf(contact.getJobOffer().getId()));
        dto.self = contactUriBuilder.build();

        return dto;
    }
}
