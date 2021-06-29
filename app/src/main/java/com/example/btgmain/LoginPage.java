package com.example.btgmain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginPage extends AppCompatActivity {
    Button btnlogin;
    TextView txtRegister;
    String EHolder, PHolder;
    EditText loginusername, loginpassword;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase db;
    DBHandler dbHandler;
    Cursor cursor;
    String TempPassword = "Not Found";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
            btnlogin = (Button)findViewById(R.id.btnLogin);
            txtRegister = (TextView)findViewById(R.id.txtRegister);
            loginpassword = findViewById(R.id.loginpassword);
            loginusername = findViewById(R.id.loginusername);

            dbHandler = new DBHandler(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CheckEditTextStatus();
               verifyLogin();
                }

        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(LoginPage.this, RegistrationPage.class);
                startActivity(go);
            }
        });
    }
    private void verifyLogin(){
        if(EditTextEmptyHolder) {
            db = dbHandler.getWritableDatabase();
            cursor = db.query(dbHandler.TABLE_NAME,null,""+
                    dbHandler.Username_COL + "=?",new String[]{EHolder},
                    null,
                    null,
                    null);
            while (cursor.moveToNext()){
                if (cursor.isFirst()){
                    cursor.moveToFirst();
                    TempPassword = cursor.getString(cursor.getColumnIndex(dbHandler.Password_COL));
                    cursor.close();
                }
             }
            CheckFinalResult();
        }else {
            Toast.makeText(LoginPage.this,"madi ingkabil mo",Toast.LENGTH_LONG).show();
        }
    }
    public void CheckFinalResult(){
        if(TempPassword.equalsIgnoreCase(PHolder)){
            Toast.makeText(LoginPage.this,"Success", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginPage.this,MainPage.class));
        }else{
            Toast.makeText(LoginPage.this,"Madi madi",Toast.LENGTH_LONG).show();
        }
        TempPassword= "NOT_FOUND";
    }

    public void CheckEditTextStatus(){
        EHolder = loginusername.getText().toString();
        PHolder = loginpassword.getText().toString();

        if(TextUtils.isEmpty(EHolder)|| TextUtils.isEmpty(PHolder)){
            EditTextEmptyHolder = false;
        }else {
            EditTextEmptyHolder = true;
        }
    }

}