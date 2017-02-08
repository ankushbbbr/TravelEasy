package com.example.ankush.traveleasy;

import java.util.ArrayList;

/**
 * Created by Ann Mary George on 19-Jan-17.
 */

public class FlightResponse {
     public Data data;

     class Data{
        public  ArrayList<Flight> returnflights;
        public ArrayList<Flight> onwardflights;
        class Flight{
            String origin;
            String destination;
            String duration;
            String flightno;
            String FlHash;
            String deptime;
            String arrtime;
            Fare fare;
            class Fare{
                int grossamount;
            }
        }
        public ArrayList<Flight> getreturn(){
            return returnflights;
        };

    }



}
