package com.example.pranshu.hackathonproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    ArrayList<String> listLatitude, listLongitude, listPlace;
    Double latitude, longitude;
    LatLng latLng;
    LocationManager locationManager;
    String provider;
    Double latdd, longdd;
    TextView etNear, etDist;
    Location location;
    int meter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        listLatitude = getIntent().getStringArrayListExtra("resultLat");
        listLongitude = getIntent().getStringArrayListExtra("resultLong");
        listPlace = getIntent().getStringArrayListExtra("resultPlace");
        etNear = (TextView) findViewById(R.id.editNearestMarker);
        etDist = (TextView) findViewById(R.id.editDiatance);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //noinspection MissingPermission
        locationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
       // mMap.getUiSettings().setMyLocationButtonEnabled(true);

        int i;

        for (i = 0; i < listLatitude.size(); i++) {
            latitude = (Double) Double.parseDouble(listLatitude.get(i));
            longitude = (Double) Double.parseDouble(listLongitude.get(i));

            latLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title(listPlace.get(i)));

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
    }

    @Override
    public void onLocationChanged(Location location) {
        long min = Long.MAX_VALUE;
        Location target = new Location("target");
        int i;
        Double latd, longd;
        for (i = 0; i < listLatitude.size(); i++) {
            latd = (Double) Double.parseDouble(listLatitude.get(i));
            longd = (Double) Double.parseDouble(listLongitude.get(i));
            target.setLatitude(latd);
            target.setLongitude(longd);

            if (location.distanceTo(target) < min) {
                min = (long) location.distanceTo(target);
                latdd = target.getLatitude();
                longdd = target.getLongitude();
            }
        }
        LatLng lat = new LatLng(latdd,longdd);
                 Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latdd, longdd, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int j = 0; j < maxLines; j++) {
                String addressStr = address.get(0).getAddressLine(j);
                builder.append(addressStr);
                builder.append(" ");
            }

            String finalAddress = builder.toString(); //This is the complete address
            etNear.setText("Nearest location: " + finalAddress + ".");

            int km = (int)min/1000;

            etDist.setText("Distance :  "+km+"  KM.");

        } catch (IOException e) {
            // Handle IOException
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this,"Please turn on GPS", Toast.LENGTH_LONG).show();

    }
}
