package com.example.appxemphim.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appxemphim.Fragment.CommentsFragment;
import com.example.appxemphim.Fragment.RateStarFragment;
import com.example.appxemphim.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;

public class MoviesInformationActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private TextView textView_review, btn_show_more_review, textView_tab_1, textView_tab_2, textView_tab_3,
            textView_name_movies, textView_category_movies, textView_cast_movies, textView_year_movies,
            textView_nation_movies, textView_director_movies, textView_time_movies, textView_count_rates,
            textView_view_count_movies;
    private LinearLayout tab_1, tab_2, tab_3;
    private View line_4, line_5;

    private ImageView imageView_poser_small, imageView_poser_big, star_1, star_2, star_3, star_4, star_5, btn_favourite;
    private Button btn_play;
    private String name, category, year;
    private ImageButton btn_back;
    private boolean checkSH = false;

    private String nation="";
    private FirebaseUser user;
    private int STAR = 0, OTAL_RATING = 0;
    String link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_information);

        initUi();
        setValueMovies();
        btnShowHideReviewOnClick();
        btnBackOnClick();
        changeTab();
        btnFavouriteOnClick();

    }

    private void initUi(){
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        textView_review = findViewById(R.id.textView_review);
        textView_name_movies = findViewById(R.id.textView_name_movies);
        textView_category_movies = findViewById(R.id.textView_category_movies);
        textView_cast_movies = findViewById(R.id.textView_cast_movies);
        textView_year_movies = findViewById(R.id.textView_year_movies);
        textView_nation_movies = findViewById(R.id.textView_nation_movies);
        textView_director_movies = findViewById(R.id.textView_director_movies);
        textView_time_movies = findViewById(R.id.textView_time_movies);
        textView_count_rates = findViewById(R.id.textView_count_rates);
        textView_view_count_movies = findViewById(R.id.textView_view_count_movies);
        textView_tab_1 = findViewById(R.id.textView_tab_1);
        textView_tab_2 = findViewById(R.id.textView_tab_2);
        textView_tab_3 = findViewById(R.id.textView_tab_3);
        btn_favourite = findViewById(R.id.btn_favourite);
        line_4 = findViewById(R.id.line_4);
        line_5 = findViewById(R.id.line_5);
        btn_show_more_review = findViewById(R.id.btn_show_more_review);
        btn_play = findViewById(R.id.btn_play);
        btn_back = findViewById(R.id.btn_back);
        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);
        tab_3 = findViewById(R.id.tab_3);
        star_1 = findViewById(R.id.star_1);
        star_2 = findViewById(R.id.star_2);
        star_3 = findViewById(R.id.star_3);
        star_4 = findViewById(R.id.star_4);
        star_5 = findViewById(R.id.star_5);
        imageView_poser_small = findViewById(R.id.imageView_poser_3x4);
        imageView_poser_big = findViewById(R.id.imageView_poser_4x3);
    }

    private void btnFavouriteOnClick(){
        btn_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                DataSnapshot dataSnapshot = task.getResult();
                                String favourite = String.valueOf(dataSnapshot.child("favourite").getValue());
                                if (favourite.toLowerCase().contains(name.toLowerCase())){
                                    btn_favourite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_30);
                                    Toast.makeText(MoviesInformationActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                    mDatabase.child("Users").child(user.getUid()).child("favourite").setValue(favourite.replace(name + ",", ""));
                                } else {
                                    btn_favourite.setBackgroundResource(R.drawable.ic_round_favorite_30);
                                    Toast.makeText(MoviesInformationActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                    if (favourite == "null"){
                                        mDatabase.child("Users").child(user.getUid()).child("favourite").setValue(name + ",");
                                    } else {
                                        mDatabase.child("Users").child(user.getUid()).child("favourite").setValue(favourite + name + ",");
                                    }

                                }
                            }
                        }
                    }
                });
            }
        });
    }

