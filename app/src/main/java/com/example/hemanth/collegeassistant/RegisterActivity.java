package com.example.hemanth.collegeassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hemanth.collegeassistant.Adapter.DataBaseHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText editID,editName,editSem,editCGPA;
    TextView textSkip;
    ProgressBar progressBar;
    Button button;
    DataBaseHelper DB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DB = new DataBaseHelper(this);

        editID = (EditText)findViewById(R.id.editText_rollno);
        editName = (EditText)findViewById(R.id.editText_name);
        editSem = (EditText)findViewById(R.id.editText_sem);
        editCGPA = (EditText)findViewById(R.id.editText_cgpa);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        button = (Button)findViewById(R.id.button);

        textSkip = (TextView)findViewById(R.id.textView_skip);
        textSkip.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(),ChatActivity.class);
                        startActivity(intent);
                    }
                }
        );
        addStudentData();
    }

    public void addStudentData() {
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = editName.getText().toString();
                        String id = editID.getText().toString();
                        String sem = editSem.getText().toString();
                        String cgpa = editCGPA.getText().toString();
                        if (TextUtils.isEmpty(editName.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(id)) {
                            Toast.makeText(getApplicationContext(), "Enter ID!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(id.length() < 10) {
                            Toast.makeText(getApplicationContext(), "ROLL NO should be 10 characters Eg: BTXXCSE0XX", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(TextUtils.isEmpty(sem)) {
                            Toast.makeText(getApplicationContext(), "Enter semester!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int s = Integer.parseInt(sem);
                        if(s < 0 || s > 8) {
                            Toast.makeText(getApplicationContext(), "semester should be between 0-8!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(TextUtils.isEmpty(cgpa)) {
                            Toast.makeText(getApplicationContext(), "Enter cgpa!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        float c = Float.parseFloat(cgpa);
                        if(c < 0 || c>10) {
                            Toast.makeText(getApplicationContext(), "cgpa shuld be between 0-10!", Toast.LENGTH_SHORT).show();
                            return;
                        }



                        progressBar.setVisibility(View.VISIBLE);

                        boolean isInserted = DB.insertData(editID.getText().toString(),
                                editName.getText().toString(),
                                editSem.getText().toString(),
                                editCGPA.getText().toString() );
                        if(isInserted == true) {

                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(RegisterActivity.this, ChatActivity.class);
                            progressBar.setVisibility(View.GONE);
                            startActivity(in);
                        }
                        else
                            Toast.makeText(RegisterActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
