package com.example.sajib.myuserlogin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DistanceMatrixApi {
    @GET("maps/api/distancematrix/json")
    Call<DistanceData> getLatLngByAddress(@Query("origins")String origins,
                                          @Query("destinations")String destinations,
                                          @Query("key")String key);
}
