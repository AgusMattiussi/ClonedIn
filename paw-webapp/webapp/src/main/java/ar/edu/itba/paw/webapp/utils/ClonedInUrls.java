package ar.edu.itba.paw.webapp.utils;

public final class ClonedInUrls {

    //TODO: Actualizar para deployar
    //public static final String BASE_URL = "paw-2022b-4/api/";
    public static final String BASE_URL = "api/";
    public static final String FULL_BASE_URL = "http://localhost:8080/";
    public static final String DOMAIN = "localhost";

    // Resource base URLs
    public static final String CATEGORIES_URL = BASE_URL + "categories";
    public static final String JOB_OFFERS_URL = BASE_URL + "jobOffers";
    public static final String USERS_URL = BASE_URL + "users";
    public static final String ENTERPRISES_URL = BASE_URL + "enterprises";
    public static final String SKILLS_URL = BASE_URL + "skills";
    public static final String AUTH_URL = BASE_URL + "auth";

    // Subdirectories
    public static final String EDUCATIONS_SUBDIRECTORY = "educations";
    public static final String EXPERIENCES_SUBDIRECTORY = "experiences";
    public static final String IMAGE_SUBDIRECTORY = "image";
    public static final String CONTACTS_SUBDIRECTORY = "contacts";
    public static final String JOB_OFFERS_SUBDIRECTORY = "jobOffers";
    public static final String SKILLS_SUBDIRECTORY = "skills";
    public static final String APPLICATIONS_SUBDIRECTORY = "applications";
    public static final String NOTIFICATIONS_SUBDIRECTORY = "notifications";

    // URL parameters
    public static final String SKILL_DESCRIPTION_PARAM = "skillDescription";

    // Headers
    public static final String ACCESS_TOKEN_HEADER = "X-Access-Token";
    public static final String TOTAL_PAGES_HEADER = "X-Total-Pages";

    // Cookies
    public static final String REFRESH_TOKEN_COOKIE = "ClonedInRefreshToken";

    private ClonedInUrls() {}
}
