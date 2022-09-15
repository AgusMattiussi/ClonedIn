package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface ContactService {

    void addContact(long enterpriseID, long userID);

    List<Enterprise> getEnterprisesForUser(long userID);

    List<User> getUsersForEnterprise(long enterpriseID);

    //TODO: void removeContact(long enterpriseID, long userID);
}
