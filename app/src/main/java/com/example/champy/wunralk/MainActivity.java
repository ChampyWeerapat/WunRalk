package com.example.champy.wunralk;

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

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Runtime runtime;
    private LocationManager lm;
    private ArrayList<LatLng> list;
    private ArrayList<Polyline> lists;
    private int count = 0;
    private double lastTime =0;
    private boolean alreadyStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView duration = (TextView) findViewById(R.id.time_duration);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

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

        final ImageButton startBtn = (ImageButton)findViewById(R.id.startbtn);
        final ImageButton stopBtn = (ImageButton)findViewById(R.id.stopbtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),activity_loging.class));
                startBtn.setSelected(!startBtn.isSelected());
                if (startBtn.isSelected()) {
                    runtime.start();
                    startBtn.setBackgroundResource(R.drawable.pause);
                    alreadyStart = true;
                    stopBtn.setImageResource(R.drawable.stop);

                } else {
                    runtime.pause();
                    startBtn.setBackgroundResource(R.drawable.start);
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_SHORT).show();
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
                if (count == 0) {
                    count = 1;
                } else {
                    double longitude = mMap.getMyLocation().getLongitude();
                    double latitude = mMap.getMyLocation().getLatitude();
//               LatLng sydney = new LatLng(latitude, longitude);
//               LatLng sydney = mMap.getCameraPosition().target;
                    LatLng sydney = new LatLng(latitude, longitude);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    PolylineOptions a = new PolylineOptions();

                    if (list.size() == 0) {
                        list.add(sydney);
                        a.add(list.get(0));
                        mMap.addPolyline(a);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));
                    } else {

                        double distance = CalculationByDistance(list.get(list.size() - 1), sydney);
                        String textTime = runtime.getTime();
                        String[] arr = textTime.split(":");
                        double house = Double.parseDouble(arr[0]);
                        double minute = Double.parseDouble(arr[1]);
                        double second = Double.parseDouble(arr[2]);
                        double miliSecond = Double.parseDouble(arr[3]);
                        double time = house*60*60+minute*60+second;
                        double speed = distance/time-lastTime;
                        TextView tSpeed = (TextView) findViewById(R.id.speed);
                        tSpeed.setText(""+speed);
                        if (distance >= 5 && distance <= 100) {
                            list.add(sydney);
                            a.add(list.get(list.size() - 2));
                            a.add(list.get(list.size() - 1));
                            mMap.addPolyline(a);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));

                            TextView textView = (TextView) findViewById(R.id.heart_rate);
                            textView.setText("YES");

                            lastTime = time;
                        } else {
                            TextView textView = (TextView) findViewById(R.id.heart_rate);
                            textView.setText("NO");
                            lastTime = time;

                        }
                    }


                }
            }
        });

        // Add a marker in Sydney and move the camera
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Location location = mMap.getMyLocation();
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);
        Location location = lm.getLastKnownLocation(provider);
        if (location == null){
            Log.d("ddd", "location is null");
        }else{
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng sydney = new LatLng(latitude, longitude);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
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
        Log.d("ddd","" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meter);
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        TextView textView = (TextView) findViewById(R.id.heart_rate);
        textView.setText("" + meter);
        Toast.makeText(this.getBaseContext(), "" + meter, Toast.LENGTH_LONG).show();

//        return Radius * c;
        return meter;
    }


}
