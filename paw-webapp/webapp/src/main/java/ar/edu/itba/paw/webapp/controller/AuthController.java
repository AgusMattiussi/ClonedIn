package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.CustomUserDetails;
import ar.edu.itba.paw.webapp.dto.SimpleMessageDTO;
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

@Path("api/auth")
@Component
public class AuthController {

    private static final String AUTH_HEADER_BASIC = "Basic ";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private static final String ACCESS_TOKEN_HEADER = "X-Access-Token";
    private static final String CHECK_HEADER_MESSAGE = "Check '" + ACCESS_TOKEN_HEADER + "' header for the access token.";


    @Autowired
    private AuthService authService;
    @Autowired
    private UserDetailsService userDetailsService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }


    // This method exists as a 'No-Op' way to get an access token. However, any endpoint can
    // be used for HTTP Basic authenticantion.
    @POST
    @Path("/access-token")
    public Response authenticate(@Context HttpServletRequest request){
        // Authenticate the user using the HttpBasic credentials provided
        // Should not allow Bearer authentication, since it may lead to a security breach
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null ||  !authHeader.startsWith(AUTH_HEADER_BASIC)) {
            // TODO: Devolver un mejor error
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(new SimpleMessageDTO(CHECK_HEADER_MESSAGE))
                .build();
    }

    @POST
    @Path("/refresh-token")
    public Response refreshToken(@Context HttpServletRequest request, @Context HttpHeaders headers){
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "ClonedInRefreshToken");
        if(refreshTokenCookie == null){
            // TODO: Devolver un mejor error
            LOGGER.warn("An user (IP: {}) tried to refresh the JWT tokens without a refresh token cookie. ",
                    request.getRemoteAddr());
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

        return Response.ok(new SimpleMessageDTO(CHECK_HEADER_MESSAGE))
                .header(ACCESS_TOKEN_HEADER, newAccessToken)
                .cookie(newRefreshTokenCookie)
                .build();
    }
}
