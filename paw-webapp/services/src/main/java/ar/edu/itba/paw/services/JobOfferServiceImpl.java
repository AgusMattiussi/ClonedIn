package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.enums.JobOfferAvailability;
import ar.edu.itba.paw.models.enums.JobOfferModality;
import ar.edu.itba.paw.models.enums.JobOfferSorting;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.EnterpriseNotFoundException;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class JobOfferServiceImpl implements JobOfferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobOfferServiceImpl.class);

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
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, JobOfferModality modality) {
        return jobOfferDao.create(enterprise, category, position, description, salary, modality);
    }

    @Override
    @Transactional
    public JobOffer create(long enterpriseId, String categoryName, String position, String description, BigDecimal salary, String modalityName, List<String> skillDescriptions) {
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise with id {} was not found - create", enterpriseId);
            return new EnterpriseNotFoundException(enterpriseId);
        });

        Category category = categoryService.findByName(categoryName).orElseThrow(() -> {
            LOGGER.error("Category with name {} was not found - create", categoryName);
            return new CategoryNotFoundException(categoryName);
        });

        JobOfferModality modality = JobOfferModality.fromString(modalityName);

        JobOffer jobOffer = this.create(enterprise, category, position, description,
                salary, modality);

        LOGGER.debug("A new job offer was registered under id: {}", jobOffer.getId());
        LOGGER.info("A new job offer was registered");

        List<Skill> skills = skillService.findMultipleByDescriptionOrCreate(skillDescriptions);
        jobOfferSkillService.addSkillToJobOffer(skills, jobOffer);

        return jobOffer;
    }

    @Override
    @Transactional
    public List<Skill> getSkills(long jobOfferId) {
        JobOffer jobOffer = jobOfferDao.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job offer with id {} was not found - getSkills", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });
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
                                                                 Long enterpriseId, String searchTerm, String position, BigDecimal minSalary,
                                                                 BigDecimal maxSalary, JobOfferSorting sortBy, boolean onlyActive, int page, int pageSize) {

        Category category = categoryName != null ? categoryService.findByName(categoryName).orElseThrow(() -> {
            LOGGER.error("Category with name {} was not found - getJobOffersListByFilters", categoryName);
            return new CategoryNotFoundException(categoryName);
        }) : null;

        if(enterpriseId != null) {
            enterpriseService.findById(enterpriseId).orElseThrow(() -> {
                LOGGER.error("Enterprise with id {} was not found - getJobOffersListByFilters", enterpriseId);
                return new EnterpriseNotFoundException(enterpriseId);
            });
        }

        List<JobOffer> jobOffers = jobOfferDao.getJobOffersListByFilters(category, modality, skillDescription, enterpriseId,
                searchTerm, position, minSalary, maxSalary, sortBy != null ? sortBy : JobOfferSorting.DEFAULT, onlyActive, page-1, pageSize);

        long jobOffersCount = this.getJobOfferCount(category, modality, skillDescription, enterpriseId,
                        searchTerm, position, minSalary, maxSalary, onlyActive);
        long maxPages = jobOffersCount / pageSize + jobOffersCount % pageSize;

        return new PaginatedResource<>(jobOffers, page, maxPages);
    }


    @Override
    public List<JobOffer> getJobOffersListByFilters(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary, int page, int pageSize) {
        return jobOfferDao.getJobOffersListByFilters(category, modality, term, minSalary, maxSalary, page, pageSize);
    }

    @Override
    public long getJobOfferCount(Category category, JobOfferModality modality, String skillDescription, Long enterpriseId,
                                 String searchTerm, String position, BigDecimal minSalary, BigDecimal maxSalary, boolean onlyActive) {
        return jobOfferDao.getJobOfferCount(category, modality, skillDescription, enterpriseId, searchTerm,
                position, minSalary, maxSalary, onlyActive);
    }

    @Override
    public long getJobOfferCount(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary) {
        return jobOfferDao.getJobOfferCount(category, modality, term, minSalary, maxSalary);
    }

    @Override
    @Transactional
    public void closeJobOffer(JobOffer jobOffer) {
        jobOfferDao.closeJobOffer(jobOffer);
        LOGGER.debug("Job offer with id {} was closed", jobOffer.getId());
        emailService.sendCloseJobOfferEmailToAllApplicants(jobOffer, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public void cancelJobOffer(JobOffer jobOffer) {
        jobOfferDao.cancelJobOffer(jobOffer);
        LOGGER.debug("Job offer with id {} was cancelled", jobOffer.getId());
    }

    @Override
    @Transactional
    public void updateJobOfferAvailability(long jobOfferId, JobOfferAvailability availability) {
        JobOffer jobOffer = this.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("Job offer with id {} was not found - updateJobOfferAvailability", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        switch (availability) {
            case ACTIVE:
                LOGGER.error("Cannot update job offer with id {} availability to ACTIVE, since it is the default state", jobOfferId);
                throw new IllegalArgumentException("Cannot update job offer availability to ACTIVE");
            case CLOSED:
                this.closeJobOffer(jobOffer);
                LOGGER.debug("Job offer with id {} was closed", jobOfferId);
                break;
            case CANCELLED:
                this.cancelJobOffer(jobOffer);
                LOGGER.debug("Job offer with id {} was cancelled", jobOfferId);
                break;
        }
    }
}
