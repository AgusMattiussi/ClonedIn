package ar.edu.itba.paw.models;

import java.sql.Date;
import java.util.Objects;

public class Education {

    private final long id;
    private final long userId;
    //TODO: Chequear manejo de fechas
    private final Date from;
    private final Date to;
    private final String title;
    private final String institutionName;
    private final String description;

    public Education(long id, long userId, Date from, Date to, String title, String institutionName, String description) {
        this.id = id;
        this.userId = userId;
        this.from = from;
        this.to = to;
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

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
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
                ", from=" + from +
                ", to=" + to +
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
