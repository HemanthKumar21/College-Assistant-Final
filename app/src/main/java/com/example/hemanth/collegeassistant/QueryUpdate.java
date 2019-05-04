package com.example.hemanth.collegeassistant;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hemanth.collegeassistant.Adapter.DataBaseHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class QueryUpdate extends AppCompatActivity {

    EditText editQuery,editAnswer,editID;
    Button button;
    DataBaseHelper dataBaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_update);

        editQuery = (EditText)findViewById(R.id.editText_query);
        editAnswer = (EditText)findViewById(R.id.editText_answer);
        editID = (EditText)findViewById(R.id.editText_qid);
        button = (Button)findViewById(R.id.button_updateQuery);
        addQuery();
    }

    public void addQuery() {
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String query = editQuery.getText().toString();
                        String answer = editAnswer.getText().toString();
                        String id = editID.getText().toString();


                        if (TextUtils.isEmpty(id)) {
                            Toast.makeText(getApplicationContext(), "Enter quetion ID!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(query)) {
                            Toast.makeText(getApplicationContext(), "Enter query!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(answer)) {
                            Toast.makeText(getApplicationContext(), "Enter answer!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String aiml = "<category>\n" +
                                "\t    <pattern>"+query+"</pattern>\n" +
                                "\t    <template>"+answer+"</template>\n" +
                                "</category>\n";

                        File saveFile = new File(getFilesDir().toString() + "/collegeAssistant/bots/CollegeAssistant/aiml/save.txt");
                        File updateAIML = new File(getFilesDir().toString() + "/collegeAssistant/bots/CollegeAssistant/aiml/updateAIML.aiml");
                        File questionSet = new File(getFilesDir().toString() + "/collegeAssistant/bots/CollegeAssistant/aiml/questions.txt");
                        int qid = Integer.parseInt(id);
                        dataBaseHelper = new DataBaseHelper(getBaseContext());
                        Cursor cursor = dataBaseHelper.getLog(qid);
                        int rows = cursor.getCount();
                        if(rows > 0) {
                            try {
                                BufferedWriter out = new BufferedWriter(
                                        new FileWriter(saveFile, true));
                                out.write(aiml + "\n");
                                out.close();
                                out = new BufferedWriter(new FileWriter(updateAIML));
                                out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                        "<aiml version=\"2.0\">\n");
                                out.close();

                                FileInputStream in = new FileInputStream(saveFile);
                                FileOutputStream outputStream = new FileOutputStream(updateAIML,true);

                                byte[] buffer = new byte[1024];
                                int length;

                                while ((length = in.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, length);
                                }
                                in.close();
                                outputStream.close();

                                out = new BufferedWriter(new FileWriter(updateAIML,true));
                                out.write("</aiml>");
                                out.close();

                                out = new BufferedWriter(new FileWriter(questionSet,true));
                                out.write("\n"+query);
                                out.close();

                                dataBaseHelper.deleteLog(qid);
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getBaseContext(),AdminActivity.class);
                        		startActivity(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Invalid question ID", Toast.LENGTH_SHORT).show();
                        }



                        
                    }
                });
    }
}
