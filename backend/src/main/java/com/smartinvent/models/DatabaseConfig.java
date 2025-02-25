package com.smartinvent.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseConfig {
    private String url;
    private String username;
    private String password;
//    private String driverClassName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getDriverClassName() {
//        return driverClassName;
//    }
//
//    public void setDriverClassName(String driverClassName) {
//        this.driverClassName = driverClassName;
//    }
}

//
//
//public class DatabaseConfig {
//    private String dbType;
//    private String host;
//    private String port;
//    private String database;
//    private String user;
//    private String password;
//    private String url;
//
//    // Гетери та сетери
//    public String getDbType() { return dbType; }
//    public void setDbType(String dbType) { this.dbType = dbType; }
//
//    public String getHost() { return host; }
//    public void setHost(String host) { this.host = host; }
//
//    public String getPort() { return port; }
//    public void setPort(String port) { this.port = port; }
//
//    public String getDatabase() { return database; }
//    public void setDatabase(String database) { this.database = database; }
//
//    public String getUser() { return user; }
//    public void setUser(String user) { this.user = user; }
//
//    public String getPassword() { return password; }
//    public void setPassword(String password) { this.password = password; }
//
//    public String getUrl() { return url; }
//    public void setUrl(String url) { this.url = url; }
//}
