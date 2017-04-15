package com.example.ankush.traveleasy.ApiResponse;

import java.util.ArrayList;

/**
 * Created by Ann Mary George on 19-Jan-17.
 */

public class FlightResponse {
     public Data data;

     public class Data{
        public  ArrayList<Flight> returnflights;
        public ArrayList<Flight> onwardflights;
        public class Flight{
            public String origin;
            public String destination;
            public String duration;
            public String flightno;
            public String FlHash;
            public String deptime;
            public String arrtime;
            public Fare fare;
            public class Fare{
                public int grossamount;
            }
        }
        public ArrayList<Flight> getreturn(){
            return returnflights;
        };

    }



}
