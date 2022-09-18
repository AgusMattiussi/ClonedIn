package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface ContactDao {

    void addContact(long enterpriseID, long userID, long jobOfferID);

    List<Enterprise> getEnterprisesForUser(long userID);

    List<User> getUsersForEnterprise(long enterpriseID);

    List<JobOffer> getJobOffersForUser(long userId);

    //TODO: void removeContact(long enterpriseID, long userID);

}
