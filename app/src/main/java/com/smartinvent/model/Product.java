package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Product implements Parcelable {
    private Long productId;
    private String name;
    private String description;
    private String productWorkId;
    private Integer count;
    private Category category;
    private Storage storage;
    private BigDecimal price;
    private String manufacturer;
    private String expirationDate;
    private BigDecimal weight;
    private String dimensions;

    public Product(Long productId, String name, String description, String productWorkId,
                   Category category, Storage storage, BigDecimal price, String manufacturer,
                   String expirationDate, BigDecimal weight, String dimensions) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.productWorkId = productWorkId;
        this.count = count;
        this.category = category;
        this.storage = storage;
        this.price = price;
        this.manufacturer = manufacturer;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.dimensions = dimensions;
    }

    public Product(String name, String description, String productWorkId, Integer count, Category category,
                   Storage storage, BigDecimal price, String manufacturer, String expirationDate,
                   BigDecimal weight, String dimensions) {
        this.name = name;
        this.description = description;
        this.productWorkId = productWorkId;
        this.count = count;
        this.category = category;
        this.storage = storage;
        this.price = price;
        this.manufacturer = manufacturer;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.dimensions = dimensions;
    }

    protected Product(Parcel in) {
        if (in.readByte() == 0) {
            productId = null;
        } else {
            productId = in.readLong();
        }
        name = in.readString();
        description = in.readString();
        productWorkId = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = new Integer(in.readString());
        }
        category = in.readParcelable(Category.class.getClassLoader());
        storage = in.readParcelable(Storage.class.getClassLoader());
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = new BigDecimal(in.readString());
        }
        manufacturer = in.readString();
        expirationDate = in.readString();
        if (in.readByte() == 0) {
            weight = null;
        } else {
            weight = new BigDecimal(in.readString());
        }
        dimensions = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (productId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(productId);
        }
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(productWorkId);
        if (count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(count.toString());
        }
        dest.writeParcelable(category, flags);
        dest.writeParcelable(storage, flags);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(price.toString());
        }
        dest.writeString(manufacturer);
        dest.writeString(expirationDate);
        if (weight == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(weight.toString());
        }
        dest.writeString(dimensions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Гетери та сетери
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProductWorkId() { return productWorkId; }
    public void setProductWorkId(String productWorkId) { this.productWorkId = productWorkId; }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Storage getStorage() { return storage; }
    public void setStorage(Storage storage) { this.storage = storage; }

    public Long getCategoryId() {
        return category != null ? category.getCategoryId() : null;
    }

    public Long getStorageId() {
        return storage != null ? storage.getStorageId() : null;
    }

    public void setCategoryId(Long categoryId) {
        if (category == null) {
            category = new Category(); // Переконайтесь, що є конструктор без параметрів
        }
        category.setCategoryId(categoryId);
    }

    public void setStorageId(Long storageId) {
        if (storage == null) {
            storage = new Storage();
        }
        storage.setStorageId(storageId);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
}