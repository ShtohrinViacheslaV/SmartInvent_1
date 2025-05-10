package com.smartinvent.repositories;


import com.smartinvent.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    boolean existsByQrCode(String qrCode);
    Optional<Product> findByProductWorkId(String productWorkId);
//    List<Product> findByIsActiveTrue();
    boolean existsByProductWorkId(String productWorkId);

}
