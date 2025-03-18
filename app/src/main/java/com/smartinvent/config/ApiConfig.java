package com.smartinvent.config;

public class ApiConfig {
    private static String BASE_URL = "http://192.168.0.120:8080";
//    private static String BASE_URL = "http://192.168.249.76:8080";


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String newBaseUrl) {
        BASE_URL = newBaseUrl;
    }

    // Додаткові конфігурації API
    public static String getApiProducts() {
        return BASE_URL + "/api/products";
    }

    public static String getApiCategories() {
        return BASE_URL + "/api/categories";
    }

    public static String getApiSync() {
        return BASE_URL + "/api/sync";
    }
}
