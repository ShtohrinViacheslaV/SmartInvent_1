package com.smartinvent.repositories;


import com.smartinvent.models.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Long> {
}
