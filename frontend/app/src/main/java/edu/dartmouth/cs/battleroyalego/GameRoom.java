package edu.dartmouth.cs.battleroyalego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }
}
