package ar.edu.itba.paw.models;

import java.math.BigDecimal;
import java.util.Objects;

public class JobOfferStatusUserData extends JobOfferWithStatus {

    private final String userName;
    private final long userId;

    public JobOfferStatusUserData(long id, long enterpriseID, Category category, String position, String description, BigDecimal salary,
                                  String modality, String available, String status, String userName, long userId) {
        super(id, enterpriseID, category, position, description, salary, modality, available, status);
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
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
