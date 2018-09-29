package com.example.sajib.myuserlogin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ParkingSlotActivity extends AppCompatActivity {
    private DatabaseReference mdatbase;
    RecyclerView recyclerView;
    ArrayList<Model> models=new ArrayList<>();
    private FirebaseHelper firebaseHelper;
    private Myadapterclass myadapterclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot);
        recyclerView=(RecyclerView)findViewById(R.id.recycleid);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
       // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mdatbase= FirebaseDatabase.getInstance().getReference().child("AlLocation");
        firebaseHelper =new FirebaseHelper(mdatbase);
        models=firebaseHelper.retrive();
        myadapterclass=new Myadapterclass(this,models);
        recyclerView.setAdapter(myadapterclass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
