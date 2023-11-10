package com.example.appxemphim.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import com.example.appxemphim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class WatchMoviesActivity extends AppCompatActivity {


    private TextView movies_name;
    private ImageView btn_back;


    private  VideoView videoView;
    private  MediaController mediaController;
    private AudioManager audioManager;


    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_watch_movies);
        initUi();
        btnBackOnClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    private void initUi(){
        videoView = findViewById(R.id.videoView);
        btn_back = findViewById(R.id.btn_back);
        movies_name = findViewById(R.id.movies_name);
        mDatabase = FirebaseDatabase.getInstance().getReference("Movies");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        String str_link = intent.getStringExtra("link");
        String str_name = intent.getStringExtra("name");
        String str_year = intent.getStringExtra("year");
        movies_name.setText(str_name + " (" +str_year + ")");
        videoView.setVideoURI(Uri.parse(str_link));
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        getData(str_name);
        videoView.start();

    }

    private void getData(String str){

        mDatabase.child(str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        int viewcount = Integer.parseInt(dataSnapshot.child("viewcount").getValue().toString());
                        mDatabase.child(str).child("viewcount").setValue(viewcount + 1);
                    }
                }

            }
        });

    }
    private void btnBackOnClick(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.stopPlayback();
                onBackPressed();
                finish();
            }
        });
    }











}