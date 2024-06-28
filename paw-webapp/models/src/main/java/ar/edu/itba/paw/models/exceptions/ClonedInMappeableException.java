package ar.edu.itba.paw.models.exceptions;

public interface ClonedInMappeableException {

    String getSimpleMessage();

    String getDetails();

    int getHttpStatus();
}
