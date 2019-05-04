package com.example.hemanth.collegeassistant;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hemanth.collegeassistant.Adapter.DataBaseHelper;
import com.example.hemanth.collegeassistant.Adapter.ListAdapter;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper = null;
    ArrayList<String> logs = new ArrayList<String>();
    ListView listView;
    Button button,home;
    String log = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dataBaseHelper = new DataBaseHelper(this);
        Cursor cursor = dataBaseHelper.getLog();
        int numRows = cursor.getCount();
        if(numRows == 0){
            Toast.makeText(AdminActivity.this,"No Queries",Toast.LENGTH_LONG).show();
        } else {
            int i=1;
            while (cursor.moveToNext()) {
                log = cursor.getString(0)+" "+cursor.getString(1) + "@" + cursor.getString(2);
                //log = Integer.toString(i) + " " + log;
                logs.add(log);
                i++;
            }
            ListAdapter listAdapter = new ListAdapter(this,R.layout.list_adapter_view,logs);
            listView = (ListView)findViewById(R.id.listView);
            listView.setAdapter(listAdapter);
        }

        button = (Button)findViewById(R.id.button_update);
        home = (Button)findViewById(R.id.button_home);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(),QueryUpdate.class);
                        startActivity(intent);
                    }
                });
        home.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(),StartActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
