package edu.dartmouth.cs.battleroyalego;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class JoinActivity extends AppCompatActivity {

    Location user_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        user_location = getIntent().getParcelableExtra("USER_LOCATION");


    }
}
