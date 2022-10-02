package ar.edu.itba.paw.models;

import java.math.BigDecimal;
import java.util.Objects;

public class JobOfferStatusUserData extends JobOfferWithStatus {

    private final String userName;

    public JobOfferStatusUserData(long id, long enterpriseID, Category category, String position, String description, BigDecimal salary, String modality, String status, String userName) {
        super(id, enterpriseID, category, position, description, salary, modality, status);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return super.toString() +
                "userName='" + userName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JobOfferStatusUserData that = (JobOfferStatusUserData) o;
        return userName.equals(that.userName) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName);
    }
}
