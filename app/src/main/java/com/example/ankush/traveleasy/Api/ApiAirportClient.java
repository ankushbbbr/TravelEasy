package com.example.ankush.traveleasy.Api;

import com.example.ankush.traveleasy.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ann Mary George on 24-Jan-17.
 */

public class ApiAirportClient {
    static ApiServiceAirport service;

    public static ApiServiceAirport getService() {
        if(service==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.IATA_API_BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(ApiServiceAirport.class);
        }
        return service;
    }
}
