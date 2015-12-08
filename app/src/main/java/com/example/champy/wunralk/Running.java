package com.example.champy.wunralk;

import android.location.LocationManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

/**
 * Created by kasem on 8/12/2558.
 */
public class Running {
    private GoogleMap mMap;
    private Runtime runtime;
    private LocationManager lm;
    private ArrayList<LatLng> list;
    private ArrayList<Polyline> lists;
    private int count = 0;
    private double lastTime =0;
    private boolean alreadyStart = false;
    private double sumDistance=0;
    private double calories;

    public Running(){
        list = new ArrayList<LatLng>();
    }

}
