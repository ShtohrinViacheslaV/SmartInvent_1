package com.smartinvent.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Клас Printout відображає сутність "Видача" в базі даних
 */
@Entity
@Table(name = "printout")
public class Printout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long printoutId;

    private String type;
    private LocalDateTime date;


    public Long getPrintoutId() {
        return printoutId;
    }

    public void setPrintoutId(Long printoutId) {
        this.printoutId = printoutId;
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
