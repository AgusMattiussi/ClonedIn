package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;

import java.util.List;

public interface ContactService {

    void addContact(long enterpriseID, long userID, long jobOfferID);

    List<Enterprise> getEnterprisesForUser(long userID);

    List<User> getUsersForEnterprise(long enterpriseID);

    List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId);


    List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID);

    boolean alreadyContacted(long userID, long jobOfferID);

    String getStatus(long userID, long jobOfferID);

    void acceptJobOffer(long userID, long jobOfferID);

    void rejectJobOffer(long userID, long jobOfferID);

    //TODO: void removeContact(long enterpriseID, long userID);
}
