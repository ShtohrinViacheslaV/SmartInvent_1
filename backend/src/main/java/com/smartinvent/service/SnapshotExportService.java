package com.smartinvent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartinvent.dto.InventoryProductResultDto;
import com.smartinvent.dto.ProductSnapshotDto;
import com.smartinvent.models.*;
import com.smartinvent.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotExportService {

    private final InventoryResultService resultService;
    private final ProductRepository productRepository;
    private final InventoryResultRepository resultRepository;
    private final CategoryRepository categoryRepository;
    private final StorageRepository storageRepository;
    private final InventorySessionRepository sessionRepository;
    private final ObjectMapper objectMapper; // ✅ ІН'ЄКЦІЯ з конфігурації Spring

    public void exportProductSnapshot(InventorySession session) {
        List<Product> products = productRepository.findAll();

        List<ProductSnapshotDto.ProductData> productDataList = products.stream().map(product ->
                ProductSnapshotDto.ProductData.builder()
                        .id(product.getProductId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .manufacturer(product.getManufacturer())
                        .expirationDate(product.getExpirationDate())
                        .weight(product.getWeight())
                        .productWorkId(product.getProductWorkId())
                        .dimensions(product.getDimensions())
                        .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                        .storageId(product.getStorage() != null ? product.getStorage().getStorageId() : null)
                        .build()
        ).toList();

        ProductSnapshotDto snapshot = ProductSnapshotDto.builder()
                .sessionId(session.getInventorySessionId())
                .snapshotTime(LocalDateTime.now())
                .products(productDataList)
                .build();

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = String.format("snapshots/session_%d_start_snapshot_%s.json",
                    session.getInventorySessionId(), timestamp);
            File dir = new File("snapshots");
            if (!dir.exists()) {
                dir.mkdir();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), snapshot);
            System.out.println("✅ Знімок збережено до " + fileName);
        } catch (IOException e) {
            System.err.println("❌ Помилка при збереженні знімка: " + e.getMessage());
        }
    }



    //   === BACKUP FINISH INVENTORY===

    @Transactional
    public void completeSessionAndBackup(Long sessionId) {
        InventorySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Сесію не знайдено"));

        // 1. Бекап продуктів
        exportProductSnapshot(session);

        // 2. Бекап DTO результатів інвентаризації


// ✅ Стало
        List<InventoryProductResultDto> results = resultService.getResultsBySession(sessionId);



        // 3. Очистити всі продукти
        productRepository.deleteAll();

        // 4. Відновити продукти з DTO, якщо їх статус != NOT_CHECKED
        List<Product> newProducts = results.stream()
                .filter(result -> result.getStatus() != InventoryProductStatusEnum.NOT_FOUND)
                .map(this::convertDtoToProduct)
                .toList();

        productRepository.saveAll(newProducts);

        // 5. Очистити DTO
        resultRepository.deleteAllBySessionInventorySessionId(sessionId);

        // 6. Завершити сесію
        session.setStatus(InventorySessionStatusEnum.COMPLETED);
        sessionRepository.save(session);
    }

    private void backupResultDtoToFile(List<InventoryProductResultDto> results, InventorySession session) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = String.format("snapshots/session_%d_result_snapshot_%s.json",
                session.getInventorySessionId(), timestamp);

        File dir = new File("snapshots");
        if (!dir.exists()) dir.mkdir();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), results);
            System.out.println("✅ DTO результат збережено до " + fileName);
        } catch (IOException e) {
            System.err.println("❌ Помилка при збереженні DTO: " + e.getMessage());
        }
    }

    private Product convertDtoToProduct(InventoryProductResultDto dto) {
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setManufacturer(dto.getManufacturer());
        product.setExpirationDate(dto.getExpirationDate());
        product.setWeight(dto.getWeight());
        product.setProductWorkId(dto.getProductWorkId());
        product.setDimensions(dto.getDimensions());
        // потрібно завантажити категорію та склад через репозиторії
        product.setCategory(categoryRepository.findByName(dto.getCategoryName()).orElse(null));
        product.setStorage(storageRepository.findByName(dto.getStorageName()).orElse(null));
        return product;
    }










}
