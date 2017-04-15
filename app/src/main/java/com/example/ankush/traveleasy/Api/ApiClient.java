package com.example.ankush.traveleasy.Api;

import com.example.ankush.traveleasy.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ankushbabbar on 18-Jan-17.
 */

public class ApiClient {
    static ApiRailwayService service;

    public static ApiRailwayService getService() {
        if(service==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.RAILWAY_API_BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
            service =retrofit.create(ApiRailwayService.class);
        }
        return service;
    }
}
