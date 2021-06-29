package com.example.btgmain;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    // variable for our array list and context
    private ArrayList<Modal> ModalArrayList;
    private Context context;


    // constructor
    public RVAdapter(ArrayList<Modal> ModalArrayList, Context context) {
        this.ModalArrayList = ModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // on below line we are setting data
        // to our views of recycler view item.
        Modal modal = ModalArrayList.get(position);
        holder.txtsUsername.setText(modal.getUsername());
        holder.txtsFname.setText(modal.getFname());
        holder.txtsLname.setText(modal.getLname());
        holder.txtsPassowrd.setText(modal.getPassword());
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,UpdateUser_Page.class);
                i.putExtra("fname",modal.getFname());
                i.putExtra("lname", modal.getLname());
                i.putExtra("username",modal.getUsername());
                i.putExtra("password",modal.getPassword());
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        // returning the size of our array list
        return ModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private TextView txtsUsername, txtsFname, txtsLname, txtsPassowrd, btnUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text
            txtsUsername = itemView.findViewById(R.id.txtsUsername);
            txtsFname = itemView.findViewById(R.id.txtsFname);
            txtsLname = itemView.findViewById(R.id.txtsLname);
            txtsPassowrd = itemView.findViewById(R.id.txtsPassword);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);

        }

    }
}


