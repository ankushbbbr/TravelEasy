package com.example.ankush.traveleasy;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by ankushbabbar on 16-Jan-17.
 */

public class Transport {
    int type;
    String name;
    String number;
    ArrayList<String> classes;
    ArrayList<Float> fare;
    String source;
    String destination;
    String travel_time;
    String departure_time;
    String arrival_time;
    public Transport(){
        classes = new ArrayList<>();
        fare = new ArrayList<>();
    }
}
