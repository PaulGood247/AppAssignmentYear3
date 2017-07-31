package com.example.paul.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<String> {
    public MyListAdapter(Context context, String[] values) {
        super(context, R.layout.class_list_layout, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.class_list_layout, parent, false);

        String lesson_id = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.classIdList);

        theTextView.setText(lesson_id);

        return theView;
    }
}
