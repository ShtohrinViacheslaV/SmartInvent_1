package com.smartinvent.controller;

import com.smartinvent.models.InventoryScan;
import com.smartinvent.service.InventoryScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/inventory")
public class InventoryScanController {

    private final InventoryScanService inventoryScanService;

    public InventoryScanController(InventoryScanService inventoryScanService) {
        this.inventoryScanService = inventoryScanService;
    }

    @PostMapping("/scan")
    public ResponseEntity<InventoryScan> saveScan(@RequestBody InventoryScan scan) {
        return ResponseEntity.ok(inventoryScanService.saveScan(scan));
    }

    @GetMapping("/scan/{productId}")
    public ResponseEntity<List<InventoryScan>> getScansByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryScanService.getScansByProduct(productId));
    }
}
