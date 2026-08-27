package org.diegoreyes.webscraper.infrastructure.persistence.repository;

import org.diegoreyes.webscraper.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, String> {

    List<ProductJpaEntity> findByNameContainingIgnoreCase(String name);

    List<ProductJpaEntity> findByStoreIgnoreCase(String store);
}
