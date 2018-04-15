package edu.dartmouth.cs.battleroyalego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class DeathActivity extends AppCompatActivity {

    Button main;
    ImageView v;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death);

        v = (ImageView) findViewById(R.id.skull_image);
        animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        v.setAnimation(animation);

        main = findViewById(R.id.button_main);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeathActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
