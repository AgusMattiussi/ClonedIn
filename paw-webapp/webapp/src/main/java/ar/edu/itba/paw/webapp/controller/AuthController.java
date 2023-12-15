package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.AuthenticationRequest;
import ar.edu.itba.paw.webapp.form.AuthenticationResponse;
import ar.edu.itba.paw.webapp.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.springframework.web.bind.annotation.RequestBody;

@Path("auth")
@Component
public class AuthController {

    @Autowired
    private AuthenticationService service;

    public AuthController(final AuthenticationService service) {
        this.service = service;
    }

    @POST
    @Path("/authenticate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response authenticate(@RequestBody AuthenticationRequest request){

        AuthenticationResponse response;
        try {
            response = service.authenticate(request);
        } catch (AuthenticationException e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(response).build();
    }
}
