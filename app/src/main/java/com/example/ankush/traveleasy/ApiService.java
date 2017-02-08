package com.example.ankush.traveleasy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ankushbabbar on 18-Jan-17.
 */

public interface ApiService {
    @GET("between/source/{scode}/dest/{dcode}/date/{date}/apikey/{apikey}/")
    Call<TrainBetweenStationsResponse> searchTrainBetween(@Path("scode") String srcStationCode, @Path("dcode") String destStationCode
                ,@Path("date") String date, @Path("apikey") String apiKey);

    @GET("suggest_station/name/{station_name}/apikey/{apikey}/")
    Call<StationAutoCompleteResponse> getStationSuggestions(@Path("station_name") String station_name,@Path("apikey") String apiKey);

}
