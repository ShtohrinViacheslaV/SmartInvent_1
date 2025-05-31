package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;


import java.math.BigDecimal;

public class InventoryProductResultDto implements Parcelable {

    private Long inventoryResultId;
    private Long inventorySessionId;

    private Long productId;
    private String productName;
    private String productDescription;
    private String productWorkId;
    private Integer productCount;
    private String categoryName;
    private String storageName;
    private BigDecimal price;
    private Integer count;
    private String manufacturer;
    private String expirationDate;
    private BigDecimal weight;
    private String dimensions;

    private InventoryProductStatusEnum status;
    private Long scannedBy;
    private String description;

    public InventoryProductResultDto() {
    }

    public InventoryProductResultDto(Long inventoryResultId, Long inventorySessionId,
                                     Long productId, String productName, String productDescription,
                                     String productWorkId, Integer productCount, String categoryName,
                                     String storageName, BigDecimal price, Integer count, String manufacturer,
                                     String expirationDate, BigDecimal weight, String dimensions,
                                     InventoryProductStatusEnum status, Long scannedBy, String description) {
        this.inventoryResultId = inventoryResultId;
        this.inventorySessionId = inventorySessionId;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productWorkId = productWorkId;
        this.productCount = productCount;
        this.categoryName = categoryName;
        this.storageName = storageName;
        this.price = price;
        this.count = count;
        this.manufacturer = manufacturer;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.dimensions = dimensions;
        this.status = status;
        this.scannedBy = scannedBy;
        this.description = description;
    }


    protected InventoryProductResultDto(Parcel in) {
        if (in.readByte() == 0) {
            inventoryResultId = null;
        } else {
            inventoryResultId = in.readLong();
        }
        if (in.readByte() == 0) {
            inventorySessionId = null;
        } else {
            inventorySessionId = in.readLong();
        }
        if (in.readByte() == 0) {
            productId = null;
        } else {
            productId = in.readLong();
        }
        productName = in.readString();
        productDescription = in.readString();
        productWorkId = in.readString();
        if (in.readByte() == 0) {
            productCount = null;
        } else {
            productCount = in.readInt();
        }
        categoryName = in.readString();
        storageName = in.readString();

        // BigDecimal не підтримується напряму, збережемо як String
        String priceStr = in.readString();
        price = priceStr != null ? new BigDecimal(priceStr) : null;

        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        manufacturer = in.readString();
        expirationDate = in.readString();

        String weightStr = in.readString();
        weight = weightStr != null ? new BigDecimal(weightStr) : null;

        dimensions = in.readString();

        // Enum – збережемо як String, потім перетворимо
        String statusStr = in.readString();
        status = statusStr != null ? InventoryProductStatusEnum.valueOf(statusStr) : null;

        if (in.readByte() == 0) {
            scannedBy = null;
        } else {
            scannedBy = in.readLong();
        }
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (inventoryResultId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(inventoryResultId);
        }
        if (inventorySessionId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(inventorySessionId);
        }
        if (productId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(productId);
        }
        dest.writeString(productName);
        dest.writeString(productDescription);
        dest.writeString(productWorkId);
        if (productCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(productCount);
        }
        dest.writeString(categoryName);
        dest.writeString(storageName);

        dest.writeString(price != null ? price.toPlainString() : null);

        if (count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(count);
        }
        dest.writeString(manufacturer);
        dest.writeString(expirationDate);

        dest.writeString(weight != null ? weight.toPlainString() : null);

        dest.writeString(dimensions);

        dest.writeString(status != null ? status.name() : null);

        if (scannedBy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(scannedBy);
        }
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InventoryProductResultDto> CREATOR = new Creator<InventoryProductResultDto>() {
        @Override
        public InventoryProductResultDto createFromParcel(Parcel in) {
            return new InventoryProductResultDto(in);
        }

        @Override
        public InventoryProductResultDto[] newArray(int size) {
            return new InventoryProductResultDto[size];
        }
    };

    public Long getInventoryResultId() {
        return inventoryResultId;
    }

    public void setInventoryResultId(Long inventoryResultId) {
        this.inventoryResultId = inventoryResultId;
    }

    public Long getInventorySessionId() {
        return inventorySessionId;
    }

    public void setInventorySessionId(Long inventorySessionId) {
        this.inventorySessionId = inventorySessionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductWorkId() {
        return productWorkId;
    }

    public void setProductWorkId(String productWorkId) {
        this.productWorkId = productWorkId;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public InventoryProductStatusEnum getStatus() {
        return status;
    }

    public void setStatus(InventoryProductStatusEnum status) {
        this.status = status;
    }

    public Long getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(Long scannedBy) {
        this.scannedBy = scannedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}