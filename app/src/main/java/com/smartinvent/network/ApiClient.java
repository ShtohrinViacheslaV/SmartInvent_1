package com.smartinvent.network;

import com.smartinvent.config.ApiConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static void updateClient(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            updateClient(ApiConfig.getBaseUrl());
        }
        return retrofit;
    }

    public static ApiService getService() {
        return getClient().create(ApiService.class);
    }
}


//package com.smartinvent.network;
//
//import com.smartinvent.config.ApiConfig;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class ApiClient {
//    private static Retrofit retrofit = null;
//
//    public static Retrofit getClient() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(ApiConfig.BASE_URL + "/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//
//    public static ApiService getService() {
//        return getClient().create(ApiService.class);
//    }
//}
