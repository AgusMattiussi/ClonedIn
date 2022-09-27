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

    boolean alreadyContacted(long userID, long jobOfferID);

    String getStatus(long userID, long jobOfferID);

    void acceptJobOffer(long userID, long jobOfferID);

    /*void rejectJobOffer(long userID, long jobOfferID);*/

    //TODO: void removeContact(long enterpriseID, long userID);

}
