package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("test")
@Component
public class TestController {

    @GET
    public Response sayHello(){
        return Response.ok("Hello from secured endpoint").build();
    }
}
