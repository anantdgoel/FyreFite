package edu.dartmouth.cs.battleroyalego;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    boolean justStart = true;
    boolean isDead = false;

    Circle circle;

    Handler mHandler;

    Button firstAid, bandage, fire, weapon;
    TextView countdown, playerCount, health;

    int healthPoints, ammo, secondsLeft, nPlayers, nFirstAids, nBandages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        if(justStart){
            healthPoints = 100;
            ammo = 30;
            secondsLeft = 120;
            nPlayers = 2;
            nFirstAids = 1;
            nBandages = 1;
            justStart = false;
        }

        //check
        checkVictoryAndDeath();

        firstAid = findViewById(R.id.first_aid);
        bandage = findViewById(R.id.bandage);
        fire = findViewById(R.id.fire);
        weapon = findViewById(R.id.weapon);
        countdown = findViewById(R.id.countdown);
        playerCount = findViewById(R.id.player_count);
        health = findViewById(R.id.health);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mHandler = new Handler();

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fireWeapon()){
                    weapon.setText("PISTOL: " + ammo + " ROUNDS");
                }
            }
        });

        firstAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useFirstAid();
            }
        });

        bandage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useBandage();
            }
        });

        weapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ammo < 20){
                    Intent intent = new Intent(MapsActivity.this, VictoryActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                circle.setRadius(circle.getRadius()*0.9999); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 60000);
            }
        }
    };

    void startShrinking() {
        mStatusChecker.run();
    }

    void stopShrinking() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    //calculate health after being shot at, isDead is true if person is <= 0
    public void shot(int dmg){
        healthPoints = healthPoints - dmg;
        Toast.makeText(getApplicationContext(), "You have been shot! -" + dmg + " health", Toast.LENGTH_LONG).show();
        if(healthPoints <= 0){
            isDead = true;
            Intent intent = new Intent(MapsActivity.this, DeathActivity.class);
            startActivity(intent);
        }
    }


    //use first aid, return true if it worked and fully restored health
    public boolean useFirstAid(){
        nFirstAids--;
        if(nFirstAids < 0){
            Toast.makeText(getApplicationContext(), "No first aid left!", Toast.LENGTH_LONG).show();
            nFirstAids = 0;
            return false;
        }else{
            healthPoints = 100;
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopShrinking();
    }

    //use bandage, return true if it worked and restored health by 10
    public boolean useBandage(){
        nBandages--;
        if(nBandages < 0){
            Toast.makeText(getApplicationContext(), "No bandage left!", Toast.LENGTH_LONG).show();
            nBandages = 0;
            return false;
        }else{
            healthPoints += 10;
            if(healthPoints > 100){
                healthPoints = 100;
            }
            return true;
        }
    }

    public void checkVictoryAndDeath(){
        if(nPlayers <= 1){
            Intent intent = new Intent(MapsActivity.this, VictoryActivity.class);
            startActivity(intent);
        }else if(healthPoints <= 0){
            Intent intent = new Intent(MapsActivity.this, DeathActivity.class);
            startActivity(intent);
        }
    }

    //convert secondsLeft to 00:00
    public String convertToTime(int seconds){
        if(seconds < 0){
            Toast.makeText(getApplicationContext(), "Safe zone starts to shrink!", Toast.LENGTH_LONG).show();
            return null;
        }else if(seconds == 0){
            return "0:00";
        }else{
            int firstDigit = (int)Math.floor((double)seconds/60);
            int secondDigits = (int)((double)seconds%60);
            return firstDigit + ":" + secondDigits;
        }
    }


    //fire the user's weapon, return false if ammo is depleted
    public boolean fireWeapon(){
        ammo--;
        if(ammo < 0){
            Toast.makeText(getApplicationContext(), "No ammo left!", Toast.LENGTH_LONG).show();
            ammo = 0;
            return false;
        }else{
            return true;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        int gameTime = 60*60*1000;
        int circleDecreaseInterval = 10*1000;

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }

        /* Draw the arena boundaries */
        LatLng gameCenter = new LatLng(43.704553, -72.294674);
        circle = mMap.addCircle(new CircleOptions()
                .center(gameCenter)
                .radius(5000)
                .strokeColor(Color.RED)
                .fillColor(0x00000000));

        CountDownTimer Timer = new CountDownTimer(gameTime, circleDecreaseInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                circle.setRadius(circle.getRadius()*0.6);
            }

            @Override
            public void onFinish() {

            }
        };

        Timer.start();
        startShrinking();


//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(43.7, -72.3);
//        Circle circle = mMap.addCircle(new CircleOptions()
//                .center(new LatLng(43.7, -72.3))
//                .radius(1000)
//                .strokeColor(Color.RED)
//                .fillColor(0x00000000));
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }

    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }
}
