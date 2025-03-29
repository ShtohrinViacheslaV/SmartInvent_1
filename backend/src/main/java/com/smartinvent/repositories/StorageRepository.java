package com.smartinvent.repositories;


import com.smartinvent.models.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * StorageRepository interface
 */
@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
}
