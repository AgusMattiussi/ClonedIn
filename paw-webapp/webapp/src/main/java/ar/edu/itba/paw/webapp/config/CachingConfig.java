package ar.edu.itba.paw.webapp.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/*
* We tried to make use of caching in our application, but we were not able to make it work properly
* because of the many LazyInitializationExceptions that we were getting.
*/

//@Configuration
//@EnableCaching
public class CachingConfig {

    //@Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("users-cache"),
                new ConcurrentMapCache("enterprises-cache"),
                new ConcurrentMapCache("images-cache"),
                new ConcurrentMapCache("educations-cache"),
                new ConcurrentMapCache("experiences-cache"),
                new ConcurrentMapCache("jobOffers-cache"),
                new ConcurrentMapCache("skills-cache"),
                new ConcurrentMapCache("categories-cache")));
        return cacheManager;
    }
}
