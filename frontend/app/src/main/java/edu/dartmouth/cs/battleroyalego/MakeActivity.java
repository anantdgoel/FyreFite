package edu.dartmouth.cs.battleroyalego;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MakeActivity extends AppCompatActivity {

    Location user_location;
    String gameID;
    String gameName;
    String gamePassword;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("https://hackdartmo.firebaseio.com");
    GeoFire geoFire = new GeoFire(ref);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        Button startGame = findViewById(R.id.create_game);
        final EditText gameNameField = findViewById(R.id.game_name);
        gameName = gameNameField.getText().toString();
        final EditText gamePasswordField = findViewById(R.id.password);
        gamePassword = gamePasswordField.getText().toString();
        user_location = getIntent().getParcelableExtra("USER_LOCATION");
        final String user_location_lat = Double.toString(user_location.getLatitude());
        final String user_location_long = Double.toString(user_location.getLongitude());


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://domain_name/api/newgame";

// Request a string response from the provided URL.
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // what to do on response
                        gameID = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("game_lat", user_location_lat);
                params.put("game_long", user_location_long);
                params.put("game_name", gameName);
                params.put("game_pwd", gamePassword);
                params.put("Content-Type", "application/json");

                return params;
            }
        });

// Add the request to the RequestQueue.
        queue.add(postRequest);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeActivity.this, GameRoom.class);
                intent.putExtra("USER_LOCATION", user_location);
                intent.putExtra("GAME_LOCATION", user_location);
                intent.putExtra("GAME_ID", gameID);
                geoFire.setLocation("GAME_"+gameID, new GeoLocation(user_location.getLatitude(), user_location.getLongitude()));
                startActivity(intent);
            }
        });
    }

}
