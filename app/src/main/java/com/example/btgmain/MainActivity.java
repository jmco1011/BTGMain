package com.example.btgmain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    public final static String msg = "msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
        Thread welcomeThread = new Thread(){
            @Override
            public  void  run(){
                try {
                    super.run();
                    sleep(5000);
                }catch (Exception e){

                }finally {
                    Intent i = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();

    }
}