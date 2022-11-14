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
