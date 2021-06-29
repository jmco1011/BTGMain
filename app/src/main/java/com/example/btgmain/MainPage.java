package com.example.btgmain;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {
    Button btnEmergency, btnAboutUs,btnMap,btnShow,txtLogout;
    TextView txtname;
    DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        btnAboutUs = findViewById(R.id.btnAboutUs);
        btnEmergency = findViewById(R.id.btnEmergency);
        btnMap = findViewById(R.id.btnMap);
        btnShow = findViewById(R.id.btnShow);
        txtname =findViewById(R.id.txtname);
        txtLogout =findViewById(R.id.txtLogout);
        txtname = findViewById(R.id.txtname);

        txtname.setText("Welcome " + db.getUsername() + "!");

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent go = new Intent(MainPage.this, AboutUsPage.class);
                                          startActivity(go);

                                      }
                                  }
        );

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(MainPage.this, MapsActivity.class);
                startActivity(go);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent i = new Intent(MainPage.this, ShowList.class);
                                           startActivity(i);
                                       }
                                   }
        );

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
               startActivity(new Intent(MainPage.this,LoginPage.class));
        }


        });
    }
}