package com.example.champy.wunralk;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Runtime runtime;
    private LocationManager lm;
    private ArrayList<LatLng> list;
    private ArrayList<Polyline> lists;
    private int count = 0;
    private double lastTime =0;
    private boolean alreadyStart = false;
    private ManageDB db;
    private TextView tSpeed,tCal,tAvg,tvHeart_rate,tvDistance;
    private ImageButton btStop,startBtn;
    private double sumDistance=0;
    private double calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView duration = (TextView) findViewById(R.id.time_duration);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tSpeed = (TextView) findViewById(R.id.speed);
        tCal = (TextView) findViewById(R.id.calories);
        tAvg = (TextView) findViewById(R.id.avg_speed);
        tvDistance = (TextView) findViewById(R.id.distance);
        btStop = (ImageButton) findViewById(R.id.stopbtn);
        startBtn = (ImageButton)findViewById(R.id.startbtn);
//        tvHeart_rate = (TextView) findViewById(R.id.heart_rate);

        SQLiteDatabase mydatabase = openOrCreateDatabase("wunralk",MODE_PRIVATE,null);
        try {
            db = ManageDB.getInstance(mydatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.createTable("history","username varchar(16),dateTime datetime,place varchar(50),distance double,time time,calories double");

//        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS member(Username VARCHAR,Password VARCHAR);");
        mydatabase.execSQL("INSERT INTO history VALUES('wunralker1','2558-12-8 18:00:00','ku',4.5,'0:21:30',200);");

//        Cursor resultSet = mydatabase.rawQuery("Select * from history", null);
//        Log.d("ddd",""+resultSet.moveToFirst());
//        String username = resultSet.getString(0);
//        tAvg.setText(""+username);

        //set TabHost
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("workout");
        tabSpec.setContent(R.id.workOut);
        tabSpec.setIndicator("WorkOut");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("maps");
        tabSpec.setContent(R.id.maps);
        tabSpec.setIndicator("Maps");
        tabHost.addTab(tabSpec);

        runtime = new Runtime(duration);
//        Timer
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        list = new ArrayList<LatLng>();

        startBtn = (ImageButton)findViewById(R.id.startbtn);
        btStop = (ImageButton)findViewById(R.id.stopbtn);
        btStop.setEnabled(false);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),activity_loging.class));
                startBtn.setSelected(!startBtn.isSelected());
                if (startBtn.isSelected()) {
                    runtime.start();
                    startBtn.setBackgroundResource(R.drawable.pause);
                    alreadyStart = true;

                } else {
                    runtime.pause();
                    startBtn.setBackgroundResource(R.drawable.start);
                    btStop.setImageResource(R.drawable.stop);
                    btStop.setEnabled(true);
                    alreadyStart = false;
                }
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onClickStop();
                
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                onMapChange();
            }
        });

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);
        Location location = lm.getLastKnownLocation(provider);
        if (location == null){
            Log.d("ddd", "location is null");
        }else{
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng sydney = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17.0f));
            Log.d("ddd", "longitude is "+longitude);
            Log.d("ddd","latitude is "+latitude);
        }
        if (mMap == null){
            Log.d("ddd","mMap is null");
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult * 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d("ddd", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meter);
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return meter;
    }

    public void onMapChange(){
        if (count == 0) {
            count = 1;
        } else {
            double longitude = mMap.getMyLocation().getLongitude();
            double latitude = mMap.getMyLocation().getLatitude();
            LatLng sydney = new LatLng(latitude, longitude);
            PolylineOptions a = new PolylineOptions();

            if (list.size() == 0) {
                list.add(sydney);
                a.add(list.get(0));
                mMap.addPolyline(a);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));
            } else {
                double distance = CalculationByDistance(list.get(list.size() - 1), sydney);
                double time = getTime();
                double speed = distance/(time-lastTime);
                tSpeed.setText(String.format("%.2f",speed));
                if (distance >= 5 && distance <= 100) {
                    sumDistance += distance;
                    tvDistance.setText(String.format("%.2f m", sumDistance));
                    list.add(sydney);
                    a.add(list.get(list.size() - 2));
                    a.add(list.get(list.size() - 1));
                    mMap.addPolyline(a);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));
                    calories = 160*0.63*(sumDistance/1000*0.62);
                    tCal.setText(String.format("%.2f",calories));
//                            tvHeart_rate.setText("YES");
                    lastTime = time;
                } else {
//                            tvHeart_rate.setText("NO");
                    lastTime = time;

                }
            }
        }
    }

    public double getTime(){
        String textTime = runtime.getTime();
        String[] arr = textTime.split(":");
        double house = Double.parseDouble(arr[0]);
        double minute = Double.parseDouble(arr[1]);
        double second = Double.parseDouble(arr[2]);
        double time = house*60*60+minute*60+second;
        return  time;
    }

    public String getStrDateNow(){
        Date time = Calendar.getInstance().getTime();
        int year = time.getYear()+1900;
        int month = time.getMonth();
        int day = time.getDate();
        int hour = time.getHours();
        int minute = time.getMinutes();
        int second = time.getSeconds();
        return String.format("%s-%s-%s %s:%s:%s",year,month,day,hour,minute,second);
    }

    public void onClickStop(){
//        mydatabase.execSQL("INSERT INTO history VALUES('wunralker','2558-12-8 18:00:00','ku',4.5,'0:21:30',200);");
        Log.d("ddd", activity_loging.getUsername());
        Log.d("ddd", getStrDateNow());
        Log.d("ddd", sumDistance + "");
        Log.d("ddd", runtime.getTime().substring(0, 8));
        Log.d("ddd", calories+"");
        db.addEvent(activity_loging.getUsername(), getStrDateNow(), "KU", sumDistance, runtime.getTime().substring(0, 8), calories);
        try {
            int a = db.readDB("history").size();

            Toast.makeText(getApplicationContext(), "history "+a , Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sumDistance = 0;
        list.clear();
        lastTime=0;
        count=0;
        runtime.reset();
        tCal.setText("0.0");
        tSpeed.setText("0.0");
        tvDistance.setText("0.0");
        calories = 0;
    }
}
