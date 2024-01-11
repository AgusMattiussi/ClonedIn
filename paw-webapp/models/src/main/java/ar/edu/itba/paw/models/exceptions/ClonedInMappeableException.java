package ar.edu.itba.paw.models.exceptions;

import javax.ws.rs.core.Response;

public interface ClonedInMappeableException {

    String getSimpleMessage();

    String getDetails();

    Response.Status getHttpStatus();

}
