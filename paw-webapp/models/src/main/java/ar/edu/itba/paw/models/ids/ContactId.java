package ar.edu.itba.paw.models.ids;

import java.io.Serializable;
import java.util.Objects;

public class ContactId implements Serializable {
    private Long user;
    private Long jobOffer;

    public ContactId(Long user, Long jobOffer) {
        this.user = user;
        this.jobOffer = jobOffer;
    }

    public ContactId() {
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(Long jobOffer) {
        this.jobOffer = jobOffer;
    }

    // Since the id is a composite key, we need to split it to get the user and job offer ids
    public static long[] splitId(String contactId) {
        String[] stringIds =  contactId.split("-");

        if(stringIds.length != 2)
            throw new IllegalArgumentException("Invalid contact id format. Should be '<userId>-<jobOfferId>'");

        long[] ids = new long[2];

        try {
            ids[0] = Long.parseLong(stringIds[0]);
            ids[1] = Long.parseLong(stringIds[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid contact id format. Should be '<userId>-<jobOfferId>'");
        }
        return ids;
    }

    public static ContactId fromString(String contactId) {
        long[] ids = splitId(contactId);
        return new ContactId(ids[0], ids[1]);
    }

    public String toString() {
        return String.format("%d-%d", user, jobOffer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactId that = (ContactId) o;
        return Objects.equals(user, that.user) && Objects.equals(jobOffer, that.jobOffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, jobOffer);
    }

}
