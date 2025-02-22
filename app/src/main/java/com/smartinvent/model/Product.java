package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private Long productId;
    private String name;
    private String description;
    private String productWorkId;
    private Integer count;
    private byte[] qrCode; // Поле для збереження QR-коду у вигляді байтового масиву
    private Category category;
    private Storage storage; // Використовуємо об'єкт Storage замість `storageId`


    // Конструктор без ID
    public Product(String name, String description, String productWorkId, Integer count, byte[] qrCode, Category category, Storage storage) {
        this.name = name;
        this.description = description;
        this.productWorkId = productWorkId;
        this.count = count;
        this.qrCode = qrCode;
        this.category = category;
        this.storage = storage;
    }

    // Конструктор з ID
    public Product(Long productId, String name, String description, String productWorkId, Integer count, byte[] qrCode, Category category, Storage storage) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.productWorkId = productWorkId;
        this.count = count;
        this.qrCode = qrCode;
        this.category = category;
        this.storage = storage;
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
            count = in.readInt();
        }
        qrCode = in.createByteArray();
        category = in.readParcelable(Category.class.getClassLoader());
        storage = in.readParcelable(Storage.class.getClassLoader());
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
            dest.writeInt(count);
        }
        dest.writeByteArray(qrCode);
        dest.writeParcelable(category, flags);
        dest.writeParcelable(storage, flags);
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

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public byte[] getQrCode() { return qrCode; }
    public void setQrCode(byte[] qrCode) { this.qrCode = qrCode; }

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



}



//package com.smartinvent.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//public class Product implements Parcelable {
//    private Long productId;
//    private String name;
//    private String description;
//    private String productWorkId; // Обов'язковий ідентифікаційний номер
//    private byte[] qrCode;
//    private int count; // Тепер ціле число
//    private Category category;
//    private Long storageId; // Додаємо склад, де знаходиться товар
//
//    public Product(String name, String description, String productWorkId, byte[] qrCode, int count, Category category, Long storageId) {
//        this.name = name;
//        this.description = description;
//        this.productWorkId = productWorkId;
//        this.qrCode = qrCode;
//        this.count = count;
//        this.category = category;
//        this.storageId = storageId;
//    }
//
//    public Product(Long productId, String name, String description, String productWorkId, byte[] qrCode, int count, Category category, Long storageId) {
//        this.productId = productId;
//        this.name = name;
//        this.description = description;
//        this.productWorkId = productWorkId;
//        this.qrCode = qrCode;
//        this.count = count;
//        this.category = category;
//        this.storageId = storageId;
//    }
//
//    protected Product(Parcel in) {
//        if (in.readByte() == 0) {
//            productId = null;
//        } else {
//            productId = in.readLong();
//        }
//        name = in.readString();
//        description = in.readString();
//        productWorkId = in.readString();
//        qrCode = in.createByteArray();
//        count = in.readInt();
//        category = in.readParcelable(Category.class.getClassLoader());
//        if (in.readByte() == 0) {
//            storageId = null;
//        } else {
//            storageId = in.readLong();
//        }
//    }
//
//    public static final Creator<Product> CREATOR = new Creator<Product>() {
//        @Override
//        public Product createFromParcel(Parcel in) {
//            return new Product(in);
//        }
//
//        @Override
//        public Product[] newArray(int size) {
//            return new Product[size];
//        }
//    };
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        if (productId == null) {
//            dest.writeByte((byte) 0);
//        } else {
//            dest.writeByte((byte) 1);
//            dest.writeLong(productId);
//        }
//        dest.writeString(name);
//        dest.writeString(description);
//        dest.writeString(productWorkId);
//        dest.writeByteArray(qrCode);
//        dest.writeInt(count);
//        dest.writeParcelable(category, flags);
//        if (storageId == null) {
//            dest.writeByte((byte) 0);
//        } else {
//            dest.writeByte((byte) 1);
//            dest.writeLong(storageId);
//        }
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public Long getProductId() { return productId; }
//    public String getName() { return name; }
//    public String getDescription() { return description; }
//    public String getProductWorkId() { return productWorkId; }
//    public byte[] getQrCode() { return qrCode; }
//    public int getCount() { return count; }
//    public Category getCategory() { return category; }
//    public Long getStorageId() { return storageId; }
//
//    public void setProductId(Long productId) { this.productId = productId; }
//    public void setName(String name) { this.name = name; }
//    public void setDescription(String description) { this.description = description; }
//    public void setProductWorkId(String productWorkId) { this.productWorkId = productWorkId; }
//    public void setQrCode(byte[] qrCode) { this.qrCode = qrCode; }
//    public void setCount(int count) { this.count = count; }
//    public void setCategory(Category category) { this.category = category; }
//    public void setStorageId(Long storageId) { this.storageId = storageId; }
//}
