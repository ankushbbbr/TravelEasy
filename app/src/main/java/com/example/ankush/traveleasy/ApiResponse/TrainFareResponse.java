package com.example.ankush.traveleasy.ApiResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ankushbabbar on 20-Apr-17.
 */

public class TrainFareResponse {
    public class Fare {
        @SerializedName("code")
        public String classCode;
        @SerializedName("name")
        public String className;
        public String fare;
    }
    @SerializedName("fare")
    public ArrayList<Fare> fares;
}
