package ar.edu.itba.paw.models.exceptions;

public class AlreadyContactedException extends RuntimeException{

        private static final String MESSAGE_WITH_ID = "The user with id %s has already been contacted for the job offer with id %s.";

        public AlreadyContactedException(long userId, long jobOfferId) {
            super(String.format(MESSAGE_WITH_ID, userId, jobOfferId));
        }
}
