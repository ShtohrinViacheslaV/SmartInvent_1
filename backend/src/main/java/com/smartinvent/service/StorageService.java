package com.smartinvent.service;

import com.smartinvent.models.Storage;
import com.smartinvent.repositories.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * StorageService клас для роботи з Storage об'єктами.
 */
@Service
public class StorageService {

    /**
     * StorageRepository об'єкт для роботи з Storage об'єктами.
     *
     * @see com.smartinvent.repositories.StorageRepository
     */
    @Autowired
    private StorageRepository storageRepository;

    /**
     * Метод createStorage для створення нового Storage об'єкта.
     *
     * @param storage Storage об'єкт
     * @return створений Storage об'єкт
     */
    public Storage createStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    /**
     * Метод updateStorage для оновлення інформації про Storage об'єкт.
     *
     * @param id      id Storage об'єкта
     * @param storage Storage об'єкт
     * @return оновлений Storage об'єкт
     */
    public Storage updateStorage(Long id, Storage storage) {
        Storage existingStorage = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found with id: " + id));
        existingStorage.setName(storage.getName());
        existingStorage.setLocation(storage.getLocation());
        existingStorage.setDetails(storage.getDetails());
        return storageRepository.save(existingStorage);
    }

    /**
     * Метод deleteStorage для видалення Storage об'єкта за його id.
     *
     * @param id id Storage об'єкта
     */
    public void deleteStorage(Long id) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found with id: " + id));
        storageRepository.delete(storage);
    }

    /**
     * Метод getAllStorages для отримання всіх Storage об'єктів.
     *
     * @return список Storage об'єктів
     */
    public List<Storage> getAllStorages() {
        return storageRepository.findAll();
    }

    /**
     * getStorageById метод для отримання Storage об'єкта за його id.
     *
     * @param id id Storage об'єкта
     * @return Storage об'єкт
     */
    public Storage getStorageById(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found with id: " + id));
    }
}
