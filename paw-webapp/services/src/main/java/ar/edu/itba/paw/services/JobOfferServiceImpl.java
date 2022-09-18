package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.services.JobOfferService;
import ar.edu.itba.paw.models.JobOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferDao jobOfferDao;

    @Autowired
    public JobOfferServiceImpl(JobOfferDao jobOfferDao){
        this.jobOfferDao = jobOfferDao;
    }

    @Override
    public JobOffer create(long enterpriseID, long categoryID, String position, String description, BigDecimal salary) {
        return jobOfferDao.create(enterpriseID, categoryID, position, description, salary);
    }

    @Override
    public Optional<JobOffer> findById(long id) {
        return jobOfferDao.findById(id);
    }

    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID) {
        return jobOfferDao.findByEnterpriseId(enterpriseID);
    }
}
