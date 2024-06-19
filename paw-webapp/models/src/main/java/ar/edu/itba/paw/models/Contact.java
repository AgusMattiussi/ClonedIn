package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.ContactStatus;
import ar.edu.itba.paw.models.ids.ContactId;

import javax.persistence.*;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "contactado")
@IdClass(ContactId.class)
public class Contact {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEmpresa")
    private Enterprise enterprise;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idOferta")
    private JobOffer jobOffer;

    @Column(name = "estado", length = 20, nullable = false)
    private String status;

    @Column(name = "creadoPor")
    private int filledBy;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Contact(User user, Enterprise enterprise, JobOffer jobOffer, String status, FilledBy filledBy, Date date) {
        if(filledBy.equals(FilledBy.ANY)) {
            throw new InvalidParameterException();
        }

        this.user = user;
        this.enterprise = enterprise;
        this.jobOffer = jobOffer;
        this.status = status;
        this.filledBy = filledBy.getValue();
        this.date = date;
    }

    public Contact(User user, Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy, Date date) {
        if(filledBy.equals(FilledBy.ANY)) {
            throw new InvalidParameterException();
        }

        this.user = user;
        this.enterprise = enterprise;
        this.jobOffer = jobOffer;
        this.status = ContactStatus.PENDING.getStatus();
        this.filledBy = filledBy.getValue();
        this.date = date;
    }

    /* package */ Contact() {
        //Just for Hibernate, we love you!
    }

    public User getUser() {
        return user;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public String getStatus() {
        return status;
    }

    public ContactStatus getStatusEnum() {
        return ContactStatus.fromString(status);
    }

    public int getFilledBy() {
        return filledBy;
    }

    public FilledBy getFilledByEnum() {
        return FilledBy.fromInt(filledBy);
    }

    public String getDate() {
        return date != null ? new SimpleDateFormat("dd/MM/yyyy").format(date) : "";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Contact{");
        sb.append("user=").append(user);
        sb.append(", enterprise=").append(enterprise);
        sb.append(", jobOffer=").append(jobOffer);
        sb.append(", status='").append(status).append('\'');
        sb.append(", filledBy=").append(filledBy);
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }

    // Since the id is a composite key, we need to split it to get the user and job offer ids
    public static long[] splitId(String contactId) {
        String[] stringIds =  contactId.split("-");

        if(stringIds.length != 2)
            throw new IllegalArgumentException("Invalid contact id format. Should be '<userId>-<jobOfferId>'");

        long[] ids = new long[2];

        try {
            ids[0] = Long.parseLong(stringIds[0]);
            ids[1] = Long.parseLong(stringIds[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid contact id format. Should be '<userId>-<jobOfferId>'");
        }

        return ids;
    }

    public String getContactId() {
        return String.format("%d-%d", user.getId(), jobOffer.getId());
    }
}