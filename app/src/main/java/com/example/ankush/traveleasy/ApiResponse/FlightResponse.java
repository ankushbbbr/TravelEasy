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
            public String airline;
            public String flightcode;
            public String carrierid;
            public String deptime;
            public String stops;
            public String arrtime;
            public Fare fare;
            //public OnwardFlights onwardflights;
            public class Fare{
                public int grossamount;
            }
//            public class OnwardFlights{
//
//            }
        }
        public ArrayList<Flight> getreturn(){
            return returnflights;
        };

    }



}
