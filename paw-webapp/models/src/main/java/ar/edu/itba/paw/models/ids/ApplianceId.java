package ar.edu.itba.paw.models.ids;

import java.io.Serializable;
import java.util.Objects;

public class ApplianceId implements Serializable {
    private Long user;
    private Long jobOffer;

    public ApplianceId(Long user, Long jobOffer) {
        this.user = user;
        this.jobOffer = jobOffer;
    }

    public ApplianceId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplianceId that = (ApplianceId) o;
        return Objects.equals(user, that.user) && Objects.equals(jobOffer, that.jobOffer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, jobOffer);
    }
}
