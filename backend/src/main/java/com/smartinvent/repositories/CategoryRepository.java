package com.smartinvent.repositories;


import com.smartinvent.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CategoryRepository interface
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
