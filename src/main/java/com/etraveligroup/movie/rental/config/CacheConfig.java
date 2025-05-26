package com.etraveligroup.movie.rental.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configures a ConcurrentMapCacheManager for caching invoices.
     * This cache manager will store invoices in memory,
     * @return
     */
    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        return new ConcurrentMapCacheManager("invoices");
    }

}
