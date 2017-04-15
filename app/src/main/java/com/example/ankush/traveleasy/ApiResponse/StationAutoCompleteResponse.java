package com.example.ankush.traveleasy.ApiResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ankushbabbar on 21-Jan-17.
 */

public class StationAutoCompleteResponse {
    public class Station{
        public String code;
        @SerializedName("fullname")
        public
        String name;
    }
    @SerializedName("station")
    public
    ArrayList<Station> station;
}
