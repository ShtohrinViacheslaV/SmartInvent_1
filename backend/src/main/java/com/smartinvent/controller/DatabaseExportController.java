//package com.smartinvent.controller;
//
//import com.smartinvent.service.DatabaseExportService;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//@RestController
//@RequestMapping("/api/database")
//public class DatabaseExportController {
//    private final DatabaseExportService databaseExportService;
//
//    public DatabaseExportController(DatabaseExportService databaseExportService) {
//        this.databaseExportService = databaseExportService;
//    }
//
//    @GetMapping("/export")
//    public ResponseEntity<byte[]> exportDatabase() {
//        try {
//            File dbFile = databaseExportService.createSQLiteCopy();
//            byte[] fileContent = Files.readAllBytes(Path.of(dbFile.getAbsolutePath()));
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory.sqlite");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(fileContent);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
