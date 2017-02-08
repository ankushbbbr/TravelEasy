package com.example.ankush.traveleasy;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ann Mary George on 24-Jan-17.
 */

public interface ApiAirportService {
    @GET("v1.2/airports/autocomplete")
    Call<ArrayList<AirportAutoCompleteResponse>> getAirportSuggestions(@Query("apikey") String apikey,
                                                                       @Query("term") String term,
                                                                       @Query("country") String country);

}
