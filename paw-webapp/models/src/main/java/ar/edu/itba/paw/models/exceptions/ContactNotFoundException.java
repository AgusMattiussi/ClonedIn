package ar.edu.itba.paw.models.exceptions;

import ar.edu.itba.paw.models.Contact;
import javax.ws.rs.core.Response;

public class ContactNotFoundException extends ClonedInException{

    private static final int STATUS = Response.Status.NOT_FOUND.getStatusCode();
    private static final String SIMPLE_MESSAGE = "Contact not found";
    private static final String DETAILS = "The contact of the user with id '%d' with the job offer with id '%d' was not found or does not exist";

    public ContactNotFoundException(long userId, long jobOfferId) {
        super(String.format(DETAILS, userId, jobOfferId));
    }

    public ContactNotFoundException(String contactId) {
        // Java forces us to call super in the first line of the constructor
        super(String.format(DETAILS, Contact.splitId(contactId)[0], Contact.splitId(contactId)[1]));
    }

    @Override
    public String getSimpleMessage() {
        return SIMPLE_MESSAGE;
    }

    @Override
    public int getHttpStatus() {
        return STATUS;
    }
}
