package com.example.paul.assignment;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        location= Dashboard.getLocation(); //gets current location
        if(location ==null){
            location = new Location("dummyprovider");
            location.setLatitude(53.337352);
            location.setLongitude(-6.267348);
        }
        setUpMapIfNeeded();

        final Location loc1 = new Location("dummyprovider"); //make a new location
        loc1.setLatitude(0);
        loc1.setLongitude(0);
        mMap.addMarker(new MarkerOptions().position(new LatLng(loc1.getLatitude(), loc1.getLongitude())).title("0 , 0")); //set a marker for location

        final Location loc2 = new Location("dummyprovider");
        loc2.setLatitude(53.337352 );
        loc2.setLongitude(-6.267348);
        mMap.addMarker(new MarkerOptions().position(new LatLng(loc2.getLatitude(), loc2.getLongitude())).title("DIT Kevin St."));

        final RadioGroup radioButtonGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int index = radioButtonGroup.indexOfChild(radioButton); //find index inside radiogroup for certain radiobutton clicked

                if(index ==0){
                    zoomToLocation(location, 17 , 90 ,30);
                }
                else if(index== 2){
                    zoomToLocation(loc1, 0 , 0, 0);
                }
                else if(index== 4) {
                    zoomToLocation(loc2 ,17 , -90 ,30);
                }
                else if(index== 6) {
                    zoomToLocation(location, 10, 0, 0);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Current Location"));
    }

    private void zoomToLocation(Location location, int zoom, int bearing, int tilt){
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude())) // Set where to zoom to
                    .zoom(zoom)  // zooms in to a certain height level 1-20
                    .bearing(bearing) // works like a compass so 90 will be 90 degrees to the right etc.
                    .tilt(tilt) // Tilt of the view by a certain degree
                    .build();  // make it
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); //animated the camera

        }
    }
}
