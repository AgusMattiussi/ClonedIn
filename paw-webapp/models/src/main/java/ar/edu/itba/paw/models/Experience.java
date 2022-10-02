package ar.edu.itba.paw.models;

import java.sql.Date;
import java.time.YearMonth;
import java.util.Objects;

public class Experience {
    private final long id;
    private final long userId;
    private final int monthFrom;
    private final int yearFrom;
    private final Integer monthTo;
    private final Integer yearTo;
    private final String enterpriseName;
    private final String position;
    private final String description;

    public Experience(long id, long userId, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        this.id = id;
        this.userId = userId;
        this.monthFrom = monthFrom;
        this.yearFrom = yearFrom;
        this.monthTo = monthTo;
        this.yearTo = yearTo;
        this.enterpriseName = enterpriseName;
        this.position = position;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public int getMonthFrom() {
        return monthFrom;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public Integer getMonthTo() {
        return monthTo;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return id == that.id && userId == that.userId;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "id=" + id +
                ", userId=" + userId +
                ", monthFrom=" + monthFrom +
                ", yearFrom=" + yearFrom +
                ", monthTo=" + monthTo +
                ", yearTo=" + yearTo +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
