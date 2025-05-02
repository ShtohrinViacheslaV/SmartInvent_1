package com.smartinvent.dto;

import java.time.LocalDate;

public class InventorySessionProductDTO {
    private Long productId;
    private String name;
    private String description;
    private String productWorkId;
    private String categoryName;
    private String storageName;
    private String status; // confirmed, modified, not_found, added, unchecked
    private boolean locked; // заблокований товар (хтось сканує його зараз)

    public InventorySessionProductDTO() {
    }

    public InventorySessionProductDTO(Long productId, String name, String description, String productWorkId,
                                      String categoryName, String storageName, String status, boolean locked) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.productWorkId = productWorkId;
        this.categoryName = categoryName;
        this.storageName = storageName;
        this.status = status;
        this.locked = locked;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductWorkId() {
        return productWorkId;
    }

    public void setProductWorkId(String productWorkId) {
        this.productWorkId = productWorkId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
