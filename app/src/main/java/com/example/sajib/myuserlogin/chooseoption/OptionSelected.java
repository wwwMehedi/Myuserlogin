package com.example.sajib.myuserlogin.chooseoption;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.sajib.myuserlogin.AddLocation;
import com.example.sajib.myuserlogin.Home;
import com.example.sajib.myuserlogin.ParkingSlotActivity;
import com.example.sajib.myuserlogin.R;

public class OptionSelected extends AppCompatActivity implements View.OnClickListener{
 private CardView nearby,locationbased,addhome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_selected);
        nearby=findViewById(R.id.nearbyhomeid);
        locationbased=findViewById(R.id.locationbasehomeid);
        addhome=findViewById(R.id.addhomeid);
        nearby.setOnClickListener(this);
        locationbased.setOnClickListener(this);
        addhome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.nearbyhomeid){
            Intent intent=new Intent(OptionSelected.this,Home.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.addhomeid){
            Intent intent=new Intent(OptionSelected.this,AddLocation.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.locationbasehomeid){
            Intent intent=new Intent(OptionSelected.this,ParkingSlotActivity.class);
            startActivity(intent);
        }

    }
}
