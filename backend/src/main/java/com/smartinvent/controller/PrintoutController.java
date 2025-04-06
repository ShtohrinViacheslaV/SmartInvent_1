package com.smartinvent.controller;

import com.smartinvent.models.Printout;
import com.smartinvent.service.PrintoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api/printouts")
public class PrintoutController {

    @Autowired
    private PrintoutService printoutService;

    // Отримання всіх звітів
    @GetMapping
    public ResponseEntity<List<Printout>> getAllPrintouts() {
        final List<Printout> printouts = printoutService.getAllPrintouts();
        return ResponseEntity.ok(printouts);
    }

    // Додавання нового звіту
    @PostMapping
    public ResponseEntity<Printout> createPrintout(@RequestBody Printout printout) {
        final Printout createdPrintout = printoutService.createPrintout(printout);
        return ResponseEntity.ok(createdPrintout);
    }

    // Видалення звіту
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrintout(@PathVariable Long id) {
        printoutService.deletePrintout(id);
        return ResponseEntity.noContent().build();
    }
}
