package com.example.btgmain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistrationPage extends AppCompatActivity {
    private EditText txtFname, txtLname, txtPassword ,txtUsername,txtCpassword,txtEmail;
    private Button button;
    private DBHandler dbHandler;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])"+
                    "(?=.*[0-9])"+
                    "(?=\\s+$)"+
                    ".{8,}"+
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        dbHandler = new DBHandler(RegistrationPage.this);
        txtFname = findViewById(R.id.txtFName);
        txtLname = findViewById(R.id.txtLName);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername = findViewById(R.id.txtUsername);
        txtCpassword = findViewById(R.id.txtCPassword);
        txtEmail = findViewById(R.id.txtEmail);
        button = findViewById(R.id.button);

        txtFname.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (source.equals("")) {
                            return source;
                        }
                        if (source.toString().matches("[a-zA-Z]+")){
                            return source;
                        }
                        return "";
                    }
                }
        });
        txtLname.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (source.equals("")) {
                            return source;
                        }
                        if (source.toString().matches("[a-zA-Z]+")){
                            return source;
                        }
                        return "";
                    }
                }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fname = txtFname.getText().toString();
                String Lname = txtLname.getText().toString();
                String Username = txtUsername.getText().toString();
                String Email = txtEmail.getText().toString();
                String Password = txtPassword.getText().toString();
                String cPassword = txtCpassword.getText().toString();

                try {
                    if (!validEmail() && !validPassword() && !validName() && !validlName()) {
                        return;
                    }else if (!cPassword.matches(Password)){
                        txtPassword.setError("Match Error");
                        txtCpassword.setError("Match Error");

                        return;
                    } else {
                        dbHandler.addUSer(Username, Fname, Lname, Email, Password);
                        Toast.makeText(RegistrationPage.this, "User added", Toast.LENGTH_SHORT).show();
                        txtUsername.setText("");
                        txtFname.setText("");
                        txtLname.setText("");
                        txtPassword.setText("");
                        txtEmail.setText("");
                        txtCpassword.setText("");
                        startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                    }
                } catch (Exception e) {
                   e.printStackTrace();}
            }
        });

    }
    public boolean validEmail(){
        String emailInput = txtEmail.getText().toString();
        if (emailInput.isEmpty()){
            txtEmail.setError("Field can't be empty");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            txtEmail.setError("invalid email");
            return false;
        }else {
            txtEmail.setError(null);
            return true;
        }
    }

    public boolean validPassword(){
        String passwordInput = txtPassword.getText().toString();
        if (passwordInput.isEmpty()) {
            txtPassword.setError("Field can't be Empty");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            txtPassword.setError("password too weak");
            return false;
        }else{
            txtPassword.setError(null);
            return true;
        }
    }
    public boolean validName() throws Exception{
        String fnameinput = txtFname.getText().toString();
        if (fnameinput.isEmpty()){
            txtFname.setError("Field can't be Empty");
            return false;
        }else
            txtFname.setError(null);
        return true;
    }
    public boolean validlName() throws Exception{
        String lnameinput = txtLname.getText().toString();
        if (lnameinput.isEmpty()){
            txtLname.setError("Field can't be Empty");
            return false;
        }else
            txtLname.setError(null);
        return true;
    }
}