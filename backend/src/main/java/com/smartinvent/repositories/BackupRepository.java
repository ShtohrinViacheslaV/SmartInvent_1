package com.smartinvent.repositories;


import com.smartinvent.models.Backup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {
}
