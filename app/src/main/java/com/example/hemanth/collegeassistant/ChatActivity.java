package com.example.hemanth.collegeassistant;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hemanth.collegeassistant.Adapter.ChatMessageAdapter;
import com.example.hemanth.collegeassistant.Adapter.DataBaseHelper;
import com.example.hemanth.collegeassistant.Pojo.ChatMessage;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {


    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    public Bot bot;
    public static Chat chat;
    private ChatMessageAdapter mAdapter;
    boolean auth=false;
    String response = "";
    String query="";
    private DataBaseHelper DB = null;
    static int qid = 0;
    //private DatabaseReference logData = FirebaseDatabase.getInstance().getReference("logs");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            //Toast.makeText(ChatActivity.this, "Run only once", Toast.LENGTH_LONG)
              //      .show();
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();


        if(!isFirstRun) {
            mListView = (ListView) findViewById(R.id.listView);
            mButtonSend = (FloatingActionButton) findViewById(R.id.btn_send);
            mEditTextMessage = (EditText) findViewById(R.id.et_message);
            mImageView = (ImageView) findViewById(R.id.iv_image);
            mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
            mListView.setAdapter(mAdapter);
            final File appDir = getFilesDir();

            DB = new DataBaseHelper(this);

            mButtonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //bot
                    String message = mEditTextMessage.getText().toString();

                    if (auth == false) {


                        CosineSimilarity cs = new CosineSimilarity();

                        response = chat.multisentenceRespond(message);

                        if (response.equals("NAIML")) {
                            try {
                                File file = new File(getFilesDir().toString() + "/collegeAssistant/bots/CollegeAssistant/aiml/pquery.txt");
                                query = cs.cosineSim(message, file);

                                if (query.equals("NA")) {
                                    file = new File(getFilesDir().toString() + "/collegeAssistant/bots/CollegeAssistant/aiml/questions.txt");
                                    query = cs.cosineSim(message, file);

                                    if (query.equals("NA")) {
                                        response = "No answer";
                                    } else {
                                        response = chat.multisentenceRespond(query);
                                        Toast.makeText(ChatActivity.this, query, Toast.LENGTH_LONG).show();
                                    }


                                    if (query.equals("NA") || response.equals("NAIML")) {
                                        /*
                                        File logFile = new File(getFilesDir().toString() + "/collegeAssistant/bots/CollegeAssistant/aiml/invalidated_log.txt");
                                        try {
                                            BufferedWriter out = new BufferedWriter(
                                                    new FileWriter(logFile, true));
                                            out.write(message + "\n");
                                            out.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        */
                                        qid++;
                                        if(query.equals("NA"))
                                            DB.insertLog(qid,message,query);
                                        if(response.equals("NAIML"))
                                            DB.insertLog(qid,message,response);

                                    }

                                } else {
                                    personalQueries(query);
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                    } else {
                        personalQueries(message);
                    }
                    if (TextUtils.isEmpty(message)) {
                        return;
                    }
                    sendMessage(message);
                    mimicOtherMessage(response);
                    mEditTextMessage.setText("");
                    mListView.setSelection(mAdapter.getCount() - 1);
                }

            });


            AssetManager assets = getResources().getAssets();
            //File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/collegeAssistant/bots/CollegeAssistant");

            File appData = new File(appDir, "/collegeAssistant/bots/CollegeAssistant");
            boolean b = appData.mkdirs();
            if (appData.exists()) {
                //Reading the file
                try {
                    for (String dir : assets.list("CollegeAssistant")) {
                        File subdir = new File(appData.getPath() + "/" + dir);
                        boolean subdir_check = subdir.mkdirs();
                        for (String file : assets.list("CollegeAssistant/" + dir)) {
                            File f = new File(appData.getPath() + "/" + dir + "/" + file);
                            if (f.exists()) {
                                continue;
                            }
                            InputStream in = null;
                            OutputStream out = null;
                            in = assets.open("CollegeAssistant/" + dir + "/" + file);
                            out = new FileOutputStream(appData.getPath() + "/" + dir + "/" + file);
                            //copy file from assets to the mobile's SD card or any secondary memory
                            copyFile(in, out);
                            in.close();
                            in = null;
                            out.flush();
                            out.close();
                            out = null;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //get the working directory
            MagicStrings.root_path = getFilesDir().toString()+"/collegeAssistant";
            System.out.println("Working Directory = " + MagicStrings.root_path);
            AIMLProcessor.extension =  new PCAIMLProcessorExtension();
            //Assign the AIML files to bot for processing
            bot = new Bot("CollegeAssistant", MagicStrings.root_path, "chat");
            chat = new Chat(bot);

        }

    }

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }



    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


    public void personalQueries(String message) {
        message = message.trim();
        if(auth == false) {
            response = "Please enter your Roll No.";
            auth = true;
            System.out.println(response+auth);
        }

        else  {




            if(message.equals("EXIT") || message.equals("exit")) {
                auth = false;
                System.out.println(response+auth+message);
                response = "exited";
                return;
            }
            DB = new DataBaseHelper(this);
            Cursor data = DB.getData(message);
            if(data.getCount() == 0) {

                //System.out.println(response+auth);
                if(DB.getAllData().getCount() > 0) {
                    response = "Invalid Rollno\n" +
                            "Please enter exit to stop";
                }
                else  {
                    Intent in = new Intent(ChatActivity.this, RegisterActivity.class);
                    startActivity(in);
                }
            }
            else
            {
                //query.toUpperCase();
                data.moveToNext();
                if(query.equals("CGPA")) {
                    response = data.getString(3);
                }
                else if(query.equals("SEMESTER")) {
                    response = data.getString(2);
                }
                auth = false;
                //System.out.println(response+auth);
            }
        }

    }
}
