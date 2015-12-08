package com.example.champy.wunralk;

import android.location.LocationManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

/**
 * Created by kasem on 8/12/2558.
 */
public class Running {
    private GoogleMap mMap;
    private LocationManager lm;
    private ArrayList<LatLng> list;
    private ArrayList<Polyline> lists;
    private int count = 0;
    private double lastTime =0;
    private ManageDB db;

    public Running(){
        list = new ArrayList<LatLng>();
    }

}
