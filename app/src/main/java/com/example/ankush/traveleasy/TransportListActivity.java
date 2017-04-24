package com.example.ankush.traveleasy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ankush.traveleasy.Api.ApiClient;
import com.example.ankush.traveleasy.Api.ApiClientFlight;
import com.example.ankush.traveleasy.Api.ApiRailwayService;
import com.example.ankush.traveleasy.Api.ApiServiceFlight;
import com.example.ankush.traveleasy.ApiResponse.FlightResponse;
import com.example.ankush.traveleasy.ApiResponse.TrainBetweenStationsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Transport> transports;
    TransportAdapter transportAdapter;
    final String TAG="TransportListTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);
        listView=(ListView) findViewById(R.id.results_listview);

        transports=new ArrayList<>();
        transportAdapter = new TransportAdapter(this,transports);
        listView.setAdapter(transportAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                transportAdapter.selectClassFromDialog(transports.get(position),transportAdapter.vi);
//            }
//        });
        String src_train,dest_train,date,src_flight,dest_flight,flight_type;
        Intent intent = getIntent();
        src_train = intent.getStringExtra(Constants.INTENT_TRAIN_SRC_STATION_CODE);
        dest_train = intent.getStringExtra(Constants.INTENT_TRAIN_DEST_STATION_CODE);
        date=intent.getStringExtra(Constants.INTENT_TRAIN_DATE);
        src_flight=intent.getStringExtra(Constants.INTENT_FLIGHT_SRC_IATA_CODE);
        dest_flight=intent.getStringExtra(Constants.INTENT_FLIGHT_DEST_IATA_CODE);
        flight_type=intent.getStringExtra(Constants.FLIGHT_TYPE);
        //Log.i(TAG, "oncreate : source ="+src_train + ", dest = "+dest_train + "date = ,"+date);
        fetchTrainsFromNetwork(src_train,dest_train,date);// date : dd-mm-yyyy

        String modDate = date.substring(6)+date.substring(3,5)+date.substring(0,2);  //YYYYMMDD

        fetchFlightsFromNetwork(src_flight,dest_flight,modDate,flight_type);
    }

    private void fetchFlightsFromNetwork(String src, String dest, String date, final String flighttype){
        final ProgressDialog mp=new ProgressDialog(this);
        mp.setIndeterminate(true);
        mp.setMessage(" Loading ");
        mp.show();


        Log.i(TAG, "fetchFlightsFromNetwork: ");
        ApiServiceFlight service= ApiClientFlight.getService();
       // if(flighttype.matches("E"))
        Call<FlightResponse> call=service.searchFlight(Constants.FLIGHT_API_ID,Constants.FLIGHT_API_KEY,
                "json",dest,src,date,date,flighttype,"1","0","0","100");
        call.enqueue(new Callback<FlightResponse>() {
            @Override
            public void onResponse(Call<FlightResponse> call, Response<FlightResponse> response) {
                // FlightResponse.Data data = FlightResponse.getData();
                //ArrayList<FlightResponse.Data.Flight> retflights=data.getreturn();
                Log.i(TAG, response.raw().request().url()+"");
                     if(mp.isShowing())
                         mp.dismiss();
                FlightResponse.Data maindata=response.body().data;
                ArrayList<FlightResponse.Data.Flight> itemlist=maindata.returnflights;
                if(itemlist==null)
                    return;
                else{
                    for(FlightResponse.Data.Flight item :itemlist){
                        Transport t = new Transport();
                        t.type=Constants.TRANSPORT_TYPE_FLIGHT;

                        t.number=item.flightno;
                        t.arrivalTime =item.arrtime;
                        t.departureTime =item.deptime;
                        String stop="";
                        String token[]=item.duration.split("h |m");
                        for(int i=0;i<2;i++){
                            if(token[i].length()==1){
                                token[i]="0"+token[i];
                            }
                        }
                        t.travelTime =token[0]+":"+token[1];
                        t.source=item.origin;
                        t.destination=item.destination;
                        if(item.stops.matches("0")){
                            Log.i(TAG," ehy ");
                            stop="Non-Stop";
                        }
                        else
                            stop=item.stops+"-Stop";

                        t.classes.add(flighttype);
                        t.fares.add(item.fare.grossamount+"");
                        t.fare = item.fare.grossamount+"";
                        t.name=item.airline+"   "+item.carrierid+item.flightcode+" "+stop;
                        Log.i(TAG, "flight: "+t.name);
                        transports.add(t);
                    }
                    Log.i(TAG, "Flight onResponse: "+transports.size());
                    if(transports.size()==0){
                        Toast.makeText(TransportListActivity.this, "No flights found!", Toast.LENGTH_SHORT).show();
                    }
                    transportAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<FlightResponse> call, Throwable t) {

            }
        });
    }

    private void fetchTrainsFromNetwork(String src,String dest,final String date) {
        Log.i(TAG, "fetchTrainsFromNetwork: ");
        //Log.i(TAG, "source ="+src + ", dest = "+dest + "date = ,"+date);
        ApiRailwayService service = ApiClient.getService();
        Call<TrainBetweenStationsResponse> call=service.searchTrainBetween(src,dest,date.substring(0,5),BuildConfig.RailwayApiKey);
        call.enqueue(new Callback<TrainBetweenStationsResponse>() {
            @Override
            public void onResponse(Call<TrainBetweenStationsResponse> call, Response<TrainBetweenStationsResponse> response) {
                Log.i(TAG, response.raw().request().url()+"");
                ArrayList<TrainBetweenStationsResponse.Train> itemList = response.body().train;
                if(itemList == null){
                    return;
                }
                Log.i(TAG, "onResponse: "+itemList.size());
                for(TrainBetweenStationsResponse.Train item : itemList){
                    Transport t = new Transport();
                    t.type=Constants.TRANSPORT_TYPE_TRAIN;
                    t.name=item.name;
                    //Log.i(TAG, t.name);
                    t.number=item.number;
                    t.arrivalTime =item.dest_arrival_time;
                    t.departureTime =item.src_departure_time;
                    t.travelTime =item.travel_time;
                    t.source=item.from.name;
                    t.destination=item.to.name;
                    t.srcCode = item.from.code;
                    t.destCode = item.to.code;
                    t.date = date;
                    for(int i=0;i<item.classes.size();i++){
                        TrainBetweenStationsResponse.Train.Classs c=item.classes.get(i);
                        if(c.available.equals("Y")){
                            t.classes.add(c.name);
                        }
                    }
                    transports.add(t);
                }
                Log.i(TAG, "onResponse: "+transports.size());
                if(transports.size()==0){
                    Toast.makeText(TransportListActivity.this, "No trains found!", Toast.LENGTH_SHORT).show();
                }
                transportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TrainBetweenStationsResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transport_list_menu,menu);
        return true;
    }
    void sortByDuration(){
        Collections.sort(transports, new Comparator<Transport>() {
            @Override
            public int compare(Transport t1, Transport t2) {
                int cmp = t1.travelTime.compareTo(t2.travelTime);
                if(cmp==0)
                    return 0;
                else if(cmp > 0)
                    return 1;
                return -1;
            }
        });
        transportAdapter.notifyDataSetChanged();
    }
    void sortByDepartureTime(){
        Collections.sort(transports, new Comparator<Transport>() {
            @Override
            public int compare(Transport t1, Transport t2) {
                int cmp = t1.departureTime.compareTo(t2.departureTime);
                if(cmp==0)
                    return 0;
                else if(cmp > 0)
                    return 1;
                return -1;
            }
        });
        transportAdapter.notifyDataSetChanged();
    }
    void sortByPrice(){
        Collections.sort(transports, new Comparator<Transport>() {
            @Override
            public int compare(Transport t1, Transport t2) {
                int f1=0,f2=0;
                try {
                    f1 = Integer.parseInt(t1.fare);
                    f2 = Integer.parseInt(t2.fare);
                }catch(NumberFormatException e){

                }
                if(f1 == f2)
                    return 0;
                else if(f1 > f2)
                    return 1;
                return -1;
            }
        });
        transportAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_sort_duration){
            sortByDuration();
            return true;
        }
        else if(id == R.id.menu_sort_price){
            sortByPrice();
            return true;
        }
        else if(id == R.id.menu_sort_time){
            sortByDepartureTime();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
