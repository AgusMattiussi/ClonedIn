package ar.edu.itba.paw.models.exceptions;

import ar.edu.itba.paw.models.enums.ContactStatus;
import javax.ws.rs.core.Response;

public class ContactStatusException extends ClonedInException {

    private static final int STATUS = Response.Status.CONFLICT.getStatusCode();
    private static final String DETAILS = "Could not change status to '%s' for contact between job offer %d and user %d." +
            " Maybe new status is incompatible with current status or the user is not in a valid application for the job offer.";
    private static final String DETAILS_NO_USER = "Could not change status to '%s' for every contact with job offer %d." +
            " Maybe new status is incompatible with current status or the user is not in a valid application for the job offer.";

    private final ContactStatus contactStatus;

    public ContactStatusException(ContactStatus contactStatus, long jobOfferId, long userId) {
        super(String.format(DETAILS, contactStatus, jobOfferId, userId));
        this.contactStatus = contactStatus;
    }

    public ContactStatusException(ContactStatus contactStatus, long jobOfferId) {
        super(String.format(DETAILS_NO_USER, contactStatus, jobOfferId));
        this.contactStatus = contactStatus;
    }

    @Override
    public String getSimpleMessage() {
        switch (contactStatus) {
            case ACCEPTED:
                return "Could not accept job offer";
            case CANCELLED:
                return "Could not cancel job offer";
            case DECLINED:
                return "Could not reject job offer";
            default:
                return "Could not change job offer status";
        }
    }

    @Override
    public int getHttpStatus() {
        return STATUS;
    }
}
