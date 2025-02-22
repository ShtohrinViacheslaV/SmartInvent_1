//package com.smartinvent.controller;
//
//import com.smartinvent.models.StorageArea;
//import com.smartinvent.service.StorageAreaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/storageAreas")
//public class StorageAreaController {
//
//    @Autowired
//    private StorageAreaService storageAreaService;
//
//    // Отримання всіх зон зберігання
//    @GetMapping
//    public ResponseEntity<List<StorageArea>> getAllStorageAreas() {
//        List<StorageArea> storageAreas = storageAreaService.getAllStorageAreas();
//        return ResponseEntity.ok(storageAreas);
//    }
//
//    // Додавання нової зони зберігання
//    @PostMapping
//    public ResponseEntity<StorageArea> createStorageArea(@RequestBody StorageArea storageArea) {
//        StorageArea createdStorageArea = storageAreaService.createStorageArea(storageArea);
//        return ResponseEntity.ok(createdStorageArea);
//    }
//
//    // Оновлення зони зберігання
//    @PutMapping("/{id}")
//    public ResponseEntity<StorageArea> updateStorageArea(@PathVariable Long id, @RequestBody StorageArea storageArea) {
//        StorageArea updatedStorageArea = storageAreaService.updateStorageArea(id, storageArea);
//        return ResponseEntity.ok(updatedStorageArea);
//    }
//
//    // Видалення зони зберігання
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteStorageArea(@PathVariable Long id) {
//        storageAreaService.deleteStorageArea(id);
//        return ResponseEntity.noContent().build();
//    }
//}
