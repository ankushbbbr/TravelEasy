package com.example.ankush.traveleasy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ankushbabbar on 18-Jan-17.
 */

public class ApiClient {
    static ApiService service;

    public static ApiService getService() {
        if(service==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.RAILWAY_API_BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
            service =retrofit.create(ApiService.class);
        }
        return service;
    }
}
