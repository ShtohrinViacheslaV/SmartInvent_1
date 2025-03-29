package com.smartinvent.repositories;


import com.smartinvent.models.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BackupRepository interface
 */
@Repository
public interface BackupRepository extends JpaRepository<Backup, Long> {
}
