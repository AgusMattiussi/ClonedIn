package ar.edu.itba.paw.models;

import java.math.BigDecimal;
import java.util.Objects;

public class JobOfferStatusEnterpriseData extends JobOfferWithStatus{
    
    private final String enterpriseName;

    public JobOfferStatusEnterpriseData(long id, long enterpriseID, Category category, String position, String description, BigDecimal salary, String modality, String status, String enterpriseName) {
        super(id, enterpriseID, category, position, description, salary, modality, status);
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    @Override
    public String toString() {
        return super.toString() +
                "enterpriseName='" + enterpriseName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JobOfferStatusEnterpriseData that = (JobOfferStatusEnterpriseData) o;
        return enterpriseName.equals(that.enterpriseName) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enterpriseName);
    }
}
