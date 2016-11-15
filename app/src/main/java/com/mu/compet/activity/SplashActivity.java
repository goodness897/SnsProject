package com.mu.compet.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.mu.compet.R;

import static com.mu.compet.R.id.imageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView animImageView;
    private AnimationDrawable anim;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animImageView = (ImageView) findViewById(imageView);

        anim = (AnimationDrawable) animImageView.getDrawable();
        anim.setOneShot(true);
        anim.start();

        if (anim.isRunning()) {
            moveLoginActivity();
        }

    }

    private void moveLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 1000);
    }
}
