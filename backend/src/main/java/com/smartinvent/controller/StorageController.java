package com.smartinvent.controller;

import com.smartinvent.models.Storage;
import com.smartinvent.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storages")
public class StorageController {

    @Autowired
    private StorageService storageService;

    // Отримання всіх складів
    @GetMapping
    public ResponseEntity<List<Storage>> getAllStorages() {
        List<Storage> storages = storageService.getAllStorages();
        return ResponseEntity.ok(storages);
    }

    // Додавання нового складу
    @PostMapping
    public ResponseEntity<Storage> createStorage(@RequestBody Storage storage) {
        Storage createdStorage = storageService.createStorage(storage);
        return ResponseEntity.ok(createdStorage);
    }

    // Оновлення складу
    @PutMapping("/{id}")
    public ResponseEntity<Storage> updateStorage(@PathVariable Long id, @RequestBody Storage storage) {
        Storage updatedStorage = storageService.updateStorage(id, storage);
        return ResponseEntity.ok(updatedStorage);
    }

    // Видалення складу
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }
}
