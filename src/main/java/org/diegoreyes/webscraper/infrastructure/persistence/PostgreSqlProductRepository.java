package org.diegoreyes.webscraper.infrastructure.persistence;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.infrastructure.persistence.entity.ProductJpaEntity;
import org.diegoreyes.webscraper.infrastructure.persistence.mapper.ProductJpaMapper;
import org.diegoreyes.webscraper.infrastructure.persistence.repository.SpringDataProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Transactional
public class PostgreSqlProductRepository implements ProductRepository {

    private final SpringDataProductRepository springDataRepository;
    private final ProductJpaMapper mapper;

    public PostgreSqlProductRepository(
            SpringDataProductRepository springDataRepository,
            ProductJpaMapper mapper
    ) {
        this.springDataRepository = Objects.requireNonNull(
                springDataRepository,
                "SpringDataProductRepository must not be null"
        );
        this.mapper = Objects.requireNonNull(
                mapper,
                "ProductJpaMapper must not be null"
        );
    }

    @Override
    public void save(Product product) {
        Objects.requireNonNull(product, "Product must not be null");
        ProductJpaEntity entity = mapper.toEntity(product);
        springDataRepository.save(entity);
    }

    @Override
    public void saveAll(List<Product> products) {
        Objects.requireNonNull(products, "Products list must not be null");
        List<ProductJpaEntity> entities = new ArrayList<>();
        for (Product product : products) {
            Objects.requireNonNull(product, "Product must not be null");
            entities.add(mapper.toEntity(product));
        }

        springDataRepository.saveAll(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(ProductId id) {
        Objects.requireNonNull(id, "ProductId must not be null");
        return springDataRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ProductId id) {
        Objects.requireNonNull(id, "ProductId must not be null");
        return springDataRepository.existsById(id.value());
    }

    @Override
    public void deleteById(ProductId id) {
        Objects.requireNonNull(id, "ProductId must not be null");
        springDataRepository.deleteById(id.value());
    }

    @Override
    public void clear() {
        springDataRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public int count() {
        return (int) springDataRepository.count();
    }
}
