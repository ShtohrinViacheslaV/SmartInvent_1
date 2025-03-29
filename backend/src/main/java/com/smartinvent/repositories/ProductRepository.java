package com.smartinvent.repositories;


import com.smartinvent.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ProductRepository interface
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
