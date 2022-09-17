package ar.edu.itba.paw.models;

import java.math.BigDecimal;
import java.util.Objects;

public class JobOffer {

    private final long id;
    private final long enterpriseID;
    private final long categoryID;
    private final String position;
    private final String description;
    private final BigDecimal salary;

    public JobOffer(long id, long enterpriseID, long categoryID, String position, String description, BigDecimal salary) {
        this.id = id;
        this.enterpriseID = enterpriseID;
        this.categoryID = categoryID;
        this.position = position;
        this.description = description;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public long getEnterpriseID() {
        return enterpriseID;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", enterpriseID=" + enterpriseID +
                ", categoryID=" + categoryID +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobOffer jobOffer = (JobOffer) o;
        return id == jobOffer.id && enterpriseID == jobOffer.enterpriseID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterpriseID);
    }
}
