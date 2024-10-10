package vn.jully.website_selling_technology_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.jully.website_selling_technology_backend.services.product.IProductRedisService;

@AllArgsConstructor
public class ProductListener {
    private final IProductRedisService productRedisService;

    private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);

    @PrePersist
    public void prePersist (Product product) {
        logger.info("prePersist");
    }

    @PostPersist // save = persis
    public void postPersist (Product product) {
        // Update Redis cache
        logger.info("postPersist");
        productRedisService.clear();
    }

    @PreUpdate
    public void preUpdate (Product product) {
        // ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate (Product product) {
        // Update Redis cache
        logger.info("postUpdate");
        productRedisService.clear();
    }

    @PreRemove
    public void preRemove (Product product) {
        // ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preRemove");
    }

    @PostRemove
    public void postRemove (Product product) {
        // Update Redis caches
        logger.info("postRemove");
        productRedisService.clear();
    }
}