package com.smartinvent.controller;

import com.smartinvent.models.Storage;
import com.smartinvent.service.StorageService;
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
@RequestMapping("/api/storages")
public class StorageController {

    @Autowired
    private StorageService storageService;

    // Отримання всіх складів
    @GetMapping("/all")
    public ResponseEntity<List<Storage>> getAllStorages() {
        final List<Storage> storages = storageService.getAllStorages();
        return ResponseEntity.ok(storages);
    }

    // Додавання нового складу
    @PostMapping("/create")
    public ResponseEntity<Storage> createStorage(@RequestBody Storage storage) {
        final Storage createdStorage = storageService.createStorage(storage);
        return ResponseEntity.ok(createdStorage);
    }

    // Оновлення складу
    @PostMapping("/update/{id}")
    public ResponseEntity<Storage> updateStorage(@PathVariable Long id, @RequestBody Storage storage) {
        final Storage updatedStorage = storageService.updateStorage(id, storage);
        return ResponseEntity.ok(updatedStorage);
    }

    // Видалення складу
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }
}
