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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MakeActivity extends AppCompatActivity {

    Location user_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        Button startGame = findViewById(R.id.create_game);
        EditText gameName = findViewById(R.id.game_name);
        EditText gamePassword = findViewById(R.id.password);
        user_location = getIntent().getParcelableExtra("USER_LOCATION");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://domain_name/api/newgame";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // what to do on response

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // what to do on error
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeActivity.this, GameRoom.class);
                intent.putExtra("USER_LOCATION", user_location);
                intent.putExtra("GAME_LOCATION", user_location);
                startActivity(intent);
            }
        });
    }

}
