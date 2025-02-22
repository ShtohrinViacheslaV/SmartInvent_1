package com.smartinvent.controller;

import com.smartinvent.models.Printout;
import com.smartinvent.service.PrintoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/printouts")
public class PrintoutController {

    @Autowired
    private PrintoutService printoutService;

    // Отримання всіх звітів
    @GetMapping
    public ResponseEntity<List<Printout>> getAllPrintouts() {
        List<Printout> printouts = printoutService.getAllPrintouts();
        return ResponseEntity.ok(printouts);
    }

    // Додавання нового звіту
    @PostMapping
    public ResponseEntity<Printout> createPrintout(@RequestBody Printout printout) {
        Printout createdPrintout = printoutService.createPrintout(printout);
        return ResponseEntity.ok(createdPrintout);
    }

    // Видалення звіту
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrintout(@PathVariable Long id) {
        printoutService.deletePrintout(id);
        return ResponseEntity.noContent().build();
    }
}
