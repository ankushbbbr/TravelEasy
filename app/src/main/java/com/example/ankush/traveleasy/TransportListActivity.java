package com.example.ankush.traveleasy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ankush.traveleasy.Api.ApiClient;
import com.example.ankush.traveleasy.Api.ApiClientFlight;
import com.example.ankush.traveleasy.Api.ApiRailwayService;
import com.example.ankush.traveleasy.Api.ApiServiceFlight;
import com.example.ankush.traveleasy.ApiResponse.FlightResponse;
import com.example.ankush.traveleasy.ApiResponse.TrainBetweenStationsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Transport> transports;
    TransportAdapter transportAdapter;
    final String TAG="TransportListTag";
    Button button_sort_duration, button_sort_departure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_list);
        listView=(ListView) findViewById(R.id.results_listview);

        transports=new ArrayList<>();
        transportAdapter = new TransportAdapter(this,transports);
        listView.setAdapter(transportAdapter);

        String src_train,dest_train,date,src_flight,dest_flight;
        Intent intent = getIntent();
        src_train = intent.getStringExtra(Constants.INTENT_TRAIN_SRC_STATION_CODE);
        dest_train = intent.getStringExtra(Constants.INTENT_TRAIN_DEST_STATION_CODE);
        date=intent.getStringExtra(Constants.INTENT_TRAIN_DATE);
        src_flight=intent.getStringExtra(Constants.INTENT_FLIGHT_SRC_IATA_CODE);
        dest_flight=intent.getStringExtra(Constants.INTENT_FLIGHT_DEST_IATA_CODE);

        Log.i(TAG, "oncreate : source ="+src_train + ", dest = "+dest_train + "date = ,"+date);
        
        fetchTrainsFromNetwork(src_train,dest_train,date);

        String moddate="2017"+date.charAt(3)+date.charAt(4)+date.charAt(0)+date.charAt(1);  //YYYYMMDD

        fetchFlightsFromNetwork(src_flight,dest_flight,moddate);

        button_sort_duration = (Button)findViewById(R.id.button_sort_duration);
        button_sort_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(transports, new Comparator<Transport>() {
                    @Override
                    public int compare(Transport t1, Transport t2) {
                        int cmp = t1.travel_time.compareTo(t2.travel_time);
                        if(cmp==0)
                            return 0;
                        else if(cmp > 0)
                            return 1;
                        return -1;
                    }
                });
                transportAdapter.notifyDataSetChanged();
            }
        });
        button_sort_departure = (Button)findViewById(R.id.button_sort_departure);
        button_sort_departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(transports, new Comparator<Transport>() {
                    @Override
                    public int compare(Transport t1, Transport t2) {
                        int cmp = t1.departure_time.compareTo(t2.departure_time);
                        if(cmp==0)
                            return 0;
                        else if(cmp > 0)
                            return 1;
                        return -1;
                    }
                });
                transportAdapter.notifyDataSetChanged();
            }
        });
    }

    private void fetchFlightsFromNetwork(String src,String dest,String date){
        final ProgressDialog mp=new ProgressDialog(this);
        mp.setIndeterminate(true);
        mp.setMessage(" Loading ");
        mp.show();


        Log.i(TAG, "fetchFlightsFromNetwork: ");
        ApiServiceFlight service= ApiClientFlight.getService();
        Call<FlightResponse> call=service.searchFlight(Constants.FLIGHT_API_ID,Constants.FLIGHT_API_KEY,
                "json",dest,src,date,date,"E","1","0","0","100");
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
                        t.name=item.FlHash;
                        t.number=item.flightno;
                        t.arrival_time=item.arrtime;
                        t.departure_time=item.deptime;

                        String token[]=item.duration.split("h |m");
                        for(int i=0;i<2;i++){
                            if(token[i].length()==1){
                                token[i]="0"+token[i];
                            }
                        }
                        t.travel_time=token[0]+":"+token[1];
                        t.source=item.origin;
                        t.destination=item.destination;
                       /* for(int i=0;i<item.size();i++){
                            TrainBetweenStationsResponse.Train.Classs c=item.classes.get(i);
                            if(c.available.equals("Y")){
                                t.classes.add(c.name);
                            }
                        }*/
                        t.classes.add("E");
                        t.fare.add((float) item.fare.grossamount);
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

    private void fetchTrainsFromNetwork(String src,String dest,String date) {
        Log.i(TAG, "fetchTrainsFromNetwork: ");
        //Log.i(TAG, "source ="+src + ", dest = "+dest + "date = ,"+date);
        ApiRailwayService service = ApiClient.getService();
        Call<TrainBetweenStationsResponse> call=service.searchTrainBetween(src,dest,date,BuildConfig.RailwayApiKey);
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
                    t.arrival_time=item.dest_arrival_time;
                    t.departure_time=item.src_departure_time;
                    t.travel_time=item.travel_time;
                    t.source=item.from.name;
                    t.destination=item.to.name;
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
}
