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
    DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
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
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popup = layoutInflater.inflate(R.layout.popupwindow, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popup, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popup.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
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