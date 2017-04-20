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
    ArrayList<String> fares;
    String date;
    String source;
    String destination;
    String srcCode;
    String destCode;
    String travelTime;
    String departureTime;
    String arrivalTime;
    String fare;
    public Transport(){
        classes = new ArrayList<>();
        fares = new ArrayList<>();
        fare = "0";
    }
}
