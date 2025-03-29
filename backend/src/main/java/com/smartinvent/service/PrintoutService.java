package com.smartinvent.service;

import com.smartinvent.models.Printout;
import com.smartinvent.repositories.PrintoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PrintoutService клас для роботи з Printout об'єктами.
 */
@Service
public class PrintoutService {

    /**
     * PrintoutRepository об'єкт для роботи з Printout об'єктами.
     *
     * @see com.smartinvent.repositories.PrintoutRepository
     */
    @Autowired
    private PrintoutRepository printoutRepository;

    /**
     * Метод createPrintout для створення нового Printout об'єкта.
     *
     * @param printout Printout об'єкт
     * @return створений Printout об'єкт
     */
    public Printout createPrintout(Printout printout) {
        return printoutRepository.save(printout);
    }

    /**
     * Метод getAllPrintouts для отримання всіх Printout об'єктів.
     *
     * @return список Printout об'єктів
     */
    public List<Printout> getAllPrintouts() {
        return printoutRepository.findAll();
    }

    /**
     * getPrintoutById метод для отримання Printout об'єкта за його id.
     *
     * @param id id Printout об'єкта
     * @return Printout об'єкт
     */
    public Printout getPrintoutById(Long id) {
        return printoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Printout not found with id: " + id));
    }

    /**
     * deletePrintout метод для видалення Printout об'єкта за його id.
     *
     * @param id id Printout об'єкта
     */
    public void deletePrintout(Long id) {
        Printout printout = printoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Printout not found with id: " + id));
        printoutRepository.delete(printout);
    }
}
