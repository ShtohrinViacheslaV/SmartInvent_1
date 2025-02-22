//package com.smartinvent.service;
//
//import com.smartinvent.models.DataChangeLog;
//import com.smartinvent.repositories.DataChangeLogRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class DataChangeLogService {
//
//    @Autowired
//    private DataChangeLogRepository dataChangeLogRepository;
//
//    public DataChangeLog createDataChangeLog(DataChangeLog dataChangeLog) {
//        return dataChangeLogRepository.save(dataChangeLog);
//    }
//
//    public List<DataChangeLog> getAllDataChangeLogs() {
//        return dataChangeLogRepository.findAll();
//    }
//
//    public DataChangeLog getDataChangeLogById(Long id) {
//        return dataChangeLogRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("DataChangeLog not found with id: " + id));
//    }
//
//    public void deleteDataChangeLog(Long id) {
//        DataChangeLog dataChangeLog = dataChangeLogRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("DataChangeLog not found with id: " + id));
//        dataChangeLogRepository.delete(dataChangeLog);
//    }
//}
