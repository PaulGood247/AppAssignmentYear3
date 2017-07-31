package com.example.paul.assignment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddLesson extends AppCompatActivity {

    Calendar startTime=Calendar.getInstance();
    Calendar endTime =Calendar.getInstance();

    String name;
    String location;
    String dayOfWeek;
    String classStartTime;
    String classEndTime;
    String classType;
    String classId;

    Intent intent;
    int callingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        callingActivity = getIntent().getIntExtra("callingActivity", 0);

        Button addButton = (Button) findViewById(R.id.addButtonView);
        Button startTimeButton = (Button) findViewById(R.id.addStartTimeButtonView);
        Button endTimeButton = (Button) findViewById(R.id.addEndTimeButtonView);
        final AutoCompleteTextView enterName = (AutoCompleteTextView) findViewById(R.id.enterNameEditText);
        final AutoCompleteTextView enterLocation = (AutoCompleteTextView) findViewById(R.id.enterLocationEditText);
        final Spinner dayOfWeekSpinner = (Spinner) findViewById(R.id.daysOfWeekSpinner);
        final Spinner classTypeSpinner = (Spinner) findViewById(R.id.classTypeSpinner);
        final TextView startTimeText = (TextView) findViewById(R.id.endTimeTextView);
        final TextView endTimeText = (TextView) findViewById(R.id.startTimeTextView);

        DBManager db = new DBManager(this);

        Cursor mCursor = db.getAll();
        mCursor.moveToFirst();
        ArrayList<String> classNames = new ArrayList<>();
        ArrayList<String> classLocations = new ArrayList<>();
        while (!mCursor.isAfterLast()) {

            classNames.add(mCursor.getString(mCursor.getColumnIndex("className"))); //add class names to array from cursor
            classLocations.add(mCursor.getString(mCursor.getColumnIndex("classLocation")));

            for(int i=0; i< classNames.size()-1; i++){
                if(classNames.get(i).equals(classNames.get(i+1))){
                    classNames.remove(i);
                }else if(classLocations.get(i).equals(classLocations.get(i+1))){
                    classLocations.remove(i);
                } //allow only one instance of a certain class
            }

            mCursor.moveToNext();
        }
        mCursor.close();

        ArrayAdapter<String> adapterName = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, classNames);
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, classLocations);
        enterName.setAdapter(adapterName);
        enterLocation.setAdapter(adapterLocation); //setup the array for autocomplete edittexts

        if(getIntent().getStringExtra("class") != null){
            classId = getIntent().getStringExtra("class"); //find out which class is calling
         }else{
            classId="0";
        }

        final int _id = Integer.parseInt(classId);

            if(_id != 0) {
                Cursor myCursor=null;
                try {
                     myCursor= db.getSomething(_id); //get a certain row from database
                    myCursor.moveToFirst();
                } catch (Exception e) {
                    Log.i("SQL Error", "Theres an sql error: "+e);
                }
                if (myCursor != null) {
                    name = myCursor.getString(myCursor.getColumnIndex("className"));
                    location = myCursor.getString(myCursor.getColumnIndex("classLocation"));
                    dayOfWeek = myCursor.getString(myCursor.getColumnIndex("classDayOfWeek"));
                    classStartTime = myCursor.getString(myCursor.getColumnIndex("classStartTime"));
                    classEndTime = myCursor.getString(myCursor.getColumnIndex("classEndTime"));
                    classType = myCursor.getString(myCursor.getColumnIndex("classType"));

                    enterName.setText(name);
                    enterLocation.setText(location);

                    ArrayList<String> list=new ArrayList<>( Arrays.asList(getResources().getStringArray(R.array.daysOfWeek)) );
                    int pos= list.indexOf(dayOfWeek);
                    dayOfWeekSpinner.setSelection(pos);

                    ArrayList<String> list2=new ArrayList<>( Arrays.asList(getResources().getStringArray(R.array.classTypes)) );
                    int pos2= list2.indexOf(classType);
                    classTypeSpinner.setSelection(pos2);

                    startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(classStartTime));
                    endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(classEndTime));
                } //the above gets everything from the cursor for the update or remove and set the fields to them
            }

        if(callingActivity ==10){
            addButton.setText("Update Class: "+ name); //change text on button for the update
        }

        startTimeButton.setOnClickListener(new View.OnClickListener() { //if a click on this button it will create a timepickerdialog
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddLesson.this,
                        startT,
                        00,
                        00,
                        true).show();

                startTimeText.setText("" + startTime.get(Calendar.HOUR_OF_DAY) + ":" + startTime.get(Calendar.MINUTE) + "");
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddLesson.this,
                        endT,
                        00,
                        00,
                        true).show();

                endTimeText.setText("" + endTime.get(Calendar.HOUR_OF_DAY) + ":" + endTime.get(Calendar.MINUTE) + "");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = enterName.getText().toString();
                String location = enterLocation.getText().toString();
                String dayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
                String classType = classTypeSpinner.getSelectedItem().toString();
                int startHour = startTime.get(Calendar.HOUR_OF_DAY);
                int startMin = startTime.get(Calendar.MINUTE);
                int endHour = endTime.get(Calendar.HOUR_OF_DAY);
                int endMin = endTime.get(Calendar.MINUTE);

                intent = new Intent(AddLesson.this, Dashboard.class);

                intent.putExtra("NAME", name);
                intent.putExtra("LOCATION", location);
                intent.putExtra("DAY", dayOfWeek);
                intent.putExtra("CLASSTYPE", classType);
                intent.putExtra("STARTHOUR", startHour);
                intent.putExtra("STARTMIN", startMin);
                intent.putExtra("ENDHOUR", endHour);
                intent.putExtra("ENDMIN", endMin);
                intent.putExtra("callingActivity", callingActivity); //sends all of the entered data back to dashboard for processing


                if (name.equalsIgnoreCase("")) {
                    enterName.setError("Please Enter Name");
                } else if (location.equalsIgnoreCase("")) {
                    enterLocation.setError("Please Enter Location"); //asks for uses to enter these fields if left empty
                } else {
                    if (callingActivity == 0) {
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        intent.putExtra("ID", _id); //sends back id if update is calling activity
                        startActivity(intent);
                    }
                }
            }
        });
    }
    /// I reference this timepickerdialog online and changed it to work for my activity. I think some of it is from www.stackoverflow.com
    TimePickerDialog.OnTimeSetListener startT=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startTime.set(Calendar.MINUTE, minute);
        }
    };

    TimePickerDialog.OnTimeSetListener endT=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {
            endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endTime.set(Calendar.MINUTE, minute);
        }
    };

}
