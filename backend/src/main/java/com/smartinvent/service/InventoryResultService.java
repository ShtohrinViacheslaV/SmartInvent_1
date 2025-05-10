package com.smartinvent.service;


import com.smartinvent.dto.CreateProductDuringInventoryRequest;
import com.smartinvent.dto.InventorySessionProductDTO;
import com.smartinvent.models.*;
import com.smartinvent.repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class InventoryResultService {

    private final InventoryResultRepository resultRepository;
    private final InventoryProductStatusRepository statusRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final StorageRepository storageRepository;
    private final InventorySessionRepository sessionRepository;

    // Отримання результатів інвентаризації для конкретної сесії
    public List<InventorySessionProductDTO> getResultsForSession(Long sessionId) {
        // Отримуємо сесію
        InventorySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

//        InventorySession session = new InventorySession();
//        session.setId(sessionId);

        // Отримуємо всі результати інвентаризації для цієї сесії
        List<InventoryResult> results = resultRepository.findBySession(session);

        // Перетворюємо результати в DTO
        List<InventorySessionProductDTO> dtoList = results.stream().map(result -> {
            InventorySessionProductDTO dto = new InventorySessionProductDTO();
            dto.setProductId(result.getProduct().getProductId());
            dto.setName(result.getProduct().getName());
            dto.setDescription(result.getDescription());
            dto.setProductWorkId(result.getProduct().getProductWorkId());
            dto.setCategoryName(result.getProduct().getCategory().getName());
            dto.setStorageName(result.getProduct().getStorage().getName());
            dto.setStatus(result.getStatus().getName());
            dto.setLocked(false); // Заблоковано чи ні, буде визначатися на фронтенді

            return dto;
        }).collect(Collectors.toList());

        return dtoList;
    }

    // Додавання результату інвентаризації для товару в сесії
    public InventoryResult addInventoryResult(InventoryResult inventoryResult) {
        Long sessionId = inventoryResult.getSession().getId();
        Long productId = inventoryResult.getProduct().getProductId();

        // Перевірка чи вже є результат
        Optional<InventoryResult> existing = resultRepository.findBySessionIdAndProductProductId(sessionId, productId);
        if (existing.isPresent()) {
            throw new RuntimeException("Inventory result already exists for this product in the session.");
        }

        // Знайдемо статус товару
        InventoryProductStatus status = statusRepository.findByName(inventoryResult.getStatus().getName())
                .orElseThrow(() -> new RuntimeException("Product status not found"));

        inventoryResult.setStatus(status);
        inventoryResult.setScanTime(LocalDateTime.now());

        return resultRepository.save(inventoryResult);
    }



    // Оновлення результату інвентаризації для товару в сесії
    public InventoryResult updateInventoryResult(Long resultId, InventoryResult inventoryResult) {
        InventoryResult existingResult = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Inventory result not found"));

        InventoryProductStatus status = statusRepository.findByName(inventoryResult.getStatus().getName())
                .orElseThrow(() -> new RuntimeException("Status not found"));
        existingResult.setStatus(status);
        existingResult.setDescription(inventoryResult.getDescription());
        existingResult.setScannedBy(inventoryResult.getScannedBy());
        existingResult.setScanTime(LocalDateTime.now());

        return resultRepository.save(existingResult);
    }


    // Отримання товарів для сесії з перевіркою блокування
    public List<InventorySessionProductDTO> getProductsForSession(Long sessionId) {
//        InventorySession session = new InventorySession();
//        session.setId(sessionId);
        InventorySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));


        // Отримуємо всі результати інвентаризації для цієї сесії
        List<InventoryResult> results = resultRepository.findBySession(session);

        // Перетворюємо результати в DTO
        return results.stream().map(result -> {
            InventorySessionProductDTO dto = new InventorySessionProductDTO();
            dto.setProductId(result.getProduct().getProductId());
            dto.setName(result.getProduct().getName());
            dto.setDescription(result.getDescription());
            dto.setProductWorkId(result.getProduct().getProductWorkId());
            dto.setCategoryName(result.getProduct().getCategory().getName());
            dto.setStorageName(result.getProduct().getStorage().getName());
            dto.setStatus(result.getStatus().getName());
            dto.setLocked(false); // Заблоковано чи ні, буде визначатися на фронтенді (можна передавати цю інформацію з сесії)

            return dto;
        }).collect(Collectors.toList());
    }

    // Оновлення статусу товарів, які не перевірені
    public void markUncheckedProductsAsNotFound(Long sessionId) {
//        InventorySession session = new InventorySession();
//        session.setId(sessionId);

        InventorySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));


        // Отримуємо всі результати інвентаризації для цієї сесії
        List<InventoryResult> results = resultRepository.findBySession(session);

        InventoryProductStatus notFoundStatus = statusRepository.findByName("NOT_FOUND")
                .orElseThrow(() -> new RuntimeException("Not found status not found"));


        InventoryProductStatus uncheckedStatus = statusRepository.findByName("UNCHECKED")
                .orElseThrow(() -> new RuntimeException("UNCHECKED status not found"));

        for (InventoryResult result : results) {
            if (result.getStatus().equals(uncheckedStatus)) {
                result.setStatus(notFoundStatus);
                resultRepository.save(result);
            }
        }
    }


    public InventorySessionProductDTO createProductDuringInventory(CreateProductDuringInventoryRequest request) {
        // 1. Отримуємо пов’язані сутності
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категорію не знайдено"));

        Storage storage = storageRepository.findById(request.getStorageId())
                .orElseThrow(() -> new RuntimeException("Склад не знайдено"));

        InventorySession session = sessionRepository.findById(request.getInventorySessionId())
                .orElseThrow(() -> new RuntimeException("Сесію інвентаризації не знайдено"));

        // 2. Отримуємо статус ADDED
        InventoryProductStatus addedStatus = statusRepository.findByName("ADDED")
                .orElseThrow(() -> new RuntimeException("Статус ADDED не знайдено"));

        // 3. Створюємо товар
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        if (productRepository.existsByProductWorkId(request.getProductWorkId())) {
            throw new RuntimeException("Продукт із таким ID уже існує");
        }

        product.setProductWorkId(request.getProductWorkId());
        product.setCategory(category);
        product.setStorage(storage);
        productRepository.save(product);

        // 4. Додаємо запис до InventoryResult зі статусом ADDED
        InventoryResult result = new InventoryResult();
        result.setSession(session);
        result.setProduct(product);
        result.setStatus(addedStatus);
        resultRepository.save(result);

        // 5. Повертаємо DTO для фронтенду
        InventorySessionProductDTO dto = new InventorySessionProductDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setProductWorkId(product.getProductWorkId());
        dto.setCategoryName(category.getName());
        dto.setStorageName(storage.getName());
        dto.setStatus("ADDED"); // у DTO статус зручно лишити як String
        dto.setLocked(false);

        return dto;
    }


    public List<InventorySessionProductDTO> searchProducts(Long sessionId, String query, String criteria, String sortBy) {
        List<InventorySessionProductDTO> allProducts = getProductsForSession(sessionId); // вже реалізована логіка
        return allProducts.stream()
                .filter(p -> {
                    switch (criteria) {
                        case "name":
                            return p.getName().toLowerCase().contains(query.toLowerCase());
                        case "productId":
                            return p.getProductWorkId().toLowerCase().contains(query.toLowerCase());
                        case "category":
                            return p.getCategoryName().toLowerCase().contains(query.toLowerCase());
                        default:
                            return false;
                    }
                })
                .sorted((p1, p2) -> {
                    if ("category".equals(sortBy)) {
                        return p1.getCategoryName().compareToIgnoreCase(p2.getCategoryName());
                    } else if ("storage".equals(sortBy)) {
                        return p1.getStorageName().compareToIgnoreCase(p2.getStorageName());
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }

}








//
//
//@Service
//@RequiredArgsConstructor
//public class InventoryResultService {
//
//    private final InventoryResultRepository resultRepository;
//    private final InventorySessionRepository sessionRepository;
//    private final ProductRepository productRepository;
//    private final EmployeeRepository employeeRepository;
//    private final InventoryProductStatusRepository statusRepository;
//
//    public InventoryResult scanProduct(Long sessionId, Long employeeId, String productWorkId) {
//        InventorySession session = sessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Session not found"));
//
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        Optional<Product> productOpt = productRepository.findByProductWorkId(productWorkId);
//
//        Product product;
//        InventoryProductStatus status;
//
//        if (productOpt.isPresent()) {
//            product = productOpt.get();
//            Optional<InventoryResult> existingResult = resultRepository.findBySessionIdAndProductId(sessionId, product.getProductId());
//
//            if (existingResult.isPresent()) {
//                throw new RuntimeException("Product already scanned in this session");
//            }
//
//            status = statusRepository.findByName("UNVERIFIED")
//                    .orElseThrow(() -> new RuntimeException("Status UNVERIFIED not found"));
//        } else {
//            // Redirect to product creation page logic should be handled in the controller layer
//            throw new RuntimeException("Product not found. Please create a new product.");
//        }
//
//        InventoryResult result = new InventoryResult();
//        result.setInventorySession(session);
//        result.setProduct(product);
//        result.setScannedBy(employee);
//        result.setScanTime(LocalDateTime.now());
//        result.setStatus(status);
//
//        return resultRepository.save(result);
//    }
//
//    public void updateProductStatus(Long sessionId, Long productId, String statusName, String description) {
//        InventoryResult result = resultRepository.findBySessionIdAndProductId(sessionId, productId)
//                .orElseThrow(() -> new RuntimeException("Inventory result not found"));
//
//        InventoryProductStatus status = statusRepository.findByName(statusName)
//                .orElseThrow(() -> new RuntimeException("Status not found"));
//
//        result.setStatus(status);
//        result.setDescription(description);
//        resultRepository.save(result);
//    }
//
//    public void cancelScan(Long sessionId, Long productId) {
//        InventoryResult result = resultRepository.findBySessionIdAndProductId(sessionId, productId)
//                .orElseThrow(() -> new RuntimeException("Inventory result not found"));
//
//        resultRepository.delete(result);
//    }
//
//    public void markUnverifiedAsNotFound(Long sessionId, Long employeeId) {
//        List<InventoryResult> results = resultRepository.findBySessionId(sessionId);
//        InventoryProductStatus notFoundStatus = statusRepository.findByName("NOT_FOUND")
//                .orElseThrow(() -> new RuntimeException("Status NOT_FOUND not found"));
//
//        for (InventoryResult result : results) {
//            if ("UNVERIFIED".equals(result.getStatus().getName())) {
//                result.setStatus(notFoundStatus);
//                result.setDescription("Product not found during inventory");
//                resultRepository.save(result);
//            }
//        }
//    }
//}
//
//










//
//@Service
//@RequiredArgsConstructor
//public class InventoryResultService {
//
//    private final InventoryResultRepository inventoryResultRepository;
//    private final ProductRepository productRepository;
//    private final EmployeeRepository employeeRepository;
//    private final InventorySessionRepository inventorySessionRepository;
//    private final InventoryProductStatusRepository inventoryProductStatusRepository;
//    private final CategoryRepository categoryRepository;
//    private final StorageRepository storageRepository;
//
//
//
//    public List<InventorySessionProductDTO> getProductsForSession(Long sessionId) {
//        // Отримуємо всі записи InventoryResult для цієї сесії
//        List<InventoryResult> results = inventoryResultRepository.findByInventorySessionId(sessionId);
//
//        // Отримаємо всі productId з них
//        Set<Long> checkedProductIds = results.stream()
//                .map(r -> r.getProduct().getProductId())
//                .collect(Collectors.toSet());
//
//        // 1. Перевірені товари — беремо з InventoryResult + Product
//        List<InventorySessionProductDTO> checkedProducts = results.stream().map(result -> {
//            Product product = result.getProduct();
//            return toDTO(product, result.getStatus().getName(), result.getScannedBy() != null);
//        }).collect(Collectors.toList());
//
//        // 2. Неперевірені товари — ті, яких немає в InventoryResult
//        List<Product> uncheckedProducts = productRepository.findAll().stream()
//                .filter(p -> !checkedProductIds.contains(p.getProductId()))
//                .collect(Collectors.toList());
//
//        List<InventorySessionProductDTO> uncheckedProductDTOs = uncheckedProducts.stream()
//                .map(p -> toDTO(p, "unchecked", false))
//                .collect(Collectors.toList());
//
//        // Об'єднуємо обидва списки
//        List<InventorySessionProductDTO> all = new ArrayList<>();
//        all.addAll(checkedProducts);
//        all.addAll(uncheckedProductDTOs);
//        return all;
//    }
//
//    private InventorySessionProductDTO toDTO(Product product, String status, boolean locked) {
//        InventorySessionProductDTO dto = new InventorySessionProductDTO();
//        dto.setProductId(product.getProductId());
//        dto.setName(product.getName());
//        dto.setDescription(product.getDescription());
//        dto.setProductWorkId(product.getProductWorkId());
//        dto.setCategoryName(product.getCategory().getName());
//        dto.setStorageName(product.getStorage().getName());
//        dto.setStatus(status);
//        dto.setLocked(locked);
////        dto.setManufacturer(product.getManufacturer());
////        dto.setExpirationDate(product.getExpirationDate());
//        return dto;
//    }
//
//
//
//    @Transactional
//    public InventoryResult processScannedOrEnteredProduct(Long sessionId, Product scannedProduct, Long employeeId) {
//        // 1. Знаходимо сесію
//        InventorySession session = inventorySessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Session not found"));
//
//        // 2. Шукаємо товар в базі за унікальним ідентифікатором (наприклад, barcode або QR-кодом)
//        Optional<Product> existingProductOpt = productRepository.findByProductWorkId(scannedProduct.getProductWorkId());
//
//        InventoryProductStatus status;
//        Product productToSave;
//
//        if (existingProductOpt.isPresent()) {
//            Product existingProduct = existingProductOpt.get();
//
//            // 3. Перевіряємо чи вже є в результатах інвентаризації
//            boolean alreadyExists = inventoryResultRepository.existsByInventorySession_InventorySessionIdAndProduct_ProductId(sessionId, existingProduct.getProductId());
//
//            if (alreadyExists) {
//                throw new RuntimeException("Product already scanned in this session");
//            }
//
//            // 4. Перевіряємо чи потрібно ставити статус CONFIRMED або MODIFIED
//            if (isProductDataSame(existingProduct, scannedProduct)) {
//                status = inventoryProductStatusRepository.findByName("CONFIRMED")
//                        .orElseThrow(() -> new RuntimeException("Status CONFIRMED not found"));
//            } else {
//                // Можемо оновити дані продукту, якщо хочемо, або тільки зберігати відмінність у результатах
//                status = inventoryProductStatusRepository.findByName("MODIFIED")
//                        .orElseThrow(() -> new RuntimeException("Status MODIFIED not found"));
//            }
//
//            productToSave = existingProduct;
//        } else {
//            // 5. Якщо товару немає в базі — додаємо його
//            productToSave = productRepository.save(scannedProduct);
//
//            status = inventoryProductStatusRepository.findByName("ADDED")
//                    .orElseThrow(() -> new RuntimeException("Status ADDED not found"));
//
//        }
//
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        // 6. Додаємо в таблицю результатів інвентаризації
//        InventoryResult inventoryResult = InventoryResult.builder()
//                .inventorySession(session)
//                .product(productToSave)
//                .status(status)
//                .scannedBy(employee)
//                .scanTime(LocalDateTime.now())
//                .description(null)
//                .build();
//
//        return inventoryResultRepository.save(inventoryResult);
//    }
//
//    private boolean isProductDataSame(Product existing, Product scanned) {
//        return Objects.equals(existing.getName(), scanned.getName())
//                && Objects.equals(existing.getDescription(), scanned.getDescription())
//                && Objects.equals(existing.getProductWorkId(), scanned.getProductWorkId())
//                && Objects.equals(existing.getCategory(), scanned.getCategory())
//                && Objects.equals(existing.getStorage(), scanned.getStorage());
//    }
//
//    @Transactional
//    public void markMissingProducts(Long sessionId, Long employeeId) {
//        List<Product> allProducts = productRepository.findAll();
//        List<InventoryResult> scannedResults = inventoryResultRepository.findByInventorySession_InventorySessionId(sessionId);
//
//        Set<Long> scannedProductIds = scannedResults.stream()
//                .map(result -> result.getProduct().getProductId())
//                .collect(Collectors.toSet());
//
//        InventorySession session = inventorySessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Session not found"));
//
//        InventoryProductStatus notFoundStatus = inventoryProductStatusRepository.findByName("NOT_FOUND")
//                .orElseThrow(() -> new RuntimeException("Status NOT_FOUND not found"));
//
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//
//        for (Product product : allProducts) {
//            if (!scannedProductIds.contains(product.getProductId())) {
//                InventoryResult result = InventoryResult.builder()
//                        .inventorySession(session)
//                        .product(product)
//                        .status(notFoundStatus)
//                        .scannedBy(employee)
//                        .scanTime(LocalDateTime.now())
//                        .description("Product not found during inventory")
//                        .build();
//                inventoryResultRepository.save(result);
//            }
//        }
//    }
//
//
//
//    public List<Product> getProductsNotInInventoryResultForSession(Long sessionId) {
//        return inventoryResultRepository.findProductsNotInInventoryResultForSession(sessionId);
//    }
//
//    public InventoryResult scanProduct(Long sessionId, Long employeeId, Long productId, String statusName, String description) {
//        InventorySession session = inventorySessionRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Session not found"));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        InventoryProductStatus status = inventoryProductStatusRepository.findByName(statusName)
//                .orElseThrow(() -> new RuntimeException("Status not found"));
//
//        InventoryResult result = new InventoryResult();
//        result.setInventorySession(session);
//        result.setProduct(product);
//        result.setScannedBy(employee);
//        result.setStatus(status);
//        result.setScanTime(LocalDateTime.now());
//        result.setDescription(description);
//
//        return inventoryResultRepository.save(result);
//    }
//
//    public boolean isProductAlreadyScanned(Long sessionId, Long productId) {
//        return inventoryResultRepository.existsByInventorySession_InventorySessionIdAndProduct_ProductId(sessionId, productId);
//    }
//}
