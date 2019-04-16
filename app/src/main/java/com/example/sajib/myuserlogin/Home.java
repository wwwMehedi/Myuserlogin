package com.example.sajib.myuserlogin;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajib.myuserlogin.markaradapter.MarkerInfoWindowAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    public static final int REQUEST_LOCATION_CODE=99;
    private double GeoLat;
    private double GeoLong;

    private double currentLat,currentLong;
    private Marker currentLocationMarker;
    private Marker marker;
    private List<Marker> markers = new ArrayList<>();
    private  DatabaseReference mdatbase;
    private static final String TAG=Home.class.getSimpleName();
    private Retrofit mRetrofit;
    public static final String BASE_URL = "https://maps.googleapis.com/";
    public static final String KEY = "AIzaSyBcz0i3t0PzJR-XHBvsTVXWTFE4I9jGxH4";
    private ArrayList<Model> modelArrayList;
    private LatLng listOfGeo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                }
            }
        };//this activity for auto authentication log out concept

        mdatbase=FirebaseDatabase.getInstance().getReference().child("AlLocation");

        //distanceMatrix("23.864992,90.4040168","23.7441373,90.3782211",KEY);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_CODE:
                if(grantResults.length> 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if(client==null)// if google apiclient is not accessible then this condition will be appliy
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);//log in checking

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
              }
    }

@Override
public void onMapReady(final GoogleMap googleMap) {

    mMap = googleMap;
    Log.e(TAG,"onMapReady");
    mdatbase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot dp: dataSnapshot.getChildren())
            {
                Model model=dp.getValue(Model.class);
                GeoLat=model.getLatitude();
                GeoLong=model.getLongitude();

                Log.e("tag", String.valueOf(model.getLatitude()));
                Log.e("tag", String.valueOf(model.getLongitude()));

                LatLng listOfGeo = new LatLng(GeoLat, GeoLong);
                MarkerOptions options = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                        .title(model.getTitle()+","+model.getAddress()+","+model.getSlot()+"seat")
                        .snippet("Phone:"+model.getPhone())


                        //.snippet(String.valueOf(obj.getLongitude())

                        .position(listOfGeo);
                mMap = googleMap;
                mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listOfGeo, 13));

                MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getApplicationContext(), String.valueOf(currentLat),String.valueOf(currentLong));
                //,progressDialog(paramiter)
                googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                //This code is for if we click any position of marker then it shows marker position and create new marker

//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng arg0) {
//                       // mMap.clear();
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(arg0);
//                        mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
//                        Marker marker = mMap.addMarker(markerOptions);
//                        marker.showInfoWindow();
//                    }
//                });


            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

    }




}

//    public void distanceMatrix(String origins,String direction,String key) {
//        mRetrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        DistanceMatrixApi distanceMatrixApi = mRetrofit.create(DistanceMatrixApi.class);
//
//        Call<DistanceData> dataCall = distanceMatrixApi.getLatLngByAddress(origins, direction,key);
//        dataCall.enqueue(new Callback<DistanceData>() {
//            @Override
//            public void onResponse(Call<DistanceData> call, Response<DistanceData> response) {
//                try {
//                    DistanceData distanceData = response.body();
//                    if (distanceData.getStatus().equals("OK")) {
//                       String text = distanceData.getRows().get(0).getElements().get(0).getDistance().getText();
//                       String data= distanceData.getRows().get(0).getElements().get(0).getDuration().getText();
//                        Toast.makeText(Home.this, text, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(Home.this, data, Toast.LENGTH_SHORT).show();
//                        int distanceValue = distanceData.getRows().get(0).getElements().get(0).getDistance().getValue();
//
//                    } else {
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DistanceData> call, Throwable t) {
//                Log.i("JSON", "yahooobb  " + call);
//
//            }
//        });
//    }

    protected synchronized void buildGoogleApiClient(){
        client=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation=location;
        if(currentLocationMarker!=null){
            currentLocationMarker.remove();
        }
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        currentLat=location.getLatitude();
        currentLong=location.getLongitude();
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomBy(13));
        if (client!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
            //LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest =new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }
    public  boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION )!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            }
            return false;


        }
        else
            return false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);// ifuser exit from app directly ten it will not logout
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
          Intent intent=new Intent(Home.this,AddLocation.class);
          startActivity(intent);
            return true;

        }
        else  if (id == R.id.action_exit) {
            moveTaskToBack(true);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {



        }
        else if (id == R.id.nav_Parkingslot) {
            Intent intent=new Intent(Home.this,ParkingSlotActivity.class);
            startActivity(intent);


        }
//        else if (id == R.id.nav_conversation) {
//            Intent intent=new Intent(Home.this,ConversationActivity.class);
//            startActivity(intent);
//
//        }
//        else if (id == R.id.nav_forum) {
//            Intent intent=new Intent(Home.this,ForumActivity.class);
//            startActivity(intent);
//
//        }
        else if (id == R.id.nav_logout) {
            mAuth.signOut();

        }
//        else if (id == R.id.nav_terms_and_condition) {
//
//
//        }
//
//        else if (id == R.id.nav_about) {
//
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
