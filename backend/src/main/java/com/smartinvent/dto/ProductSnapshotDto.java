package com.smartinvent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSnapshotDto {
    private Long sessionId;
    private LocalDateTime snapshotTime;
    private List<ProductData> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductData {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String manufacturer;
        private LocalDate expirationDate;
        private BigDecimal weight;
        private String productWorkId;
        private String dimensions;
        private Long categoryId;
        private Long storageId;
    }
}
