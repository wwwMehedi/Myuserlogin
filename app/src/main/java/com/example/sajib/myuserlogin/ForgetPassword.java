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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
private EditText  editText;
private Button button;
private FirebaseAuth firebaseAuth;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    editText=(EditText)findViewById(R.id.edittextid);
    button =(Button)findViewById(R.id.buttonid);
    firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));
        }
        progressDialog = new ProgressDialog(this);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            forgrtfirst();
        }
    });
    }
    public void forgrtfirst()
    {
        final  String email=editText.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Enter your email",Toast.LENGTH_SHORT).show();
            return;

        }

        progressDialog.setTitle("Loading.......");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgetPassword.this,"successful",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent=new Intent(ForgetPassword.this,Login.class);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(ForgetPassword.this,"unsuuccesful",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
    }
}
