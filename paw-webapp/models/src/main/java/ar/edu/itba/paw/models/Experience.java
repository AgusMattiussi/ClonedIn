package ar.edu.itba.paw.models;

import java.sql.Date;

public class Experience {
    private final long id;
    private final long userId;
    private final Date from;
    private final Date to;
    private final String enterpriseName;
    private final String position;
    private final String description;

    public Experience(long id, long userId, Date from, Date to, String enterpriseName, String position, String description) {
        this.id = id;
        this.userId = userId;
        this.from = from;
        this.to = to;
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

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
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
}
