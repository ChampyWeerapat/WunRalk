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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Runtime runtime;
    private int count = 0;

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final ImageButton clickHereBtn = (ImageButton)findViewById(R.id.startstopbtn);
        clickHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),activity_loging.class));
                clickHereBtn.setSelected(!clickHereBtn.isSelected());
                if (clickHereBtn.isSelected()) {
                    runtime.start();
                    clickHereBtn.setBackgroundResource(R.drawable.stop);

                } else {
                    runtime.pause();
                    clickHereBtn.setBackgroundResource(R.drawable.start);
                }
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
                TextView speed = (TextView) findViewById(R.id.speed);
                speed.setText(""+ ++count);
            }
        });
        // Add a marker in Sydney and move the camera
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f));
            Log.d("ddd", "longitude is "+longitude);
            Log.d("ddd","latitude is "+latitude);
        }
        if (mMap == null){
            Log.d("ddd","mMap is null");
        }
    }
}
