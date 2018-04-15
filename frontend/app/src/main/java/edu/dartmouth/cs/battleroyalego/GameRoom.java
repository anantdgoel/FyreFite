package edu.dartmouth.cs.battleroyalego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameRoom extends AppCompatActivity {

    public String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);
        gameName = getIntent().getStringExtra("GAME_NAME");
        final TextView gameNameText = findViewById(R.id.gameName);
        gameNameText.setText(gameName);
        Button map = findViewById(R.id.startGameButton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameRoom.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
