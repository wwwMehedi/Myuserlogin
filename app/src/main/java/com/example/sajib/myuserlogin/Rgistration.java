package com.example.sajib.myuserlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Rgistration extends AppCompatActivity {
    private EditText nameeditTextid;
    private EditText emaileeditTextid;
    private EditText passwordeditTextid;
    private EditText RetypepasswordeditTextid;
    private EditText addresseditTextid;
    private EditText phoneedditTextid;
    private EditText countryeditTextid;
    private EditText cityeditTextid;
    private ImageView imageviewid;
    private ImageButton imagebuttonTextid;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrationxml);

        nameeditTextid=(EditText)findViewById(R.id.nameeditTextid);
        emaileeditTextid=(EditText)findViewById(R.id.emaileeditTextid);
        passwordeditTextid=(EditText)findViewById(R.id.passwordeditTextid);
        RetypepasswordeditTextid=(EditText)findViewById(R.id.RetypepasswordeditTextid);
        addresseditTextid=(EditText)findViewById(R.id.addresseditTextid);
        phoneedditTextid=(EditText)findViewById(R.id.phoneedditTextid);
        countryeditTextid=(EditText)findViewById(R.id.countryeditTextid);
        cityeditTextid= findViewById(R.id.cityeditTextid);
        imageviewid=(ImageView)findViewById(R.id.imageviewid);
        imagebuttonTextid=(ImageButton)findViewById(R.id.imagebuttonTextid);
        imagebuttonTextid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationuser();
            }
        });

     firebaseAuth=FirebaseAuth.getInstance();
     mDatabase= FirebaseDatabase.getInstance().getReference().child("user");
        if(firebaseAuth.getCurrentUser() != null){
            finish();
           startActivity(new Intent(getApplicationContext(), Home.class));
        }
        progressDialog = new ProgressDialog(this);

    }

    private void registrationuser(){

        final String name=nameeditTextid.getText().toString().trim();
        final  String email=emaileeditTextid.getText().toString().trim();
        final String password=passwordeditTextid.getText().toString().trim();
        final  String retypepassword=RetypepasswordeditTextid.getText().toString().trim();
        final String address=addresseditTextid.getText().toString().trim();
        final  String country=countryeditTextid.getText().toString().trim();
        final  String city=cityeditTextid.getText().toString().trim();
        final  String phone=phoneedditTextid.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(retypepassword)){
            Toast.makeText(this,"Please confirm password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please enter phone number",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Please enter address",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(country)){
            Toast.makeText(this,"Please enter country name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(city)){
            Toast.makeText(this,"Please enter city name",Toast.LENGTH_LONG).show();
            return;
            //here next step will be describe


        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        if(password.matches(retypepassword)){
            firebaseAuth.createUserWithEmailAndPassword(email, retypepassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();

                                String user_id = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = mDatabase.child(user_id);
                                current_user_db.child("name").setValue(name);
                                current_user_db.child("email").setValue(email);
                                current_user_db.child("userID").setValue(user_id);
                                current_user_db.child("phone").setValue(phone);
                                current_user_db.child("address").setValue(address);
                                current_user_db.child("country").setValue(country);
                                current_user_db.child("city").setValue(city);

                                finish();
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(Rgistration.this,"Registration Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else {
            progressDialog.dismiss();
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
        }
    }
    public void signin(View view) {
        Intent intent = new Intent(Rgistration.this,Rgistration.class);
        startActivity(intent);
    }
}
