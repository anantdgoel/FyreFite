package edu.dartmouth.cs.battleroyalego;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.TextView;

public class MakeActivity extends AppCompatActivity {

    Location user_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        Button startGame = findViewById(R.id.create_game);
        user_location = getIntent().getParcelableExtra("USER_LOCATION");

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
