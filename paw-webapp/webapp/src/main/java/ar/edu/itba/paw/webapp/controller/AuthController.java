package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.webapp.auth.AuthenticationRequest;
import ar.edu.itba.paw.webapp.auth.AuthenticationResponse;
import ar.edu.itba.paw.webapp.auth.AuthenticationService;
import ar.edu.itba.paw.webapp.form.EnterpriseForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

        String token = service.authenticate(request).getAccessToken();

        if(token != null){
            System.out.println("\n\n\n\n TOKEN: " + token + "\n\n\n\n");
            return Response.ok("{\n\t\"access_token\": \"" + token + "\""+"\n}").build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
