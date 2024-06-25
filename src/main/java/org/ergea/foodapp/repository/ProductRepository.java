package org.ergea.foodapp.repository;

import org.ergea.foodapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE p.price < :priceThreshold AND p.deleted_date IS NULL")
    List<Product> findPromotionalProducts(Double priceThreshold);
}
