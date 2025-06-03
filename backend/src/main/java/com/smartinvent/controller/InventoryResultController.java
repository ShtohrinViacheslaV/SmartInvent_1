package com.smartinvent.controller;


import com.smartinvent.dto.CreateProductDuringInventoryRequest;
import com.smartinvent.dto.InventoryProductResultDto;
import com.smartinvent.dto.InventorySessionProductDTO;
import com.smartinvent.models.Employee;
import com.smartinvent.models.InventoryProductStatusEnum;
import com.smartinvent.models.InventoryResult;
import com.smartinvent.models.Product;
import com.smartinvent.service.InventoryResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory/result")
@RequiredArgsConstructor
public class InventoryResultController {


    private final InventoryResultService resultService;


    // Додавання результату інвентаризації
    @PostMapping("/add")
    public ResponseEntity<InventoryResult> addInventoryResult(@RequestBody InventoryResult inventoryResult) {
        InventoryResult result = resultService.addInventoryResult(inventoryResult);
        return ResponseEntity.ok(result);
    }

    // Оновлення результату інвентаризації
    @PutMapping("/update/{resultId}")
    public ResponseEntity<InventoryResult> updateInventoryResult(@PathVariable Long resultId, @RequestBody InventoryResult inventoryResult) {
        InventoryResult result = resultService.updateInventoryResult(resultId, inventoryResult);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<InventoryProductResultDto>> getResultsBySession(
            @PathVariable Long sessionId) {
        List<InventoryProductResultDto> results = resultService.getResultsBySession(sessionId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/session/{sessionId}/products/by-work-id/{productWorkId}")
    public ResponseEntity<InventoryProductResultDto> getProductByWorkId(
            @PathVariable Long sessionId,
            @PathVariable String productWorkId
    ) {
        InventoryProductResultDto dto = resultService.getProductBySessionAndWorkId(sessionId, productWorkId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/session/{sessionId}/products/by-id/{productId}")
    public ResponseEntity<InventoryProductResultDto> getProductById(@PathVariable Long sessionId, @PathVariable Long productId) {
        InventoryProductResultDto dto = resultService.getProductBySessionAndProductId(sessionId, productId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/session/{sessionId}/save-or-update/result")
    public ResponseEntity<Void> saveOrUpdateInventoryResult(@PathVariable Long sessionId,
                                                            @RequestBody InventoryProductResultDto dto) {
        resultService.saveOrUpdateInventoryProductResultDto(sessionId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/session/{sessionId}/status-counts")
    public ResponseEntity<Map<String, Long>> getStatusCounts(@PathVariable Long sessionId) {
        return ResponseEntity.ok(resultService.countStatusesBySession(sessionId));
    }



    @GetMapping("/session/{sessionId}/products/status/{status}")
    public ResponseEntity<List<InventoryProductResultDto>> getProductsByStatus(
            @PathVariable Long sessionId,
            @PathVariable InventoryProductStatusEnum status) {
        List<InventoryProductResultDto> result = resultService.getProductsByStatus(sessionId, status);
        return ResponseEntity.ok(result);
    }






    // Отримання товарів для сесії
    @GetMapping("/products/{sessionId}")
    public ResponseEntity<List<InventorySessionProductDTO>> getProductsForSession(@PathVariable Long sessionId) {
        List<InventorySessionProductDTO> products = resultService.getProductsForSession(sessionId);
        return ResponseEntity.ok(products);
    }

    // Оновлення статусу не перевірених товарів
    @PostMapping("/mark-not-found/{sessionId}")
    public ResponseEntity<Void> markUncheckedProductsAsNotFound(@PathVariable Long sessionId) {
        resultService.markUncheckedProductsAsNotFound(sessionId);
        return ResponseEntity.ok().build();
    }

    // Додавання товару під час інвентаризації
    @PostMapping("/add-product")
    public ResponseEntity<InventorySessionProductDTO> addProductDuringInventory(@RequestBody CreateProductDuringInventoryRequest request) {
        InventorySessionProductDTO productDto = resultService.createProductDuringInventory(request);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<InventorySessionProductDTO>> searchInventorySessionProducts(
            @RequestParam Long sessionId,
            @RequestParam String query,
            @RequestParam(defaultValue = "name") String criteria,
            @RequestParam(required = false) String sortBy
    ) {
        List<InventorySessionProductDTO> result = resultService.searchProducts(sessionId, query, criteria, sortBy);
        return ResponseEntity.ok(result);
    }



}