package com.smartinvent.controller;

import com.smartinvent.models.Printout;
import com.smartinvent.service.PrintoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Клас-контроллер для обробки запитів, пов'язаних з вивідками
 */
@RestController
@RequestMapping("/api/printouts")
public class PrintoutController {

    /**
     * Об'єкт сервісу для роботи з вивідками
     */
    @Autowired
    private PrintoutService printoutService;

    /**
     * Метод для отримання всіх звітів в базі даних
     *
     * @return - список звітів в базі даних
     */
    @GetMapping
    public ResponseEntity<List<Printout>> getAllPrintouts() {
        List<Printout> printouts = printoutService.getAllPrintouts();
        return ResponseEntity.ok(printouts);
    }

    /**
     * Метод для створення нового звіту
     *
     * @param printout - об'єкт вивідку
     * @return - створений звіт
     */
    @PostMapping
    public ResponseEntity<Printout> createPrintout(@RequestBody Printout printout) {
        Printout createdPrintout = printoutService.createPrintout(printout);
        return ResponseEntity.ok(createdPrintout);
    }

    /**
     * Метод deletePrintout для видалення звіту
     *
     * @param id - ідентифікатор звіту
     * @return - видалений звіт з бази даних
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrintout(@PathVariable Long id) {
        printoutService.deletePrintout(id);
        return ResponseEntity.noContent().build();
    }
}
