package com.example.sajib.myuserlogin;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Sajib on 31-Mar-18.
 */

public class FirebaseHelper {
    private DatabaseReference databaseReference;
    ArrayList<Model> models = new ArrayList<>();

    public FirebaseHelper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;

    }

    public ArrayList<Model> retrive() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dp :dataSnapshot.getChildren()){
                    Model model=dp.getValue(Model.class);
                    models.add(model);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return models;
    }
}


