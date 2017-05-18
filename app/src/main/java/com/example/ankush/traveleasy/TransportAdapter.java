package com.example.ankush.traveleasy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankush.traveleasy.Api.ApiClient;
import com.example.ankush.traveleasy.Api.ApiRailwayService;
import com.example.ankush.traveleasy.ApiResponse.TrainFareResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ankushbabbar on 03-Sep-16.
 */
public class TransportAdapter extends ArrayAdapter<Transport>
{
    String TAG="AdapterTag";
    Context mContext;
    ArrayList<Transport> mTransports;
    public TransportAdapter(Context context, ArrayList<Transport> transports) {
        super(context, 0, transports);//0 bcoz we are not passing id
        mContext=context;
        mTransports=transports;
    }
    public static class ViewHolder{
        TextView nameTextView;
        TextView timeTextView;
        TextView durationTextView;
        ImageView iconImageView;
        TextView priceTextView;
        LinearLayout fareLayout;
        ViewHolder(TextView nameTextView, TextView timeTextView, TextView durationTextView,
                   TextView priceTextView, ImageView iconImageView, LinearLayout fareLayout){
            this.nameTextView=nameTextView;
            this.timeTextView=timeTextView;
            this.durationTextView=durationTextView;
            this.priceTextView=priceTextView;
            this.iconImageView=iconImageView;
            this.fareLayout = fareLayout;
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Log.i(TAG, "getView: "+mTransports.size());
        if(convertView==null){
            convertView= View.inflate(mContext,R.layout.list_item_layout,null);
            TextView nameTextView=(TextView) convertView.findViewById(R.id.transport_name);
            TextView timeTextView=(TextView) convertView.findViewById(R.id.transport_time);
            TextView priceTextView=(TextView) convertView.findViewById(R.id.transport_price);
            TextView durationTextView=(TextView) convertView.findViewById(R.id.transport_duration);
            ImageView iconImageView=(ImageView) convertView.findViewById(R.id.transport_icon);
            LinearLayout fareLayout = (LinearLayout)convertView.findViewById(R.id.fare_linear_layout);
            ViewHolder vh = new ViewHolder(nameTextView,timeTextView,durationTextView,priceTextView
            ,iconImageView,fareLayout);
            convertView.setTag(vh);
        }
        Transport curr= mTransports.get(position);
        final ViewHolder vh=(ViewHolder)convertView.getTag();
        vh.nameTextView.setText(curr.name);
        vh.timeTextView.setText(curr.departureTime + " - " + curr.arrivalTime);
        vh.durationTextView.setText(curr.travelTime);
        vh.priceTextView.setText("Price = \n₹ " + curr.fare);
        if(curr.type == Constants.TRANSPORT_TYPE_TRAIN)
            vh.iconImageView.setImageResource(R.drawable.train_logo);
        else
            vh.iconImageView.setImageResource(R.drawable.flight_logo);

        if(curr.type == Constants.TRANSPORT_TYPE_TRAIN){
            if(curr.fares.size() == 0) {
                Button getFareButton = new Button(mContext);
                getFareButton.setText("GET\nFARE");
                vh.fareLayout.removeAllViews();
                vh.fareLayout.addView(getFareButton);
                getFareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiRailwayService service = ApiClient.getService();
                        Transport train = mTransports.get(position);
                        Call<TrainFareResponse> call = service.getTrainFare(train.number, train.srcCode, train.destCode,
                                train.date, BuildConfig.RailwayApiKey);
                        call.enqueue(new Callback<TrainFareResponse>() {
                            @Override
                            public void onResponse(Call<TrainFareResponse> call, Response<TrainFareResponse> response) {
                                ArrayList<TrainFareResponse.Fare> itemList = response.body().fares;
                                if(itemList == null)
                                    return;
                                mTransports.get(position).classes.clear();
                                mTransports.get(position).fares.clear();
                                for (TrainFareResponse.Fare f : itemList) {
                                    mTransports.get(position).classes.add(f.classCode);
                                    mTransports.get(position).fares.add(f.fare);
                                    //Log.i("TrainFareTag", "onResponse: " + f.classCode + "  " + f.fare);
                                }
                                selectClassFromDialog(mTransports.get(position),vh.fareLayout);
                            }

                            @Override
                            public void onFailure(Call<TrainFareResponse> call, Throwable t) {

                            }
                        });
                    }
                });
            }
            else{
                setUpFareTextView(vh.fareLayout,curr.fare);
            }
        }
        else {
            if (curr.flag == false) {
                Button getFareButton = new Button(mContext);
                getFareButton.setText("GET\nFARE");
                vh.fareLayout.removeAllViews();
                vh.fareLayout.addView(getFareButton);
                getFareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Transport flight = mTransports.get(position);
//                          mTransports.get(position).fares.add(flight.fare);
                        setUpFareTextView(vh.fareLayout, flight.fare);
                        mTransports.get(position).flag = true;
                    }
                });

            }
            else
                setUpFareTextView(vh.fareLayout,curr.fare);
        }
        return convertView;
    }
    public void selectClassFromDialog(final Transport train, final LinearLayout fareLayout){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Class");
        View dialogView = LayoutInflater.from(mContext).
                inflate(R.layout.train_fare_dialog,null, false);
        builder.setView(dialogView);
        final RadioGroup radioGroup = (RadioGroup)dialogView.findViewById(R.id.radio_group_train_class);
        Log.i(TAG, "selectClassFromDialog: "+train.classes.size());
        for (int i = 0; i < train.classes.size(); i++) {
            RadioButton rdButton = new RadioButton(mContext);
            rdButton.setId(i);
            rdButton.setText(train.classes.get(i));
            radioGroup.addView(rdButton);
            Log.i(TAG, "selectClassFromDialog: "+train.classes.get(i));
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = radioGroup.getCheckedRadioButtonId();
                if(id == -1){
                    Toast.makeText(mContext, "Please select class", Toast.LENGTH_SHORT).show();
                }else {
                    train.fare = train.fares.get(id);
                    setUpFareTextView(fareLayout, train.fare);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    void setUpFareTextView(LinearLayout layout,String fare){
        layout.removeAllViews();
        TextView fareTextView = new TextView(mContext);
        layout.addView(fareTextView);
        fareTextView.setText("Price = \n₹ " + fare);
        fareTextView.setTextSize(20);
    }
}
