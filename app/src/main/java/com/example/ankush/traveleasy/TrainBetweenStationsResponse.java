package com.example.ankush.traveleasy;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ankushbabbar on 18-Jan-17.
 */

public class TrainBetweenStationsResponse {
    ArrayList<Train> train;
    class Train{
        String number;
        String name;
        ArrayList<Day> days;
        class Day{
            @SerializedName("day-code")
            String name;
            String runs;
        }
        String travel_time;
        String src_departure_time;
        String dest_arrival_time;
        class Classs{
            String available;
            @SerializedName("class-code")
            String name;
        }
        ArrayList<Classs> classes;
        class From{
            String name;
            String code;
        }
        class To{
            String name;
            String code;
        }
        From from;
        To to;
    }
}
