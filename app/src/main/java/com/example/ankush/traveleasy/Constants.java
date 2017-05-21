package com.example.ankush.traveleasy;

/**
 * Created by ankushbabbar on 16-Jan-17.
 */

public class Constants {
    public static final int TRANSPORT_TYPE_TRAIN = 1;
    public static final int TRANSPORT_TYPE_FLIGHT = 2;
    public static final int TRANSPORT_TYPE_BUS = 3;

    public static final String INTENT_TRAIN_SRC_STATION_CODE = "train_src_station";
    public static final String INTENT_TRAIN_DEST_STATION_CODE = "train_dest_station";
    public static final String INTENT_TRAIN_DATE = "train_date";

    public static final String INTENT_FLIGHT_SRC_IATA_CODE = "flight_src_code";
    public static final String INTENT_FLIGHT_DEST_IATA_CODE = "flight_dest_code";

    public static final String RAILWAY_API_BASE_URL = "http://api.railwayapi.com/";
    public static final String RAILWAY_API_KEY="Use_Your_own_key";

    public static final String FLIGHT_TYPE = "flight_type";

    public static final String FLIGHT_API_BASE_URL = "https://developer.goibibo.com/";
    public static final String FLIGHT_API_KEY="Use_Your_own_key";
    public static final String FLIGHT_API_ID="Use_Your_own_key";

    public static final String IATA_API_BASE_URL = "https://api.sandbox.amadeus.com/";
    public static final String IATA_API_KEY="Use_Your_own_key";
}
