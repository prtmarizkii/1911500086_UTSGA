package com.example.a1911500086_utsga.ui.notifications;

import android.Manifest;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.a1911500086_utsga.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link titiklokasisekarang086} factory method to
 * create an instance of this fragment.
 */
public class titiklokasisekarang086 extends Fragment {

    //Initialize variable
    Button btlocation;
    TextView tvlatitude,tvlongitude;
    FusedLocationProviderClient client;

    ImageView myimage;



    //animation
    private ImageView mmyimage;
    private AnimatorSet mRotateAnim;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialize view
        View view = inflater.inflate(R.layout.fragment_titiklokasisekarang086, container, false);

        //Assign
        btlocation = view.findViewById(R.id.btlocation);
        tvlatitude = view.findViewById(R.id.tvlatitude);
        tvlongitude = view.findViewById(R.id.tvlongitude);
        mmyimage = view.findViewById(R.id.myimage);


        //Initialize Location clinet
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        btlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity()
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    //When permission is granted
                    //call methode
                    getCurrentLocation();




                } else {
                    //wn permission is not granted
                    //requestpermission
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if(requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            //Wheen permission
            //Callmethode
            getCurrentLocation();
        }else {
            //wheen permissioon are dnied
            Toast.makeText(getActivity()
                    ,"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //Initialize Location manager
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        //Checek conditio
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //When Location services enabled
            //Get Last location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    //Check conditional
                    if(location !=null){
                        //when location result is not null
                        //set latitude
                        tvlatitude.setText(String.valueOf(location.getLatitude()));
                        //set longitude
                        tvlongitude.setText((String.valueOf(location.getLongitude())));
                    }else {
                        //when locatio result is null
                        //initialize location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setInterval(1000)
                                .setNumUpdates(1);

                        //Initialize Location Call Back
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //Initialize Location
                                Location location1 = locationResult.getLastLocation();
                                //set latitude
                                tvlatitude.setText(String.valueOf(location.getLatitude()));
                                //set longitude
                                tvlongitude.setText(String.valueOf(location.getLongitude()));
                            }
                        };
                        //request location updates
                        client.requestLocationUpdates(locationRequest
                                ,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else {
            //when location service not enabled
            //open location string
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags((Intent.FLAG_ACTIVITY_NEW_TASK)));
        }
    }
}