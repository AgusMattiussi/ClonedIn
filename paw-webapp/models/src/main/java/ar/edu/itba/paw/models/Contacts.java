package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.ids.ContactsId;

import javax.persistence.*;

@Entity
@Table(name = "contactado")
@IdClass(ContactsId.class)
public class Contacts {

    @Id
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", referencedColumnName="id")
    private User user;

    //FIXme: o @OneToMany
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpresa", referencedColumnName="id")
    private Enterprise enterprise;

    @Id
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOferta", referencedColumnName="id")
    private JobOffer jobOffer;

    @Column(name = "estado", length = 20, nullable = false)
    private String status;

    public Contacts(User user, Enterprise enterprise, JobOffer jobOffer, String status) {
        this.user = user;
        this.enterprise = enterprise;
        this.jobOffer = jobOffer;
        this.status = status;
    }

    /* package */ Contacts() {
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