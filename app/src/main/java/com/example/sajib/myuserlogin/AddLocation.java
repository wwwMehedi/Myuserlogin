package com.example.sajib.myuserlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class AddLocation extends AppCompatActivity  {

    //implements LocationListener,
    //        GoogleApiClient.ConnectionCallbacks,
    //        GoogleApiClient.OnConnectionFailedListener

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseUser;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    //private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextInputLayout layoutname,layoutphone,layoutadress,layoutslot,layoutautocomplete;
    private EditText editTextone;
    private EditText geoLat;
    private EditText geoLong;
    private EditText edititphone;
    private EditText editaddress;
    private EditText editTextslot;
    private TextView tv;

    // This is new COde
    private ProgressBar progressBar;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private String geolat;
    private String geolong;

    AutoCompleteTextView auto;


    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> id=new ArrayList<>();
    ArrayList<String> id2=new ArrayList<>();



    private SpotsDialog spotsDialogprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        tv = (TextView) this.findViewById(R.id.mywidget);
        tv.setSelected(true);
        layoutname=findViewById(R.id.layoutnameid);
        layoutphone=findViewById(R.id.layoutphoneid);
        layoutadress=findViewById(R.id.layoutaddressid);
        layoutslot=findViewById(R.id.layoutslotid);
        layoutautocomplete=findViewById(R.id.layoutautocompleteid);

        editTextone = (EditText) findViewById(R.id.edittexone);
        auto=findViewById(R.id.autoid);
        geoLat = (EditText) findViewById(R.id.edittexttwo);
        geoLong = (EditText) findViewById(R.id.edittextthree);
        setValue();

        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name);
        auto.setThreshold(1);
        auto.setAdapter(adapter);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item=(String)adapterView.getItemAtPosition(i);
                int position=-1;
                for(int x=0;x<name.size();x++){
                    if(name.get(x).equals(item)){
                        position=x;
                        break;
                    }
                }
                geoLat.setText(id.get(position));
                geoLong.setText(id2.get(position));
            }
        });




        edititphone = (EditText) findViewById(R.id.editphonnumber);
        editaddress = (EditText) findViewById(R.id.editaddress);
        editTextslot = (EditText) findViewById(R.id.editparkingslot);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AlLocation");
        databaseUser = FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser.getUid());

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
        //  progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        if (progressBar != null) {
//            progressBar.setVisibility(View.GONE);
//        }

        progressDialog = new ProgressDialog(this);

        //THis is new Code

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                // The next two lines tell the new client that “this” current class will handle connection stuff
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                //fourth line adds the LocationServices API endpoint from GooglePlayServices
//                .addApi(LocationServices.API)
//                .build();

        // Create the LocationRequest object
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
//                .setFastestInterval(1 * 1000);

    }
    public void setValue() {
        name.add("Dhanmondi 32");
        id.add("23.7507847");
        id2.add("90.3660748");

        name.add("dhanmondi 7");
        id.add("23.7497467");
        id2.add("90.3537703");

        name.add("dhanmondi 15");
        id.add("23.7498374");
        id2.add("90.3537703");

        name.add("dhanmondi 8");
        id.add("23.7461405");
        id2.add("90.3756262");

        name.add("dhanmondi 9");
        id.add("23.7461601");
        id2.add("90.3756262");

        name.add("dhanmondi 27");
        id.add("23.7461844");
        id2.add("90.3690601");
        name.add("mohammadpur");
        id.add("23.7488214");
        id2.add("90.143968");
        name.add("Mirpur 10 Bus Stopage");
        id.add("23.8067457");
        id2.add("90.3677811");
        name.add("Mirpur 1");
        id.add("23.8103062");
        id2.add("90.3216033");
        name.add("Mirpur 2");
        id.add("23.8049833");
        id2.add("90.3612882");


    }


    private void savedata() {
        final String name = editTextone.getText().toString().trim();
        geolat = geoLat.getText().toString().trim();
        geolong = geoLong.getText().toString().trim();
        final String address = editaddress.getText().toString().trim();
        final String phone = edititphone.getText().toString().trim();
        final String editslot = editTextslot.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextone.setError("Your name is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(geolat))) {
            geoLat.setError("Your geoLat is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(geolong))) {
            geoLong.setError("Your geoLong is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(address))) {
            editaddress.setError("Your address is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(phone))) {
            edititphone.setError("Your phone is empty");
            return;
        }
        if (TextUtils.isEmpty(String.valueOf(editslot))) {
            editTextslot.setError("Your slot is empty");
            return;
        }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(geolat)
                && !TextUtils.isEmpty(String.valueOf(geolong)) && !TextUtils.isEmpty(address) &&
                !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(editslot)) {
            //now progressDialog is working now
            progressDialog.setTitle("Loading");
            progressDialog.show();

            final DatabaseReference newpost = databaseReference.push();
            databaseUser.addValueEventListener(new ValueEventListener() {
                //dont understand
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    newpost.child("title").setValue(name);
                    newpost.child("slot").setValue(editslot);
                    newpost.child("address").setValue(address);
                    newpost.child("phone").setValue(phone);
                    newpost.child("userId").setValue(firebaseUser.getUid());
                    newpost.child("latitude").setValue(Double.parseDouble(geolat));
                    newpost.child("longitude").setValue(Double.parseDouble(geolong))

                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(AddLocation.this, Home.class);
                                        startActivity(intent);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddLocation.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //prgrss dlog
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

//    protected void onResume() {
//        super.onResume();
//        //Now lets connect to the API
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.v(this.getClass().getSimpleName(), "onPause()");
//
//        //Disconnect from API onPause()
//        if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//            mGoogleApiClient.disconnect();
//        }
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (authStateListener != null) {
//            firebaseAuth.removeAuthStateListener(authStateListener);
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), Home.class);
//        startActivity(intent);
//        finish();
//    }


    public void savebutton(View view) {
        savedata();
    }

//    @Override
//    public void onConnected(Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

//        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//        if (location == null) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//
//        } else {
//            //If everything went fine lets get latitude and longitude
//            currentLatitude = location.getLatitude();
//            currentLongitude = location.getLongitude();
//            String Latitude = String.valueOf(currentLatitude);
//            String Longitude = String.valueOf(currentLongitude);
//
//            geoLat.setText(Latitude);
//            geoLong.setText(Longitude);
//
//            Toast.makeText(this, currentLatitude + "  " + currentLongitude + "", Toast.LENGTH_LONG).show();
//        }
//    }


//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        /*
//         * Google Play services can resolve some errors it detects.
//         * If the error has a resolution, try sending an Intent to
//         * start a Google Play services activity that can resolve
//         * error.
//         */
//        if (connectionResult.hasResolution()) {
//            try {
//                // Start an Activity that tries to resolve the error
//                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
//                /*
//                 * Thrown if Google Play services canceled the original
//                 * PendingIntent
//                 */
//            } catch (IntentSender.SendIntentException e) {
//                // Log the error
//                e.printStackTrace();
//            }
//        } else {
//            /*
//             * If no resolution is available, display a dialog to the
//             * user with the error.
//             */
//            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        currentLatitude = location.getLatitude();
//        currentLongitude = location.getLongitude();
//        String Latitude = String.valueOf(currentLatitude);
//        String Longitude = String.valueOf(currentLongitude);
//
//        geoLat.setText(Latitude);
//        geoLong.setText(Longitude);
//
//        Toast.makeText(this, currentLatitude + "  " + currentLongitude + "", Toast.LENGTH_LONG).show();
//    }
}
