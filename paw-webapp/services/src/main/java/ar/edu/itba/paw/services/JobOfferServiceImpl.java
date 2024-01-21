package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.enums.JobOfferAvailability;
import ar.edu.itba.paw.models.enums.JobOfferModality;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.EnterpriseNotFoundException;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.models.enums.JobOfferAvailability.*;

@Primary
@Service
public class JobOfferServiceImpl implements JobOfferService {

    @Autowired
    private JobOfferDao jobOfferDao;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private JobOfferSkillService jobOfferSkillService;

    @Override
    @Transactional
    public JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, JobOfferModality modality) {
        return jobOfferDao.create(enterprise, category, position, description, salary, modality);
    }

    @Override
    @Transactional
    public JobOffer create(long enterpriseId, String categoryName, String position, String description, BigDecimal salary, String modalityName, List<String> skillDescriptions) {
        Enterprise enterprise = enterpriseService.findById(enterpriseId)
                .orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));

        Category category = categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        JobOfferModality modality = modalityName == null || modalityName.isEmpty() ?
                JobOfferModality.NOT_SPECIFIED : JobOfferModality.fromString(modalityName);

        JobOffer jobOffer = this.create(enterprise, category, position, description,
                salary, modality);

        //TODO: Agregar mas skills a la job offer
        List<Skill> skills = skillService.findMultipleByDescriptionOrCreate(skillDescriptions);
        jobOfferSkillService.addSkillToJobOffer(skills, jobOffer);

        return jobOffer;
    }

    @Override
    @Transactional
    public List<Skill> getSkills(long jobOfferId) {
        JobOffer jobOffer = jobOfferDao.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));
        return jobOffer.getSkills();
    }

    @Override
    public List<JobOffer> getAllJobOffers() {
        return jobOfferDao.getAllJobOffers();
    }

    @Override
    public long getJobOffersCount() {
        return jobOfferDao.getJobOffersCount();
    }

    @Override
    public List<JobOffer> getAllJobOffers(int page, int pageSize) {
        return jobOfferDao.getAllJobOffers(page, pageSize);
    }

    @Override
    @Transactional
    public Optional<JobOffer> findById(long id) {
        return jobOfferDao.findById(id);
    }

    @Override
    public List<JobOffer> findByEnterprise(Enterprise enterprise) {
        return jobOfferDao.findByEnterprise(enterprise);
    }

    @Override
    public List<JobOffer> findByEnterprise(Enterprise enterprise, int page, int pageSize) {
        return jobOfferDao.findByEnterprise(enterprise, page, pageSize);
    }

    @Override
    public List<JobOffer> findActiveByEnterprise(Enterprise enterprise) {
        return jobOfferDao.findActiveByEnterprise(enterprise);
    }

    @Override
    public List<JobOffer> findActiveByEnterprise(Enterprise enterprise, int page, int pageSize) {
        return jobOfferDao.findActiveByEnterprise(enterprise, page, pageSize);
    }


    @Override
    public long getJobOffersCountForEnterprise(Enterprise enterprise) {
        return jobOfferDao.getJobOffersCountForEnterprise(enterprise);
    }

    @Override
    public long getActiveJobOffersCountForEnterprise(Enterprise enterprise) {
        return jobOfferDao.getActiveJobOffersCountForEnterprise(enterprise);
    }

    @Override
    public PaginatedResource<JobOffer> getJobOffersListByFilters(String categoryName, JobOfferModality modality, String skillDescription,
                                                                 String enterpriseName, String searchTerm, String position, BigDecimal minSalary,
                                                                 BigDecimal maxSalary, boolean onlyActive, int page, int pageSize) {
        Category category = categoryName != null ? categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName)) : null;

        List<JobOffer> jobOffers = jobOfferDao.getJobOffersListByFilters(category, modality, skillDescription, enterpriseName,
                searchTerm, position, minSalary, maxSalary, onlyActive, page-1, pageSize);

        long jobOffersCount = this.getJobOfferCount(category, modality, skillDescription, enterpriseName,
                        searchTerm, position, minSalary, maxSalary, onlyActive);
        long maxPages = jobOffersCount / pageSize + 1;

        return new PaginatedResource<>(jobOffers, page, maxPages);
    }

    @Override
    public PaginatedResource<JobOffer> getJobOffersListByFilters(String categoryName, JobOfferModality modality, String skillDescription,
                                                                 long enterpriseId, String searchTerm, String position, BigDecimal minSalary,
                                                                 BigDecimal maxSalary, boolean onlyActive, int page, int pageSize) {
        Enterprise enterprise = enterpriseService.findById(enterpriseId)
                .orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));
        return getJobOffersListByFilters(categoryName, modality, skillDescription, enterprise.getName(), searchTerm,
                position, minSalary, maxSalary, onlyActive, page, pageSize);
    }

    @Override
    public List<JobOffer> getJobOffersListByFilters(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary, int page, int pageSize) {
        return jobOfferDao.getJobOffersListByFilters(category, modality, term, minSalary, maxSalary, page, pageSize);
    }

    @Override
    public long getJobOfferCount(Category category, JobOfferModality modality, String skillDescription, String enterpriseName,
                                 String searchTerm, String position, BigDecimal minSalary, BigDecimal maxSalary, boolean onlyActive) {
        return jobOfferDao.getJobOfferCount(category, modality, skillDescription, enterpriseName, searchTerm,
                position, minSalary, maxSalary, onlyActive);
    }

    @Override
    public long getJobOfferCount(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary) {
        return jobOfferDao.getJobOfferCount(category, modality, term, minSalary, maxSalary);
    }

    @Override
    public void closeJobOffer(JobOffer jobOffer) {
        jobOfferDao.closeJobOffer(jobOffer);
    }

    @Override
    public void cancelJobOffer(JobOffer jobOffer) {
        jobOfferDao.cancelJobOffer(jobOffer);
    }

    @Override
    @Transactional
    public void updateJobOfferAvailability(long jobOfferId, JobOfferAvailability availability) {
        JobOffer jobOffer = this.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        switch (availability) {
            case ACTIVE:
                throw new IllegalArgumentException("Cannot update job offer availability to ACTIVE");
            case CLOSED:
                this.closeJobOffer(jobOffer);
                break;
            case CANCELLED:
                this.cancelJobOffer(jobOffer);
                break;
        }
    }
}
