package com.example.ankush.traveleasy;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
        Log.i(TAG, "TransportAdapter: constructor");
        Log.i(TAG, "TransportAdapter: "+transports.size());
        mContext=context;
        mTransports=transports;
    }
    public static class ViewHolder{
        TextView nameTextView;
        TextView timeTextView;
        TextView durationTextView;
        ImageView iconImageView;
        TextView priceTextView;
        ViewHolder(TextView nameTextView, TextView timeTextView, TextView durationTextView,
                   TextView priceTextView, ImageView iconImageView){
            this.nameTextView=nameTextView;
            this.timeTextView=timeTextView;
            this.durationTextView=durationTextView;
            this.priceTextView=priceTextView;
            this.iconImageView=iconImageView;
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView: "+mTransports.size());
        if(convertView==null){
            convertView= View.inflate(mContext,R.layout.list_item_layout,null);
            TextView nameTextView=(TextView) convertView.findViewById(R.id.transport_name);
            TextView timeTextView=(TextView) convertView.findViewById(R.id.transport_time);
            TextView priceTextView=(TextView) convertView.findViewById(R.id.transport_price);
            TextView durationTextView=(TextView) convertView.findViewById(R.id.transport_duration);
            ImageView iconImageView=(ImageView) convertView.findViewById(R.id.transport_icon);

            ViewHolder vh = new ViewHolder(nameTextView,timeTextView,durationTextView,priceTextView
            ,iconImageView);
            convertView.setTag(vh);
        }
        Transport curr= mTransports.get(position);
        ViewHolder vh=(ViewHolder)convertView.getTag();
        vh.nameTextView.setText(curr.name);
        vh.timeTextView.setText(curr.departureTime + " - " + curr.arrivalTime);
        vh.durationTextView.setText(curr.travelTime);
        vh.priceTextView.setText("Price = \n₹ " + curr.averageFare);
        if(curr.type == Constants.TRANSPORT_TYPE_TRAIN)
            vh.iconImageView.setImageResource(R.drawable.train_logo);
        else if(curr.type == Constants.TRANSPORT_TYPE_FLIGHT)
            vh.iconImageView.setImageResource(R.drawable.flight_logo);

        return convertView;
    }
}
