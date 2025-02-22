//package com.smartinvent.controller;
//
//import com.smartinvent.models.DataChangeLog;
//import com.smartinvent.service.DataChangeLogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/dataChangeLogs")
//public class DataChangeLogController {
//
//    @Autowired
//    private DataChangeLogService dataChangeLogService;
//
//    // Отримання всіх логів змін даних
//    @GetMapping
//    public ResponseEntity<List<DataChangeLog>> getAllDataChangeLogs() {
//        List<DataChangeLog> logs = dataChangeLogService.getAllDataChangeLogs();
//        return ResponseEntity.ok(logs);
//    }
//
//    // Додавання нового логу змін
//    @PostMapping
//    public ResponseEntity<DataChangeLog> createDataChangeLog(@RequestBody DataChangeLog log) {
//        DataChangeLog createdLog = dataChangeLogService.createDataChangeLog(log);
//        return ResponseEntity.ok(createdLog);
//    }
//
//    // Видалення логу змін
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteDataChangeLog(@PathVariable Long id) {
//        dataChangeLogService.deleteDataChangeLog(id);
//        return ResponseEntity.noContent().build();
//    }
//}
