package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("test")
@Component
public class TestController {

    @GET
    public Response sayHello(){
        return Response.ok("Hello from secured endpoint xdd").build();
    }

    @GET
    @Path("/{id}")
    @PreAuthorize("@securityValidator.canAccessUserProfile(#id)")
    public Response sayHello1(@PathParam("id") final long id){
        return Response.ok("Hello user " + id).build();
    }
}
