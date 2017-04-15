package com.example.ankush.traveleasy.ApiResponse;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ankushbabbar on 18-Jan-17.
 */

public class TrainBetweenStationsResponse {
    public ArrayList<Train> train;
    public class Train{
        public String number;
        public String name;
        public ArrayList<Day> days;
        public class Day{
            @SerializedName("day-code")
            public String name;
            public String runs;
        }
        public String travel_time;
        public String src_departure_time;
        public String dest_arrival_time;
        public class Classs{
            public String available;
            @SerializedName("class-code")
            public String name;
        }
        public ArrayList<Classs> classes;
        public class From{
            public String name;
            public String code;
        }
        public class To{
            public String name;
            public String code;
        }
        public From from;
        public To to;
    }
}
