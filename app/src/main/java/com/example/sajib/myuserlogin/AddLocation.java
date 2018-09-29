package com.example.sajib.myuserlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddLocation extends AppCompatActivity {
private DatabaseReference databaseReference;
private FirebaseUser firebaseUser;
private  DatabaseReference databaseUser;
private ProgressDialog progressDialog;
private FirebaseAuth firebaseAuth;
private FirebaseAuth.AuthStateListener authStateListener;
private EditText editTextone;
private EditText editTexttwo;
private EditText editTextthree;
private EditText edititphone;
private EditText editaddress;
private EditText editTextslot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        editTextone=(EditText)findViewById(R.id.edittexone);
        editTexttwo=(EditText)findViewById(R.id.edittexttwo);
        editTextthree=(EditText)findViewById(R.id.edittextthree);
        edititphone=(EditText)findViewById(R.id.editphonnumber);
        editaddress=(EditText)findViewById(R.id.editaddress);
        editTextslot=(EditText)findViewById(R.id.editparkingslot);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("AlLocation");
        databaseUser=FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser.getUid());

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(AddLocation.this, Login.class));
                    finish();
                }
            }
        };//this activity for logout concept
        progressDialog = new ProgressDialog(this);

    }
    private void savedata()
    {
     final String name=editTextone.getText().toString().trim();
     final String  geoLat= editTexttwo.getText().toString().trim();
     final String  geoLong= editTextthree.getText().toString().trim();
     final String address=editaddress.getText().toString().trim();
     final String phone=edititphone.getText().toString().trim();
     final String editslot=editTextslot.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextone.setError("Your name is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(geoLat))) {
            editTexttwo.setError("Your geoLat is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(geoLong))) {
            editTexttwo.setError("Your geoLong is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(address))) {
            editTexttwo.setError("Your address is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(phone))) {
            editTexttwo.setError("Your phone is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(editslot))) {
            editTexttwo.setError("Your slot is empty");
            return;
        }

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(geoLat)
                && !TextUtils.isEmpty(String.valueOf(geoLong))&&  !TextUtils.isEmpty(address) &&
                !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(geoLat) && !TextUtils.isEmpty(editslot)){
            progressDialog.setTitle("Loading");
            progressDialog.show();
            final DatabaseReference newpost=databaseReference.push();
            databaseUser.addValueEventListener(new ValueEventListener() { //dont understand
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newpost.child("title").setValue(name);
                    newpost.child("slot").setValue(editslot);
                    newpost.child("address").setValue(address);
                    newpost.child("phone").setValue(phone);
                    newpost.child("latitude").setValue(Double.parseDouble(geoLat));
                    newpost.child("longitude").setValue(Double.parseDouble(geoLong))

                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Intent intent=new Intent(AddLocation.this,Home.class);
                                        startActivity(intent);
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddLocation.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }
    }

    public void savebutton(View view) {
        savedata();
    }
}
