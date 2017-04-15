package com.example.ankush.traveleasy.Api;

import com.example.ankush.traveleasy.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ann Mary George on 19-Jan-17.
 */

public class ApiClientFlight {
    static ApiServiceFlight service;

    public static ApiServiceFlight getService() {
        if(service==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.FLIGHT_API_BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(ApiServiceFlight.class);
        }
        return service;
    }
}
