//package com.smartinvent.service;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//import org.sqlite.SQLiteDataSource;
//
//import javax.sql.DataSource;
//import java.io.File;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//@Service
//public class DatabaseExportService {
//    private final JdbcTemplate jdbcTemplate;
//
//    public DatabaseExportService(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public File createSQLiteCopy() throws Exception {
//        String sqliteUrl = "jdbc:sqlite:inventory_copy.sqlite";
//        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
//        sqLiteDataSource.setUrl(sqliteUrl);
//
//        try (Connection sqliteConn = sqLiteDataSource.getConnection();
//             Statement sqliteStmt = sqliteConn.createStatement()) {
//
//            // Створення таблиць у локальній SQLite
//            sqliteStmt.execute("CREATE TABLE IF NOT EXISTS Product ("
//                    + "product_id INTEGER PRIMARY KEY, "
//                    + "name TEXT, "
//                    + "description TEXT, "
//                    + "product_work_id TEXT, "
//                    + "count INTEGER, "
//                    + "qrCode BLOB, "
//                    + "category_id INTEGER"
//                    + ");");
//
//            // Отримання даних із PostgreSQL
//            String sql = "SELECT product_id, name, description, product_work_id, count, qrCode, category_id FROM Product";
//            try (PreparedStatement pst = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql);
//                 ResultSet rs = pst.executeQuery();
//                 PreparedStatement insertStmt = sqliteConn.prepareStatement(
//                         "INSERT INTO Product (product_id, name, description, product_work_id, count, qrCode, category_id) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
//
//                while (rs.next()) {
//                    insertStmt.setInt(1, rs.getInt("product_id"));
//                    insertStmt.setString(2, rs.getString("name"));
//                    insertStmt.setString(3, rs.getString("description"));
//                    insertStmt.setString(4, rs.getString("product_work_id"));
//                    insertStmt.setInt(5, rs.getInt("count"));
//                    insertStmt.setBytes(6, rs.getBytes("qrCode"));
//                    insertStmt.setInt(7, rs.getInt("category_id"));
//                    insertStmt.executeUpdate();
//                }
//            }
//        }
//        return new File("inventory_copy.sqlite");
//    }
//}
