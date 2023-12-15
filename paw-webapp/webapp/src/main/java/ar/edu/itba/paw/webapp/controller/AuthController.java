package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.webapp.form.AuthenticationRequest;
import ar.edu.itba.paw.webapp.form.AuthenticationResponse;
import ar.edu.itba.paw.webapp.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.springframework.web.bind.annotation.RequestBody;

@Path("auth")
@Component
public class AuthController {

    private static final String AUTH_HEADER_BASIC = "Basic ";


    @Autowired
    private AuthenticationService authService;
    @Autowired
    private UserDetailsService userDetailsService;

    public AuthController(final AuthenticationService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/access-token")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response authenticate(@Context HttpServletRequest request){
        // Authenticate the user using the HttpBasic credentials provided
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null ||  !authHeader.startsWith(AUTH_HEADER_BASIC)) {
            // TODO: Devolver un mejor error
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String username = authService.getUsernameFromBasicHeader(authHeader);
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        String accessToken = authService.generateAccessToken(user);
        NewCookie refreshTokenCookie = authService.generateRefreshTokenCookie(user, request.getRemoteAddr());

        return Response.ok(new AuthenticationResponse(accessToken))
                .cookie(refreshTokenCookie)
                .build();
    }
}
