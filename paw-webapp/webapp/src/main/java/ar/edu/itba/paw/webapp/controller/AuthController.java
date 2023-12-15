package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.webapp.form.AuthenticationResponse;
import ar.edu.itba.paw.webapp.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.springframework.web.util.WebUtils;

@Path("auth")
@Component
public class AuthController {

    private static final String AUTH_HEADER_BASIC = "Basic ";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthService authService;
    @Autowired
    private UserDetailsService userDetailsService;

    public AuthController(final AuthService authService) {
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

    @POST
    @Path("/refresh-token")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response refreshToken(@Context HttpServletRequest request, @Context HttpHeaders headers){
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "ClonedInRefreshToken");
        if(refreshTokenCookie == null){
            // TODO: Devolver un mejor error
            LOGGER.warn("An user (IP: {}) tried to refresh the JWT tokens without a refresh token cookie. ", request.getRemoteAddr());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String refreshToken = refreshTokenCookie.getValue();
        String ip = request.getRemoteAddr();

        if(!authService.isRefreshTokenValid(refreshToken, ip)){
            // TODO: Devolver un mejor error ("Refresh token invalida")
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String username = authService.getUsernameFromToken(refreshToken);
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        String newAccessToken = authService.generateAccessToken(user);
        NewCookie newRefreshTokenCookie = authService.generateRefreshTokenCookie(user, request.getRemoteAddr());

        return Response.ok(new AuthenticationResponse(newAccessToken))
                .cookie(newRefreshTokenCookie)
                .build();
    }
}
