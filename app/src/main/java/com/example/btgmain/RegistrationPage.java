package com.example.btgmain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationPage extends AppCompatActivity {
    private EditText txtFname, txtLname, txtPassword ,txtUsername;
    private Button button;
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        txtFname = findViewById(R.id.txtFName);
        txtLname = findViewById(R.id.txtLName);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername = findViewById(R.id.txtUsername);
        button = findViewById(R.id.button);

        dbHandler = new DBHandler(RegistrationPage.this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fname = txtFname.getText().toString();
                String Lname = txtLname.getText().toString();
                String Username = txtUsername.getText().toString();
                String Password = txtPassword.getText().toString();

                if (Username.isEmpty() && Fname.isEmpty()&& Lname.isEmpty()&&Password.isEmpty()){
                    Toast.makeText(RegistrationPage.this,"Please Enter data", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHandler.addUSer(Username,Fname,Lname,Password);
                Toast.makeText(RegistrationPage.this,"User added",Toast.LENGTH_SHORT).show();
                txtUsername.setText("");
                txtFname.setText("");
                txtLname.setText("");
                txtPassword.setText("");
            }
        });




    }
}