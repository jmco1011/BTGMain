package com.example.btgmain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class ShowList extends AppCompatActivity {
private ArrayList<Modal> MAL;
private DBHandler dbHandler;
private RVAdapter rvAdapter;
private RecyclerView rv;
Button btnUpdate;
EditText txtFname, txtLname, txtPassword ,txtUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        getSupportActionBar().hide();
        txtFname = findViewById(R.id.txtFName);
        txtLname = findViewById(R.id.txtLName);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername = findViewById(R.id.txtUsername);
        btnUpdate = findViewById(R.id.btnUpdate);

        MAL = new ArrayList<>();
        dbHandler = new DBHandler(ShowList.this);

        MAL = dbHandler.readUser();

        rvAdapter = new RVAdapter(MAL,ShowList.this);
        rv = findViewById(R.id.rvList);

        LinearLayoutManager llm = new LinearLayoutManager(ShowList.this);
        rv.setLayoutManager(llm);

        rv.setAdapter(rvAdapter);

    }
}