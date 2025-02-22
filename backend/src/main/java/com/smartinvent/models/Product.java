package com.smartinvent.models;

import jakarta.persistence.*;



   @Entity
   @Table(name = "product")
   public class Product {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long productId;

       private String name;
       private String description;
       private String productWorkId;
       private Integer count;
       @Lob // Використовується для збереження великих об'єктів
       private byte[] qrCode;

       @ManyToOne
       @JoinColumn(name = "category_id")
       private Category category;

       @ManyToOne
       @JoinColumn(name = "storage_id")
       private Storage storage;


       public Product() {
       }

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

       public Integer getCount() {
           return count;
       }

       public void setCount(Integer count) {
           this.count = count;
       }

       public byte[] getQrCode() {
           return qrCode;
       }

       public void setQrCode(byte[] qrCode) {
           this.qrCode = qrCode;
       }

       public Category getCategory() {
           return category;
       }

       public void setCategory(Category category) {
           this.category = category;
       }

       public Storage getStorage() {
           return storage;
       }

       public void setStorage(Storage storage) {
           this.storage = storage;
       }
   }
