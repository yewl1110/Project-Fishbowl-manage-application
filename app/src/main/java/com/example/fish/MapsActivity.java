package com.example.fish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("권한이 필요합니다.")
                    .setMessage("외부 저장소 읽기 권한을 허용합니다.")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[] {"Manifest.permission.READ_EXTERNAL_STORAGE"}, 1);
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        LatLng latLng = new LatLng(0,0);
        CameraPosition position = new CameraPosition.Builder().target(latLng).zoom(16f).build();

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0), 15));
        map = googleMap;

    }

    final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
