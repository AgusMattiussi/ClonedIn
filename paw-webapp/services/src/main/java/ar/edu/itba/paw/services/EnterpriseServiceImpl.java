package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.models.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseDao enterpriseDao;

    @Autowired
    public EnterpriseServiceImpl(EnterpriseDao enterpriseDao) {
        this.enterpriseDao = enterpriseDao;
    }

    @Override
    public Enterprise create(String email, String name, String password, String location, long categoryId_fk, String description) {
        return enterpriseDao.create(email, name, password, location, categoryId_fk, description);
    }

    @Override
    public Optional<Enterprise> findByEmail(String email) {
        return enterpriseDao.findByEmail(email);
    }

    @Override
    public Optional<Enterprise> findById(long enterpriseId) {
        return enterpriseDao.findById(enterpriseId);
    }
}
