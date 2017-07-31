package com.example.paul.assignment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class Dashboard extends AppCompatActivity implements LocationListener {

    TextView webpageText;
    private LocationManager locationManager;
    private static Location location;
    WebView webView1;

    DBManager db = new DBManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpLocation();

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        setContentView(R.layout.activity_dashboard);

        webView1 = (WebView)findViewById(R.id.webView);
        webView1.setWebViewClient(new MyBrowser()); //creates a browser for my webview

        webpageText = (TextView) findViewById(R.id.webPageText);

        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ImageButton addButton= (ImageButton) findViewById(R.id.addLessonButtonView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Add a lesson...", Toast.LENGTH_SHORT).show();
                Intent switchscreen;
                switchscreen = new Intent(Dashboard.this, AddLesson.class);
                switchscreen.putExtra("CODE", 5);
                switchscreen.putExtra("class", 0);
                startActivityForResult(switchscreen, 5); //calls the addlesson activity expecting a result
            }
        });

        ImageButton removeButton= (ImageButton) findViewById(R.id.removeLessonButtonView);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Remove a lesson...", Toast.LENGTH_SHORT).show();
                Intent switchscreen;
                switchscreen = new Intent(Dashboard.this, ShowLessons.class);
                switchscreen.putExtra("CODE", 6);
                startActivityForResult(switchscreen, 6);
            }
        });

        ImageButton updateButton= (ImageButton) findViewById(R.id.updateLessonButtonView);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Update a lesson...", Toast.LENGTH_SHORT).show();
                Intent switchscreen;
                switchscreen = new Intent(Dashboard.this, ShowLessons.class);
                switchscreen.putExtra("CODE", 7);
                startActivity(switchscreen);
            }
        });

        int callingActivity= getIntent().getIntExtra("callingActivity", 0); // gets the calling activity
        if(callingActivity ==10){
            Bundle extras = getIntent().getExtras(); //stores all of the extra data sent back

            int _id = extras.getInt("ID"); //gets the int called ID from the bundle
            String name = extras.getString("NAME");
            String location = extras.getString("LOCATION");
            String dayOfWeek = extras.getString("DAY");
            String classType = extras.getString("CLASSTYPE");
            int startTime = extras.getInt("STARTHOUR");
            int endTime = extras.getInt("ENDHOUR");

            boolean b= db.updateEverything(_id, name, location, dayOfWeek, classType, startTime, endTime); //updates the info with the new info sent back

            if(b){
                Toast.makeText(this, "Successfully updated class: "+ name, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Something went wrong updating class: "+ name, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); //loads the scecified url
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) //if it was startActivityForResult, the info will be sent to here
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode ==RESULT_OK)
        {
            Bundle extras = data.getExtras();

            if(requestCode==6) {

                String classpicked = extras.getString("class");
                String classpickedName = extras.getString("className");
                if(classpicked!=null) {
                    int _id = Integer.parseInt(classpicked);

                    if (db.removeById(_id)) {
                        Toast.makeText(Dashboard.this, "Removed " + classpickedName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Dashboard.this, "Something went wrong! Unable to remove "+ classpickedName, Toast.LENGTH_SHORT).show();
                    }
                }

            } else if(requestCode==7)
            {
                String classpicked = extras.getString("class");
                String classpickedName = extras.getString("className");
                if(classpicked!=null) {
                    int _id = Integer.parseInt(classpicked);

                }

            } else if(requestCode==5)
            {

                String name = extras.getString("NAME");
                String location = extras.getString("LOCATION");
                String dayOfWeek = extras.getString("DAY");
                String classType = extras.getString("CLASSTYPE");
                int startHour = extras.getInt("STARTHOUR");
                int startMin = extras.getInt("STARTMIN");
                int endHour = extras.getInt("ENDHOUR");
                int endMin = extras.getInt("ENDMIN");

                Toast.makeText(this, "Start time returned is " + startHour + ":" + startMin + " ", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "End time returned is " + endHour + ":" + endMin + " ", Toast.LENGTH_SHORT).show();


                try {
                    db.insertSomething(name, location, dayOfWeek, classType, startHour, endHour);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }else { //if nothing sent back
            Toast.makeText(this, "Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        String latestLocation= "";
        if(location!=null){
            latestLocation=String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
        }
        Toast.makeText(Dashboard.this, latestLocation, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void setUpLocation()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 5, this);
    }


    protected void onResume(){
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 5, this);
    }

    protected void onPause(){
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public static Location getLocation(){
        return location;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //menu item clicks
        int id = item.getItemId();
        if(id == R.id.action_lessons){
            Toast.makeText(this, "Clicked Lessons", Toast.LENGTH_SHORT).show();
            Intent switchscreen;
            switchscreen = new Intent(Dashboard.this, ShowLessons.class);
            switchscreen.putExtra("CODE", 1);
            startActivityForResult(switchscreen, 1);
            return true;
        }

        if(id == R.id.action_taskApp){

            Toast.makeText(this, "Loading asana.com...\n Scroll down to view!", Toast.LENGTH_LONG).show();

            String url = "https://asana.com/"; //sets the url

            ////i refenrenced the following from different sources online to allow for the smoothest webview possible
            webView1.getSettings().setLoadsImagesAutomatically(true); //loads images automatically
            webView1.getSettings().setJavaScriptEnabled(true); //enables java script
            webView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //sets scollbars
            ///
            webView1.loadUrl(url); //load the webpage

            webpageText.setText("Asana.com");

            return true;
        }

        if(id == R.id.action_Map){
            Intent switchscreen = new Intent(Dashboard.this, MapsActivity.class);
            startActivity(switchscreen);

            Toast.makeText(this, "Never Miss A Lesson!", Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
