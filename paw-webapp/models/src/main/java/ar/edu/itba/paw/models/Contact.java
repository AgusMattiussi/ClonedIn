package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.ids.ContactId;

import javax.persistence.*;

@Entity
@Table(name = "contactado")
@IdClass(ContactId.class)
public class Contact {

    //TODO: chequear mapeo de relaciones

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

    public Contact(User user, Enterprise enterprise, JobOffer jobOffer, String status) {
        this.user = user;
        this.enterprise = enterprise;
        this.jobOffer = jobOffer;
        this.status = status;
    }

    public Contact(User user, Enterprise enterprise, JobOffer jobOffer) {
        this.user = user;
        this.enterprise = enterprise;
        this.jobOffer = jobOffer;
        this.status = JobOfferStatus.PENDING.getStatus();
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

    @Override
    public String toString() {
        return "Contacts{" +
                "user=" + user +
                ", enterprise=" + enterprise +
                ", jobOffer=" + jobOffer +
                ", status='" + status + '\'' +
                '}';
    }
}