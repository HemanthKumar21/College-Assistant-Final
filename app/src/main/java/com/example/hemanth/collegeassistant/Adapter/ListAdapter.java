package com.example.hemanth.collegeassistant.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hemanth.collegeassistant.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    private ArrayList<String> logs;
    private int mViewResourceId;

    public ListAdapter(Context context, int textViewResourceId, ArrayList<String> logs) {
        super(context, textViewResourceId, logs);
        this.logs = logs;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        String log = logs.get(position);

        if(log != null) {
            TextView logdata = (TextView) convertView.findViewById(R.id.logstring);
            if(logdata != null) {
                logdata.setText(log);
            }
        }

        return convertView;
    }
}