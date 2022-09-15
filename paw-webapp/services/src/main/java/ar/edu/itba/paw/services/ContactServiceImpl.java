package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactDao contactDao;

    @Autowired
    public ContactServiceImpl(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    public void addContact(long enterpriseID, long userID) {
        contactDao.addContact(enterpriseID, userID);
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(long userID) {
        return contactDao.getEnterprisesForUser(userID);
    }

    @Override
    public List<User> getUsersForEnterprise(long enterpriseID) {
        return contactDao.getUsersForEnterprise(enterpriseID);
    }
}
