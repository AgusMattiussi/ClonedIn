package ar.edu.itba.paw.models;

import java.math.BigDecimal;
import java.util.Objects;

public class JobOfferWithStatus extends JobOffer{

    private final String status;

    public JobOfferWithStatus(long id, long enterpriseID, Category category, String position, String description, BigDecimal salary, String modality, String available, String status) {
        super(id, enterpriseID, category, position, description, salary, modality, available);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return super.toString() +
                "status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JobOfferWithStatus that = (JobOfferWithStatus) o;
        return status.equals(that.status) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status);
    }
}
