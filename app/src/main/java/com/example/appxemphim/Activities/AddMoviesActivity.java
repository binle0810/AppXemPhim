package com.example.appxemphim.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appxemphim.Models.Movies;
import com.example.appxemphim.R;
import com.example.appxemphim.Models.stars;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMoviesActivity extends AppCompatActivity {

    private ImageButton  btn_back;
    private Button btn_add;
    private EditText link_film, editText_year, editText_nation, editText_content, link_image, editText_name, editText_cast, editText_director, editText_time;
    private CheckBox checkbox_1, checkbox_3, checkbox_5, checkbox_7, checkbox_8, checkbox_9, checkbox_10, checkbox_12;
    private int EDITTEXT_COUNT = 2;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movies);
        initUi();
        btnAddOnClick();
        btnBackOnClick();
    }
    private void initUi(){
        btn_back = findViewById(R.id.btn_back);
        btn_add = findViewById(R.id.btn_add);
        link_film = findViewById(R.id.link_film);
        link_image=findViewById(R.id.link_Image_poster);
        editText_year = findViewById(R.id.editText_year);
        editText_nation = findViewById(R.id.editText_nation);
        editText_content = findViewById(R.id.editText_content);
        editText_name = findViewById(R.id.editText_name);
        editText_cast = findViewById(R.id.editText_cast);
        editText_director = findViewById(R.id.editText_director);
        editText_time = findViewById(R.id.editText_time);
        checkbox_1 = findViewById(R.id.checkbox_1);
        checkbox_3 = findViewById(R.id.checkbox_3);
        checkbox_5 = findViewById(R.id.checkbox_5);
        checkbox_7 = findViewById(R.id.checkbox_7);
        checkbox_8 = findViewById(R.id.checkbox_8);
        checkbox_9 = findViewById(R.id.checkbox_9);
        checkbox_10 = findViewById(R.id.checkbox_10);
        checkbox_12 = findViewById(R.id.checkbox_12);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void btnBackOnClick(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean checkError(){
        int check = 0;

        if (link_film.getText().toString().trim().matches("")){
            check = 1;
        }
        if (link_image.getText().toString().trim().matches("")){
            check = 1;
        }

        if (!checkbox_1.isChecked() && !checkbox_3.isChecked()
                && !checkbox_5.isChecked()  && !checkbox_7.isChecked() && !checkbox_8.isChecked()
                && !checkbox_9.isChecked() && !checkbox_10.isChecked() && !checkbox_12.isChecked() ){
            check = 1;
        }

        if (editText_name.getText().toString().trim().matches("") || editText_year.getText().toString().trim().matches("")
                || editText_nation.getText().toString().trim().matches("") || editText_content.getText().toString().trim().matches("") || editText_cast.getText().toString().trim().matches("")
                || editText_director.getText().toString().trim().matches("") || editText_time.getText().toString().trim().matches("")){
            check = 1;
        }

        if (check == 0){
            return true;
        } else {
            return false;
        }
    }
    private String getCategory(){
        String CATEGORY = "";
        if (checkbox_1.isChecked()){
            CATEGORY += checkbox_1.getText() + ",";
        }
        if (checkbox_3.isChecked()){
            CATEGORY += checkbox_3.getText().toString() + ",";
        }
        if (checkbox_5.isChecked()){
            CATEGORY += checkbox_5.getText().toString() + ",";
        }
        if (checkbox_7.isChecked()){
            CATEGORY += checkbox_7.getText().toString() + ",";
        }
        if (checkbox_8.isChecked()){
            CATEGORY += checkbox_8.getText().toString() + ",";
        }
        if (checkbox_9.isChecked()){
            CATEGORY += checkbox_9.getText().toString() + ",";
        }
        if (checkbox_10.isChecked()){
            CATEGORY += checkbox_10.getText().toString() + ",";
        }
        if (checkbox_12.isChecked()){
            CATEGORY += checkbox_12.getText().toString() + ",";
        }
        return CATEGORY;
    }
    private void btnAddOnClick(){
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkError() == true) {
                    String YEAR = editText_year.getText().toString();
                    String CAST = editText_cast.getText().toString();
                    String CONTENT = editText_content.getText().toString();
                    String NATIONS = editText_nation.getText().toString();
                    String NAME = editText_name.getText().toString();
                    String DIRECTOR = editText_director.getText().toString();
                    String TIME = editText_time.getText().toString();
                    addMovies(CAST, getCategory(), CONTENT, DIRECTOR, link_film.getText().toString(), link_image.getText().toString(), NAME, NATIONS, TIME, YEAR);
                }

            }
        });
    }
    private String getDateTime(String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        String time = dateFormat.format(date);
        return time;
    }

    private void addMovies(String cast, String category, String content, String director, String link, String linkposter,
                           String name, String nation, String time, String year){
        stars stars = new stars(0,0);
        int viewcount = 0;
        Movies movies = new Movies( cast, category,content,director,link, linkposter, name, nation,getDateTime("HH:mm:ss dd/MM/yyyy"), stars, time, String.valueOf(viewcount),year );
        mDatabase.child("Movies").child(name).setValue(movies).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddMoviesActivity.this,"Thêm phim thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddMoviesActivity.this,"Thêm phim thất bại! vui lòng thử lại sau.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}