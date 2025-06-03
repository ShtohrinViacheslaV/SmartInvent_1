//package com.smartinvent.controller;
//
//import com.smartinvent.models.TransactionTypeEnum;
//import com.smartinvent.service.TransactionTypeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/transaction-types")
//public class TransactionTypeController {
//
//    @Autowired
//    private TransactionTypeService transactionTypeService;
//
//    @GetMapping("/all")
//    public ResponseEntity<List<TransactionTypeEnum>> getAllTypes() {
//        return ResponseEntity.ok(transactionTypeService.getAllTransactionTypes());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<TransactionTypeEnum> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(transactionTypeService.getTransactionTypeById(id));
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<TransactionTypeEnum> createType(@RequestBody TransactionTypeEnum type) {
//        return ResponseEntity.ok(transactionTypeService.createTransactionType(type));
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<TransactionTypeEnum> updateType(@PathVariable Long id, @RequestBody TransactionTypeEnum type) {
//        return ResponseEntity.ok(transactionTypeService.updateTransactionType(id, type));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
//        transactionTypeService.deleteTransactionType(id);
//        return ResponseEntity.noContent().build();
//    }
//}