private void setPoster(){
    mDatabase.child("Movies").child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (task.isSuccessful()){
                if (task.getResult().exists()){
                    DataSnapshot dataSnapshot = task.getResult();

                    String image_poster = String.valueOf(dataSnapshot.child("link_poster").getValue());
                    Picasso.get().load(image_poster).into(imageView_poser_small);
                    Picasso.get().load(image_poster).into(imageView_poser_big);
                }
            }
        }
    });

}

    public void setValueMovies(){
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();
        Intent intent = getIntent();
        String str = intent.getStringExtra("id");
        mDatabase.child("Movies").child(str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){

                        DataSnapshot dataSnapshot = task.getResult();
                        name = String.valueOf(dataSnapshot.child("name").getValue());
                        String cast = String.valueOf(dataSnapshot.child("cast").getValue());
                        category = String.valueOf(dataSnapshot.child("category").getValue());
                        String content = String.valueOf(dataSnapshot.child("content").getValue());
                        nation = String.valueOf(dataSnapshot.child("nation").getValue());
                        year = String.valueOf(dataSnapshot.child("year").getValue());
                        String director = String.valueOf(dataSnapshot.child("director").getValue());
                        String time = String.valueOf(dataSnapshot.child("time").getValue());
                        String viewcount = String.valueOf(dataSnapshot.child("viewcount").getValue());
                        STAR = ((Long) dataSnapshot.child("stars").child("star").getValue()).intValue();
                        OTAL_RATING = ((Long) dataSnapshot.child("stars").child("total_rating").getValue()).intValue();
                        link = String.valueOf(dataSnapshot.child("link").getValue());

                        btnPlayOnclick(link, name);

                        try {
                            float AVERRAGE = STAR / OTAL_RATING;
                            if (AVERRAGE > 4){
                                setStar5();
                            } else {
                                if (AVERRAGE > 3){
                                    setStar4();
                                } else {
                                    if (AVERRAGE > 2){
                                        setStar3();
                                    } else {
                                        if (AVERRAGE > 1){
                                            setStar2();
                                        } else {
                                            if (AVERRAGE == 0){
                                                setStar0();
                                            } else {
                                                setStar1();
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (ArithmeticException e) {
                        }
                        textView_name_movies.setText(name);
                        textView_cast_movies.setText(cast);
                        textView_category_movies.setText(category);
                        textView_review.setText(content);
                        textView_nation_movies.setText(nation);
                        textView_year_movies.setText(year);
                        textView_director_movies.setText(director);
                        textView_time_movies.setText(time + " phút");
                        textView_count_rates.setText(OTAL_RATING + " đánh giá");
                        textView_view_count_movies.setText( viewcount + " lượt");
                        setPoster();
                        getFavourite(name);
                        RateStarFragment rateStarFragment = new RateStarFragment();
                        replaceFragment(rateStarFragment, "id", name);
                    }else {
                        Toast.makeText(MoviesInformationActivity.this,"Lỗi!",Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    private void getFavourite(String name){
        mDatabase.child("Users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String favourite = String.valueOf(dataSnapshot.child("favourite").getValue());
                        if (favourite.toLowerCase().contains(name.toLowerCase())){
                            btn_favourite.setBackgroundResource(R.drawable.ic_round_favorite_30);
                        } else {
                            btn_favourite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_30);
                        }
                    }
                }
            }
        });
    }
    private void btnBackOnClick(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void btnPlayOnclick(String value, String name){
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoviesInformationActivity.this, WatchMoviesActivity.class);
                intent.putExtra("link",  value);
                intent.putExtra("name",  name);
                intent.putExtra("year",  year);
                startActivity(intent);
            }
        });
    }
    private void changeTab(){
        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_4.setBackgroundResource(R.color.Maroon);
                line_5.setBackgroundResource(0);
                textView_tab_1.setTextColor(getResources().getColor(R.color.Red));
                textView_tab_2.setTextColor(getResources().getColor(R.color.White));
                textView_tab_3.setTextColor(getResources().getColor(R.color.White));
                RateStarFragment rateStarFragment = new RateStarFragment();
                replaceFragment(rateStarFragment, "id", name);
            }
        });

        tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_4.setBackgroundResource(R.color.Maroon);
                line_5.setBackgroundResource(R.color.Maroon);
                textView_tab_1.setTextColor(getResources().getColor(R.color.White));
                textView_tab_2.setTextColor(getResources().getColor(R.color.Red));
                textView_tab_3.setTextColor(getResources().getColor(R.color.White));

                CommentsFragment commentsFragment = new CommentsFragment();
                replaceFragment(commentsFragment, "id", name);
            }
        });

        tab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_4.setBackgroundResource(0);
                line_5.setBackgroundResource(R.color.Maroon);
                textView_tab_1.setTextColor(getResources().getColor(R.color.White));
                textView_tab_2.setTextColor(getResources().getColor(R.color.White));
                textView_tab_3.setTextColor(getResources().getColor(R.color.Red));
                Intent intent = new Intent(MoviesInformationActivity.this , MoviesActivity.class);
                intent.putExtra("id", nation);
                startActivity(intent);
            }
        });
    }

    private void btnShowHideReviewOnClick(){
        btn_show_more_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkSH)
                {
                    textView_review.setMaxLines(Integer.MAX_VALUE);
                    btn_show_more_review.setText("ẩn bớt.");
                    checkSH = true;
                }
                else
                {
                    textView_review.setMaxLines(3);
                    btn_show_more_review.setText("xem thêm...");
                    checkSH = false;
                }
            }
        });
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void replaceFragment(Fragment fragment, String key, String value){
        Bundle args = new Bundle();
        args.putString(key, value);
        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    private void setStar5(){
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_24);
    }
    private void setStar4(){
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }
    private void setStar3(){
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }
    private void setStar2(){
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }
    private void setStar1(){
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }
    private void setStar0(){
        star_1.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

}
