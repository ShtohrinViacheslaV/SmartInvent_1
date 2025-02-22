package com.smartinvent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private Long categoryId;
    private String name;

    public Category() {
    }

    // Конструктор без ID
    public Category(String name) {
        this.name = name;
    }

    // Конструктор з ID
    public Category(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    protected Category(Parcel in) {
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readLong();
        }
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(categoryId);
        }
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return name;
    }
}



//package com.smartinvent.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//public class Category implements Parcelable {
//    private Long categoryId;
//    private String categoryName;
//
//    public Category() {
//    }
//
//    public Category(Long categoryId, String categoryName) {
//        this.categoryId = categoryId;
//        this.categoryName = categoryName;
//    }
//
//    protected Category(Parcel in) {
//        if (in.readByte() == 0) {
//            categoryId = null;
//        } else {
//            categoryId = in.readLong();
//        }
//        categoryName = in.readString();
//    }
//
//    public static final Creator<Category> CREATOR = new Creator<Category>() {
//        @Override
//        public Category createFromParcel(Parcel in) {
//            return new Category(in);
//        }
//
//        @Override
//        public Category[] newArray(int size) {
//            return new Category[size];
//        }
//    };
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        if (categoryId == null) {
//            dest.writeByte((byte) 0);
//        } else {
//            dest.writeByte((byte) 1);
//            dest.writeLong(categoryId);
//        }
//        dest.writeString(categoryName);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//
//    public void setCategoryId(Long categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
//
//    public Long getCategoryId() { return categoryId; }
//    public String getCategoryName() { return categoryName; }
//
//    public String toString() {
//        return categoryName;
//    }
//
//}
//
//
//
////package com.smartinvent.model;
////
////import java.util.Objects;
////
////
////public class Category {
////
////    private Long categoryId;
////    private String name;
////    private String description;
////    private String productType;  // Додаткове поле для типу товару
////
////
////    public Long getCategoryId() {
////        return categoryId;
////    }
////
////    public void setCategoryId(Long categoryId) {
////        this.categoryId = categoryId;
////    }
////
////    public String getName() {
////        return name;
////    }
////
////    public void setName(String name) {
////        this.name = name;
////    }
////
////    public String getDescription() {
////        return description;
////    }
////
////    public void setDescription(String description) {
////        this.description = description;
////    }
////
////    public String getProductType() {
////        return productType;
////    }
////
////    public void setProductType(String productType) {
////        this.productType = productType;
////    }
////
////
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////        Category category = (Category) o;
////        return Objects.equals(categoryId, category.categoryId);
////    }
////
////    @Override
////    public int hashCode() {
////        return Objects.hash(categoryId);
////    }
////}