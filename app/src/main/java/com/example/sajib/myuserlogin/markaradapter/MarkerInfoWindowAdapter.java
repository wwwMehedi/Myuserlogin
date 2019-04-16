package com.example.sajib.myuserlogin.markaradapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajib.myuserlogin.DistanceData;
import com.example.sajib.myuserlogin.DistanceMatrixApi;
import com.example.sajib.myuserlogin.Home;
import com.example.sajib.myuserlogin.Model;
import com.example.sajib.myuserlogin.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
    private Context context;
   String Lat,Long;
    private Retrofit mRetrofit;
    public static final String BASE_URL = "https://maps.googleapis.com/";
    public static final String KEY = "AIzaSyBcz0i3t0PzJR-XHBvsTVXWTFE4I9jGxH4";
    public ProgressDialog progressDialog;
    TextView tvLat;
    TextView tvLng;
    TextView tvtitle;
    EditText tvphone;
    TextView tvslot;


    public MarkerInfoWindowAdapter(Context context,String Lat,String Long) {
        this.context = context;
        this.Lat=Lat;
        this.Long=Long;
       //, ProgressDialog progressDialog(paramaiter) this.progressDialog = progressDialog;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.map_marker_info_window, null);

        LatLng latLng = arg0.getPosition();
        tvLat = (TextView) v.findViewById(R.id.tv_lat);
        tvLng = (TextView) v.findViewById(R.id.tv_lng);
        tvtitle=v.findViewById(R.id.tv_title);
        tvslot=v.findViewById(R.id.tv_slot);
       tvphone=v.findViewById(R.id.phoneid);

       tvphone.setText(arg0.getSnippet());
       tvtitle.setText(arg0.getTitle());
      //  tvphone.setText(arg0.getTitle());
       // tvslot.setText(arg0.getSnippet());
      //  tvLat.setText("Latitude:" + latLng.latitude);
       // tvLng.setText("Longitude:"+ latLng.longitude);
//        distanceMatrix(Lat+","+Long,
//                latLng.latitude+","+latLng.longitude,KEY);


        distanceMatrix(Lat+","+Long,latLng.latitude+","+latLng.longitude,KEY);
       // progressDialog.show();
        return v;
    }

    public void distanceMatrix(String origins,String direction,String key) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DistanceMatrixApi distanceMatrixApi = mRetrofit.create(DistanceMatrixApi.class);

        Call<DistanceData> dataCall = distanceMatrixApi.getLatLngByAddress(origins, direction,key);
        dataCall.enqueue(new Callback<DistanceData>() {
            @Override
            public void onResponse(Call<DistanceData> call, Response<DistanceData> response) {
                try {
                    DistanceData distanceData = response.body();
                    if (distanceData.getStatus().equals("OK")) {
                        String text = distanceData.getRows().get(0).getElements().get(0).getDistance().getText();
                        String data= distanceData.getRows().get(0).getElements().get(0).getDuration().getText();
                        Toast.makeText(context, text+","+"from your location", Toast.LENGTH_LONG).show();
                        //Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                        int distanceValue = distanceData.getRows().get(0).getElements().get(0).getDistance().getValue();

                        progressDialog.dismiss();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DistanceData> call, Throwable t) {
                Log.i("JSON", "yahooobb  " + call);

            }
        });
    }




}
