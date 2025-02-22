package com.smartinvent.service;

import com.smartinvent.models.Storage;
import com.smartinvent.repositories.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    public Storage createStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    public Storage updateStorage(Long id, Storage storage) {
        Storage existingStorage = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found with id: " + id));
        existingStorage.setName(storage.getName());
        existingStorage.setLocation(storage.getLocation());
        existingStorage.setDetails(storage.getDetails());
        return storageRepository.save(existingStorage);
    }

    public void deleteStorage(Long id) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found with id: " + id));
        storageRepository.delete(storage);
    }

    public List<Storage> getAllStorages() {
        return storageRepository.findAll();
    }

    public Storage getStorageById(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found with id: " + id));
    }
}
