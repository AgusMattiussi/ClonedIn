package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.dto.SimpleMessageDTO;
import ar.edu.itba.paw.webapp.utils.ClonedInUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("api/auth")
@Component
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private static final String CHECK_HEADER_MESSAGE = "Check '" + ClonedInUrls.ACCESS_TOKEN_HEADER + "' header for the access token.";

    // This method exists as a 'No-Op' way to get an access token. However, any endpoint can
    // be used for authenticantion.
    @POST
    @Path("/access-token")
    public Response authenticate(){
        return Response.ok(new SimpleMessageDTO(CHECK_HEADER_MESSAGE))
                .build();
    }

}
