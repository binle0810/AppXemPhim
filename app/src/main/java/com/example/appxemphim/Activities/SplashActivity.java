package com.example.appxemphim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.appxemphim.R;

public class SplashActivity extends AppCompatActivity {
    private CardView mImageView;
    private LinearLayout mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mImageView = findViewById(R.id.image);
        mTextView = findViewById(R.id.text);
        startAnimation();
    }
    private void startAnimation() {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        Animation translate = AnimationUtils.loadAnimation(this, R.anim.translate);
        rotate.reset();
        translate.reset();
        mImageView.setAnimation(rotate);
        mTextView.setAnimation(translate);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },3000);
    }
}