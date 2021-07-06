package com.example.btgmain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateUser_Page extends AppCompatActivity {
    private EditText txtuFname, txtuLname, txtuPassword ,txtuUsername;
    private Button button;
    private DBHandler dbHandler;
    String uFname, uLname, uUsername, uPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user__page);

        txtuFname = findViewById(R.id.txtuFname);
        txtuLname = findViewById(R.id.txtuLname);
        txtuPassword = findViewById(R.id.txtuPassword);
        txtuUsername = findViewById(R.id.txtuUsername);
        button = findViewById(R.id.btnUpdate2);

        dbHandler = new DBHandler(UpdateUser_Page.this);

        uFname = getIntent().getStringExtra("fname");
        uLname = getIntent().getStringExtra("lname");
        uUsername= getIntent().getStringExtra("username");
        uPassword = getIntent().getStringExtra("password");

        txtuFname.setText(uFname);
        txtuLname.setText(uLname);
        txtuPassword.setText(uPassword);
        txtuUsername.setText(uUsername);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.updateUser(uFname,txtuFname.getText().toString(),
                        txtuLname.getText().toString(),
                        txtuUsername.getText().toString(),
                        txtuPassword.getText().toString());
                Toast.makeText(UpdateUser_Page.this,"Update Successful",Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateUser_Page.this,MainPage.class));
            }
        });
        }
}