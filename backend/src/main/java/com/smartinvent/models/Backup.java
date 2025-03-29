package com.smartinvent.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Клас-сутність для зберігання інформації про резервні копії
 */
@Entity
@Table(name = "backup")
public class Backup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backupId;

    private String type;
    private LocalDateTime date;


    public Long getBackupId() {
        return backupId;
    }

    public void setBackupId(Long backupId) {
        this.backupId = backupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
