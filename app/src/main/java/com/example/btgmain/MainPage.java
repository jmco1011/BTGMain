package com.example.btgmain;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {
    Button btnEmergency, btnAboutUs,btnMap,btnShow,txtLogout;
    TextView txtname;
    boolean click = true;
    AlertDialog.Builder builder;
    DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        builder = new AlertDialog.Builder(this);
        PopupWindow popUp = new PopupWindow(this);
        LinearLayout layout  = new LinearLayout(this);
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
                builder.setTitle("Logout");
                builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(MainPage.this,LoginPage.class));
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:(6374)4423939"));
                startActivity(callIntent);
            }
        });
    }
}