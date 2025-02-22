//package com.smartinvent.service;
//
//import com.smartinvent.models.StorageArea;
//import com.smartinvent.repositories.StorageAreaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class StorageAreaService {
//
//    @Autowired
//    private StorageAreaRepository storageAreaRepository;
//
//    public StorageArea createStorageArea(StorageArea storageArea) {
//        return storageAreaRepository.save(storageArea);
//    }
//
//    public StorageArea updateStorageArea(Long id, StorageArea storageArea) {
//        StorageArea existingStorageArea = storageAreaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Storage Area not found with id: " + id));
//        existingStorageArea.setName(storageArea.getName());
//        existingStorageArea.setStorage(existingStorageArea.getStorage()); // якщо є зв'язок з Storage
//        return storageAreaRepository.save(existingStorageArea);
//    }
//
//    public void deleteStorageArea(Long id) {
//        StorageArea storageArea = storageAreaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Storage Area not found with id: " + id));
//        storageAreaRepository.delete(storageArea);
//    }
//
//    public List<StorageArea> getAllStorageAreas() {
//        return storageAreaRepository.findAll();
//    }
//
//    public StorageArea getStorageAreaById(Long id) {
//        return storageAreaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Storage Area not found with id: " + id));
//    }
//}
