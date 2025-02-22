package com.smartinvent.service;

import com.smartinvent.models.InventoryScan;
import com.smartinvent.repositories.InventoryScanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryScanService {

    private final InventoryScanRepository inventoryScanRepository;

    public InventoryScanService(InventoryScanRepository inventoryScanRepository) {
        this.inventoryScanRepository = inventoryScanRepository;
    }

    public InventoryScan saveScan(InventoryScan scan) {
        return inventoryScanRepository.save(scan);
    }

    public List<InventoryScan> getScansByProduct(Long productId) {
        return inventoryScanRepository.findByProductId(productId);
    }
}
