package com.example.ankush.traveleasy.AutoComplete;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.ankush.traveleasy.Api.ApiClient;
import com.example.ankush.traveleasy.Api.ApiRailwayService;
import com.example.ankush.traveleasy.ApiResponse.StationAutoCompleteResponse;
import com.example.ankush.traveleasy.BuildConfig;
import com.example.ankush.traveleasy.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankushbabbar on 21-Jan-17.
 */

public class StationAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    static final int MAX_RESULTS = 10;
    Context mContext;
    ArrayList<String> mStations;
    final String TAG="AutoCompleteAdapterTag";

    public StationAutoCompleteAdapter(Context context,int resource) {
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
       // Log.i(TAG, "getCount: "+mStations.size());
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
                    newal = findStationsFromDatabase(mContext, constraint.toString());
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

    private ArrayList<String> findStationsFromNetwork(Context context, String station_name) {
        final ArrayList<String> retVal = new ArrayList<>();
        ApiRailwayService service = ApiClient.getService();
        Call<StationAutoCompleteResponse> call=service.getStationSuggestions(station_name, BuildConfig.RailwayApiKey);
        call.enqueue(new Callback<StationAutoCompleteResponse>() {
            @Override
            public void onResponse(Call<StationAutoCompleteResponse> call, Response<StationAutoCompleteResponse> response) {
                Log.i(TAG, response.raw().request().url()+"");
                ArrayList<StationAutoCompleteResponse.Station> itemList;
                if(response.body()!= null)
                    itemList = response.body().station;
                else
                    itemList = null;
                if(itemList == null){
                    return;
                }
                Log.i(TAG, "onResponse: "+itemList.size());
                mStations.clear();
                for(StationAutoCompleteResponse.Station item : itemList){
                   // Log.i(TAG, "onResponse: "+item.code + " : "+ item.name);
                    mStations.add(item.code + ": "+item.name);
                    retVal.add(item.code + ": "+item.name);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<StationAutoCompleteResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: ");
            }
        });
        return retVal;
    }
    private ArrayList<String> findStationsFromDatabase(Context context, String station_name) {
        Log.i(TAG, "findStationsFromDatabase: called");
        Log.i(TAG, station_name);
        final ArrayList<String> retVal = new ArrayList<>();
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM station " +
                "WHERE name like '%" +station_name+"%'",null);
        Log.i(TAG, "count " + c.getCount());
        mStations.clear();
        while(c.moveToNext()){
            String name = c.getString(c.getColumnIndex("name"));
            String code = c.getString(c.getColumnIndex("code"));
            Log.i(TAG, "findStationsFromDatabase: "+name);
            mStations.add(code + ": "+name);
            retVal.add(code + ": "+name);
        }
        return retVal;
    }
}
