package com.smartinvent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartinvent.dto.ProductSnapshotDto;
import com.smartinvent.models.InventorySession;
import com.smartinvent.models.Product;
import com.smartinvent.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotExportService {

    private final ProductRepository productRepository;
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
}
