package ar.edu.itba.paw.models.exceptions;

public class InvalidRefreshTokenException extends ClonedInException {

    private static final String SIMPLE_MESSAGE = "Refresh is invalid";
    private static final String DETAILS = "The refresh token provided in the 'ClonedInRefreshToken' cookie has expired or is invalid. " +
            "Please, authenticate again to get a new refresh token.";
    public static final int STATUS = 603;

    public InvalidRefreshTokenException() {
        super(DETAILS);
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
