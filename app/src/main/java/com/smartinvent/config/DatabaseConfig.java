package com.smartinvent.config;

public class DatabaseConfig {
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private String url;


    public DatabaseConfig(String host, String port, String database, String username, String password, String url) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getDatabase() {
        return database;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }
}
