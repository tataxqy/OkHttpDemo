package com.example.okhttpdemo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_TEXT=1;
    private TextView mcontent;
    private static String string;
    private static final String TAG = "MainActivity";
    private final OkHttpClient mclient=new OkHttpClient();

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case UPDATE_TEXT:
                    mcontent.setText(string);

            }
            super.handleMessage(msg);
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menGet:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        get();
                        Message message=new Message();
                        message.what=UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void get()
    {

                Request.Builder builder=new Request.Builder();
                builder.url("https://github.com/square/okhttp/blob/master/README.md");
                Request request=builder.build();

                Log.d(TAG,"run:"+request);
                Call call=mclient.newCall(request);
                try {
                    Response response=call.execute();
                    if(response.isSuccessful())
                    {
                        string =response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

