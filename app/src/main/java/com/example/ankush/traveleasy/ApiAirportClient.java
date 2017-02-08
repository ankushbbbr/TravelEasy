package com.example.ankush.traveleasy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ann Mary George on 24-Jan-17.
 */

public class ApiAirportClient {
    static ApiAirportService service;

    public static ApiAirportService getService() {
        if(service==null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.IATA_API_BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(ApiAirportService.class);
        }
        return service;
    }
}
