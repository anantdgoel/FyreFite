package edu.dartmouth.cs.battleroyalego;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import javax.microedition.khronos.opengles.GL10;

public class VictoryActivity extends AppCompatActivity {

    Button mMenu;

    ImageView v;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        mMenu = findViewById(R.id.button_main);
        v = (ImageView) findViewById(R.id.victory_image);
        animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        v.setAnimation(animation);

        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VictoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }






    @Override
    public void onBackPressed() {
    }
}
