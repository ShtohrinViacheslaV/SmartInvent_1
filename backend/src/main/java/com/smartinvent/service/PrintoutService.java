package com.smartinvent.service;

import com.smartinvent.models.Printout;
import com.smartinvent.repositories.PrintoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrintoutService {

    @Autowired
    private PrintoutRepository printoutRepository;

    public Printout createPrintout(Printout printout) {
        return printoutRepository.save(printout);
    }

    public List<Printout> getAllPrintouts() {
        return printoutRepository.findAll();
    }

    public Printout getPrintoutById(Long id) {
        return printoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Printout not found with id: " + id));
    }

    public void deletePrintout(Long id) {
        final Printout printout = printoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Printout not found with id: " + id));
        printoutRepository.delete(printout);
    }
}
