package ar.edu.itba.paw.models;

import java.sql.Date;
import java.util.Objects;

public class Education {

    private final long id;
    private final long userId;
    //TODO: Chequear manejo de fechas
    private final int monthFrom;
    private final int yearFrom;
    private final int monthTo;
    private final int yearTo;
    private final String title;
    private final String institutionName;
    private final String description;

    public Education(long id, long userId, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description) {
        this.id = id;
        this.userId = userId;
        this.monthFrom = monthFrom;
        this.yearFrom = yearFrom;
        this.monthTo = monthTo;
        this.yearTo = yearTo;
        this.title = title;
        this.institutionName = institutionName;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public int getMonthFrom() {
        return monthFrom;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public int getMonthTo() {
        return monthTo;
    }

    public int getYearTo() {
        return yearTo;
    }

    public String getTitle() {
        return title;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Education{" +
                "id=" + id +
                ", userId=" + userId +
                ", monthFrom=" + monthFrom +
                ", yearFrom=" + yearFrom +
                ", monthTo=" + monthTo +
                ", yearTo=" + yearTo +
                ", title='" + title + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Education education = (Education) o;
        return id == education.id && userId == education.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
