package com.example.ankush.traveleasy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ann Mary George on 19-Jan-17.
 */
// "http://developer.goibibo.com/api/search/?app_id=75ebdc31&app_key=5dbe77b3ce284fe43c3fce7da25dfe95&" +
  //      "format=json&source=DEL&" +
    //    "destination=COK&dateofdeparture=20170128&dateofarrival=20170128&
// seatingclass=E&adults=1&children=0&infants=0&counter=100"
public interface ApiServiceFlight {
   /* @GET("between/source/{scode}/dest/{dcode}/date/{date}/apikey/{apikey}/")
    Call<TrainBetweenStationsResponse> searchTrainBetween(@Path("scode") String srcStationCode, @Path("dcode") String destStationCode
            , @Path("date") String date, @Path("apikey") String apiKey);*/

     @GET("api/search/")
    Call<FlightResponse> searchFlight(@Query("app_id") String app_id, @Query("app_key") String app_key, @Query("format") String format,
                                      @Query("source") String src, @Query("destination") String dest,
                                      @Query("dateofdeparture") String dd, @Query("dateofarrival") String da,
                                      @Query("seatingclass") String seatingclass, @Query("adults") String adult,
                                      @Query("children") String children, @Query("infants") String infants,
                                      @Query("counter") String counter);



}
