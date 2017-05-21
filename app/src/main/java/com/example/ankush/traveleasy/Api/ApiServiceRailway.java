package com.example.ankush.traveleasy.Api;

import com.example.ankush.traveleasy.ApiResponse.StationAutoCompleteResponse;
import com.example.ankush.traveleasy.ApiResponse.TrainBetweenStationsResponse;
import com.example.ankush.traveleasy.ApiResponse.TrainFareResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ankushbabbar on 18-Jan-17.
 */

public interface ApiServiceRailway {
    @GET("between/source/{scode}/dest/{dcode}/date/{date}/apikey/{apikey}/")
    Call<TrainBetweenStationsResponse> searchTrainBetween(@Path("scode") String srcStationCode,
                                                          @Path("dcode") String destStationCode,
                                                          @Path("date") String date, @Path("apikey") String apiKey);

    @GET("suggest_station/name/{station_name}/apikey/{apikey}/")
    Call<StationAutoCompleteResponse> getStationSuggestions(@Path("station_name") String station_name,
                                                            @Path("apikey") String apiKey);

    @GET("fare/train/{train_no}/source/{src_code}/dest/{dest_code}/age/25/quota/GEN/doj/{date}/apikey/{apikey}/")
    Call<TrainFareResponse> getTrainFare(@Path("train_no") String trainNo,@Path("src_code") String srcCode,
                                         @Path("dest_code") String destCode,@Path("date") String date,
                                         @Path("apikey") String apiKey);
}
