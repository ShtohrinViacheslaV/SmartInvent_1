//package com.smartinvent.service;
//
//import com.smartinvent.models.TransactionTypeEnum;
//import com.smartinvent.repositories.TransactionTypeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TransactionTypeService {
//
//    @Autowired
//    private TransactionTypeRepository transactionTypeRepository;
//
//    public List<TransactionTypeEnum> getAllTransactionTypes() {
//        return transactionTypeRepository.findAll();
//    }
//
//    public TransactionTypeEnum getTransactionTypeById(Long id) {
//        return transactionTypeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Transaction type not found with id: " + id));
//    }
//
//    public TransactionTypeEnum createTransactionType(TransactionTypeEnum type) {
//        return transactionTypeRepository.save(type);
//    }
//
//    public TransactionTypeEnum updateTransactionType(Long id, TransactionTypeEnum updatedType) {
//        TransactionTypeEnum existingType = transactionTypeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Transaction type not found with id: " + id));
//
//        existingType.setName(updatedType.getName());
//        existingType.setDescription(updatedType.getDescription());
//
//        return transactionTypeRepository.save(existingType);
//    }
//
//    public void deleteTransactionType(Long id) {
//        TransactionTypeEnum type = transactionTypeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Transaction type not found with id: " + id));
//        transactionTypeRepository.delete(type);
//    }
//}
