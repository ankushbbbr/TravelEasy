package com.example.ankush.traveleasy;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ankushbabbar on 21-Jan-17.
 */

public class StationAutoCompleteResponse {
    class Station{
        String code;
        @SerializedName("fullname")
        String name;
    }
    @SerializedName("station")
    ArrayList<Station> station;
}
