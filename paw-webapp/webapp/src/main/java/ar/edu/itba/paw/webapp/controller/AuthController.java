package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.webapp.auth.AuthenticationRequest;
import ar.edu.itba.paw.webapp.auth.AuthenticationResponse;
import ar.edu.itba.paw.webapp.auth.AuthenticationService;
import ar.edu.itba.paw.webapp.form.EnterpriseForm;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

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

        AuthenticationResponse response = null;
        try {
            response = service.authenticate(request);
        } catch (AuthenticationException e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(response).build();
    }
}
