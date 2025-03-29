package com.smartinvent.controller;

import com.smartinvent.models.Storage;
import com.smartinvent.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Клас-контроллер для обробки запитів, пов'язаних зі складами
 */
@RestController
@RequestMapping("/api/storages")
public class StorageController {

    /**
     * Об'єкт сервісу для роботи зі складами
     */
    @Autowired
    private StorageService storageService;

    /**
     * Метод для отримання всіх складів
     *
     * @return - список складів
     */
    @GetMapping
    public ResponseEntity<List<Storage>> getAllStorages() {
        List<Storage> storages = storageService.getAllStorages();
        return ResponseEntity.ok(storages);
    }

    /**
     * Метод для створення нового складу
     *
     * @param storage - об'єкт складу
     * @return - створений склад
     */
    @PostMapping
    public ResponseEntity<Storage> createStorage(@RequestBody Storage storage) {
        Storage createdStorage = storageService.createStorage(storage);
        return ResponseEntity.ok(createdStorage);
    }

    /**
     * Метод для оновлення інформації про склад
     *
     * @param id      - ідентифікатор складу
     * @param storage - об'єкт складу
     * @return - оновлений склад
     */
    @PutMapping("/{id}")
    public ResponseEntity<Storage> updateStorage(@PathVariable Long id, @RequestBody Storage storage) {
        Storage updatedStorage = storageService.updateStorage(id, storage);
        return ResponseEntity.ok(updatedStorage);
    }

    /**
     * Метод для видалення складу
     *
     * @param id - ідентифікатор складу
     * @return - видалений склад з бази даних
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorage(@PathVariable Long id) {
        storageService.deleteStorage(id);
        return ResponseEntity.noContent().build();
    }
}
