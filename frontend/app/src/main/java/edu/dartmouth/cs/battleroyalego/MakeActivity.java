package edu.dartmouth.cs.battleroyalego;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MakeActivity extends AppCompatActivity {

    Location user_location;
    String gameID;
    String gameName;
    String gamePassword;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://hackdartmo.firebaseio.com").getReference();
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
        final String userUID = getIntent().getStringExtra("USER_UID");
        final String user_location_lat = Double.toString(user_location.getLatitude());
        final String user_location_long = Double.toString(user_location.getLongitude());


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.game_url) + "/newgame/";
        JSONObject params = new JSONObject();

        try {
            params.put("lat", user_location_lat);
            params.put("long", user_location_long);
            params.put("radius", "5000"); // probably should use user input
            params.put("name", gameName);
            params.put("game_pwd", gamePassword);
            params.put("admin_uid", userUID);
            params.put("Content-Type", "application/json");
        } catch (JSONException e){
            e.printStackTrace();
        }

        // Request a JSON response from the provided URL.
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
                gameID = null;
                try {
                    gameID = response.getString("uid");
                    System.out.println(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
                intent.putExtra("GAME_NAME", gameNameField.getText().toString());
                System.out.println(gameID);
                geoFire.setLocation(gameID, new GeoLocation(user_location.getLatitude(), user_location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                    }
                });
                startActivity(intent);
            }
        });
    }

}
