package com.example.ankush.traveleasy;

import java.util.ArrayList;

/**
 * Created by ankushbabbar on 16-Jan-17.
 */

public class Transport {
    int type;
    String name;
    String number;
    ArrayList<String> classes;
    ArrayList<Float> fares;
    String source;
    String destination;
    String travelTime;
    String departureTime;
    String arrivalTime;
    String averageFare;
    public Transport(){
        classes = new ArrayList<>();
        fares = new ArrayList<>();
    }
}
