package com.example.ankush.traveleasy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankush.traveleasy.AutoComplete.AirportAutoCompleteAdapter;
import com.example.ankush.traveleasy.AutoComplete.DelayAutoCompleteTextView;
import com.example.ankush.traveleasy.AutoComplete.StationAutoCompleteAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText dateEditText;
    Button searchButton;
    Calendar myCalendar;
    AutoCompleteTextView srcAutoTvTrain;
    AutoCompleteTextView destAutoTvTrain;
    DelayAutoCompleteTextView flightSrcEditText;
    DelayAutoCompleteTextView flightDestEditText;
    StationAutoCompleteAdapter s;
    boolean isDateSet = false;
    String TAG="MainActivityTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        findViews();
        myCalendar = Calendar.getInstance();
        srcAutoTvTrain.setThreshold(3);
        s = new StationAutoCompleteAdapter(this,R.layout.suggestion_dropdown);
        srcAutoTvTrain.setAdapter(s);
//        srcAutoTvTrain.setLoadingIndicator(
//                (android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));
        srcAutoTvTrain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String val= (String) adapterView.getItemAtPosition(position);
                String stnCode= val.split(":")[0];
                srcAutoTvTrain.setText(stnCode);
            }
        });
        destAutoTvTrain.setThreshold(3);
        destAutoTvTrain.setAdapter(new StationAutoCompleteAdapter(this,R.layout.suggestion_dropdown));
        destAutoTvTrain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String val= (String) adapterView.getItemAtPosition(position);
                String stnCode= val.split(":")[0];
                destAutoTvTrain.setText(stnCode);
            }
        });
        flightSrcEditText.setThreshold(1);
        flightSrcEditText.setAdapter(new AirportAutoCompleteAdapter(this,R.layout.suggestion_dropdown));
        flightSrcEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String val= (String) adapterView.getItemAtPosition(position);
                String stnCode= val.split(":")[0];
                flightSrcEditText.setText(stnCode);
            }
        });
        flightDestEditText.setThreshold(3);
        flightDestEditText.setAdapter(new AirportAutoCompleteAdapter(this,R.layout.suggestion_dropdown));
        flightDestEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String val= (String) adapterView.getItemAtPosition(position);
                String stnCode= val.split(":")[0];
                flightDestEditText.setText(stnCode);
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        flightDestEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        destAutoTvTrain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSearch();
            }
        });
    }
    private void findViews(){
        dateEditText = (EditText)findViewById(R.id.edittext_date);
        searchButton= (Button)findViewById(R.id.button_search);
        flightSrcEditText= (DelayAutoCompleteTextView) findViewById(R.id.from_auto_textbox_flight);
        flightDestEditText=(DelayAutoCompleteTextView) findViewById(R.id.to_auto_textbox_flight);
        srcAutoTvTrain = (AutoCompleteTextView) findViewById(R.id.from_auto_textbox_train);
        destAutoTvTrain = (AutoCompleteTextView)findViewById(R.id.to_auto_textbox_train);

    }
    private void doSearch(){
        String src_train= srcAutoTvTrain.getText().toString();
        String dest_train= destAutoTvTrain.getText().toString();
        String date=dateEditText.getText().toString();
        String src_flight = flightSrcEditText.getText().toString();
        String dest_flight = flightDestEditText.getText().toString();

        Intent intent = new Intent();
        intent.setClass(MainActivity.this,TransportListActivity.class);
        intent.putExtra(Constants.INTENT_TRAIN_SRC_STATION_CODE,src_train);
        intent.putExtra(Constants.INTENT_TRAIN_DEST_STATION_CODE,dest_train);
        intent.putExtra(Constants.INTENT_TRAIN_DATE,date);

        intent.putExtra(Constants.INTENT_FLIGHT_SRC_IATA_CODE,src_flight);
        intent.putExtra(Constants.INTENT_FLIGHT_DEST_IATA_CODE,dest_flight);
        if(date.length() == 0 || src_flight.length() == 0 || dest_flight.length() == 0
                || src_train.length()==0 || dest_train.length() == 0){
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }
    private void updateLabel() {

        String myFormat = "dd-MM";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
