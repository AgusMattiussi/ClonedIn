package ar.edu.itba.paw.models;

import java.math.BigDecimal;
import java.util.Objects;

public class JobOffer {

    private final long id;
    private final long enterpriseID;
    private final Category category;
    private final String position;
    private final String description;
    private final BigDecimal salary;
    private final String modality;

    public JobOffer(long id, long enterpriseID, Category category, String position, String description, BigDecimal salary, String modality) {
        this.id = id;
        this.enterpriseID = enterpriseID;
        this.category = category;
        this.position = position;
        this.description = description;
        this.salary = salary;
        this.modality = modality;
    }

    public long getId() {
        return id;
    }

    public long getEnterpriseID() {
        return enterpriseID;
    }

    public Category getCategory() {
        return category;
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

    public String getModality() {
        return modality;
    }

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", enterpriseID=" + enterpriseID +
                ", category=" + category.toString() +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", salary=" + salary +
                ", modality='" + modality + '\'' +
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
