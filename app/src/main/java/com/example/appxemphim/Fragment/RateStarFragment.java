package com.example.appxemphim.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appxemphim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class RateStarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ImageView star_1, star_2, star_3, star_4, star_5;
    private Button btn_send_rate;
    private String moviesID;
    private int STAR_COUNT = 0;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private int STAR = 0, TOTAL_RATING = 0;

    public RateStarFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_star, container, false);

        star_1 = view.findViewById(R.id.star_1);
        star_2 = view.findViewById(R.id.star_2);
        star_3 = view.findViewById(R.id.star_3);
        star_4 = view.findViewById(R.id.star_4);
        star_5 = view.findViewById(R.id.star_5);
        btn_send_rate = view.findViewById(R.id.btn_send_rate);

        moviesID = getArguments().getString("id", "");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rateStar();
        sendRateOnclick();
        showStars();


        return view;
    }

    private void sendRateOnclick(){
        btn_send_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STAR_COUNT == 0){
                    Toast.makeText(getActivity(), "Vui lòng đánh giá trước khi gửi!", Toast.LENGTH_SHORT).show();
                } else {
                    send();
                    showStars();
                }
            }
        });
    }

    private void showStars(){
        mDatabase.child("Users").child(user.getUid()).child("stars").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        try {
                            STAR_COUNT = ((Long) dataSnapshot.child(moviesID).getValue()).intValue();
                            switch (STAR_COUNT){
                                case 1:
                                    setStar1();
                                    break;
                                case 2:
                                    setStar2();
                                    break;
                                case 3:
                                    setStar3();
                                    break;
                                case 4:
                                    setStar4();
                                    break;
                                case 5:
                                    setStar5();
                                    break;
                                default:
                                    setStar0();
                                    break;
                            }
                        } catch (NullPointerException e){
                            setStar0();
                        }

                    }else {

                    }
                }
            }
        });
    }

    private void send(){
        mDatabase.child("Movies").child(moviesID).child("stars").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        STAR = ((Long) dataSnapshot.child("star").getValue()).intValue();
                        TOTAL_RATING = ((Long) dataSnapshot.child("total_rating").getValue()).intValue();
                        int new_star = STAR + STAR_COUNT;
                        int new_total_rating = TOTAL_RATING + 1;
                        stars stars = new stars(new_star, new_total_rating);
                        mDatabase.child("Movies").child(moviesID).child("stars").setValue(stars).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Đã gửi đánh giá!", Toast.LENGTH_SHORT).show();
                                } else {
                                }
                            }
                        });

                    }else {

                    }
                }
            }
        });
        mDatabase.child("Users").child(user.getUid()).child("stars").child(moviesID).setValue(STAR_COUNT).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                }
            }
        });

        mDatabase.child("Users").child(user.getUid()).child("stars").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String nameMovies = String.valueOf(dataSnapshot.child("nameMovies").getValue());
                        if (nameMovies.toLowerCase().contains(moviesID.toLowerCase())){

                        } else {
                            if (nameMovies == "null"){
                                mDatabase.child("Users").child(user.getUid()).child("stars").child("nameMovies").setValue(moviesID + ",");
                            } else {
                                mDatabase.child("Users").child(user.getUid()).child("stars").child("nameMovies").setValue(nameMovies + moviesID + ",");
                            }
                        }
                    }
                }
            }
        });
    }

    private void rateStar(){
        star_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 1;
                setStar1();
            }
        });
        star_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 2;
                setStar2();
            }
        });
        star_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 3;
                setStar3();
            }
        });
        star_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 4;
                setStar4();
            }
        });
        star_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 5;
                setStar5();
            }
        });
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


    public class stars {
        public int star, total_rating;

        public stars(){

        }

        public stars(int star, int total_rating) {
            this.star = star;
            this.total_rating = total_rating;
        }
    }
}