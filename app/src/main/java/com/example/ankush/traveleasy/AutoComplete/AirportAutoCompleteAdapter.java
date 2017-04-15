package com.example.ankush.traveleasy.AutoComplete;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.ankush.traveleasy.Api.ApiAirportClient;
import com.example.ankush.traveleasy.Api.ApiAirportService;
import com.example.ankush.traveleasy.ApiResponse.AirportAutoCompleteResponse;
import com.example.ankush.traveleasy.Constants;
import com.example.ankush.traveleasy.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ann Mary George on 24-Jan-17.
 */

public class AirportAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable{
    static final int MAX_RESULTS = 10;
    Context mContext;
    ArrayList<String> mStations;
    final String TAG="AirportAutoTag";

    public AirportAutoCompleteAdapter(Context context,int resource) {
        super(context, resource);
        mContext=context;
        mStations = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.suggestion_dropdown, parent, false);
        }
        Log.i(TAG, "getView: "+mStations.get(position));
        TextView tv=(TextView) convertView.findViewById(R.id.suggestion_textView);
        tv.setText((String)getItem(position));
        return convertView;
    }

    @Override
    public int getCount() {
         Log.i(TAG, "getCount: "+mStations.size());
        return mStations.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mStations.get(position);
    }

    @Override
    public Filter getFilter() {
        Log.i(TAG, "getFilter: ");
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    ArrayList<String> newal=new ArrayList<>();
                    newal = findAirports(mContext, constraint.toString());
//                    mStations.clear();
//                    mStations.addAll(newal);
                    Log.i(TAG, "performFiltering: "+newal.size()+" , "+mStations.size());
                    filterResults.values = mStations;
                    filterResults.count = mStations.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    private ArrayList<String> findAirports(Context context, String station_name) {
        Log.i(TAG, "findAirports: "+station_name);
        final ArrayList<String> retVal = new ArrayList<>();
        ApiAirportService service = ApiAirportClient.getService();
        Call<ArrayList<AirportAutoCompleteResponse>> call=service.getAirportSuggestions(Constants.IATA_API_KEY,station_name,"IN");
        call.enqueue(new Callback<ArrayList<AirportAutoCompleteResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<AirportAutoCompleteResponse>> call, Response<ArrayList<AirportAutoCompleteResponse>> response) {

                Log.i(TAG, response.raw().request().url()+"");
                if(response.body()== null){
                    return;
                }
                int size=response.body().size();
                if(size==0)
                    return;
                mStations.clear();
                Log.i(TAG, "onResponse: "+size);
                for(int i=0;i<size;i++){
                    AirportAutoCompleteResponse st = response.body().get(i);
                    mStations.add(st.value+ ": "+st.label);
                    retVal.add(st.value+ ": "+st.label);
                }
                Log.i(TAG, "onResponse: "+mStations.size());
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<AirportAutoCompleteResponse>> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
            }



        });
        return retVal;
    }
}


