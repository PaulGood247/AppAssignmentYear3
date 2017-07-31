package com.example.paul.assignment;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;
        import android.widget.TextView;

        import java.sql.SQLException;

public class FridayFragment extends Fragment {

    DBManager db;
    Cursor myCursor;
    static String classId, className;
    int callingActivity;
    Intent classClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBManager(this.getContext());

        classClick = new Intent(getActivity() , Dashboard.class);

        int code= getActivity().getIntent().getIntExtra("CODE", 0);

        if(code ==1){
            callingActivity=1;
        }
        else if(code== 6){
            callingActivity=6;
        }else if(code ==7){
            callingActivity=7;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //create the fragment

        View v = inflater.inflate(R.layout.show_lessons, container, false);
        ListView list = (ListView) v.findViewById(R.id.list);

        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list != null) {

            try {
                myCursor = db.selectSomethingByDay("Friday");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String[] columns = {"_id", "className", "classLocation", "classDayOfWeek", "classType", "classStartTime", "classEndTime"};

            int[] toList = new int[]{R.id.classIdList, R.id.classNameList, R.id.classLocationList, R.id.classDayList, R.id.classTypeList, R.id.classStartTimeList, R.id.classEndTimeList};
            SimpleCursorAdapter myAdapter = new SimpleCursorAdapter(getActivity(), R.layout.class_list_layout, myCursor, columns, toList, 0); //populate the list with the cursor
            list.setAdapter(myAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> list, View view, int position, long arg3) {

                    TextView idText = (TextView) view.findViewById(R.id.classIdList);
                    classId = (String) idText.getText();

                    TextView nameText = (TextView) view.findViewById(R.id.classNameList);
                    className = (String) nameText.getText();

                    if (callingActivity == 6) {

                        classClick.putExtra("class", classId);
                        classClick.putExtra("className", className);
                        getActivity();
                        getActivity().setResult(Activity.RESULT_OK, classClick);

                        /////alert dialog was made using various sources online
                        new AlertDialog.Builder(getActivity()) //creates a dialog box
                                .setIcon(android.R.drawable.ic_dialog_alert) //set a warning icon as the icon on top of alert box
                                .setTitle("Remove Lesson") //give a title
                                .setMessage("Are you sure you want to remove: " + className + "?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() { //if yes
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish(); //finish the activity
                                    }

                                })
                                .setNegativeButton("No", null).show(); //cancel
                        //////
                    } else if (callingActivity == 7) {
                        Intent intent = new Intent(getActivity(), AddLesson.class); //send user to addlesson class but will view differently due to addlesson code
                        intent.putExtra("class", classId);
                        intent.putExtra("callingActivity", 10);
                        startActivity(intent);
                    }
                }
            });
        }

        return v;
    }
}
