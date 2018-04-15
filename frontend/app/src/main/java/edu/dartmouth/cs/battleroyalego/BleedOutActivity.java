package edu.dartmouth.cs.battleroyalego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class BleedOutActivity extends AppCompatActivity {

    Button mFirstAid, mDeath;

    ImageView v;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleed_out);


        v = findViewById(R.id.blood_image);
        animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        v.setAnimation(animation);


        mFirstAid = findViewById(R.id.button_aid);
        mDeath = findViewById(R.id.button_give);

        mFirstAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BleedOutActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        mDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BleedOutActivity.this, DeathActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
