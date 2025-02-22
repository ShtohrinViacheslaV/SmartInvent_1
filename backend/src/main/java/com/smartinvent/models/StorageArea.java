//package com.smartinvent.models;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "storage_area")
//public class StorageArea {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long areaId;
//
//    private String name;
//
//    @ManyToOne
//    @JoinColumn(name = "storage_id", nullable = false)
//    private Storage storage;
//
//    @ManyToOne
//    @JoinColumn(name = "company_id", nullable = false)
//    private Company company;
//
//    public Long getAreaId() {
//        return areaId;
//    }
//
//    public void setAreaId(Long areaId) {
//        this.areaId = areaId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Storage getStorage() {
//        return storage;
//    }
//
//    public void setStorage(Storage storage) {
//        this.storage = storage;
//    }
//
//    public Company getCompany() {
//        return company;
//    }
//
//    public void setCompany(Company company) {
//        this.company = company;
//    }
//}
