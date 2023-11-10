package com.example.appxemphim.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appxemphim.Activities.AddMoviesActivity;
import com.example.appxemphim.R;
import com.example.appxemphim.Activities.ReviewProductActivity;
import com.example.appxemphim.Activities.ShareAppActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SettingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout linearLayout_add_movies;
    private Button btn_add_movies,btn_rating,btn_shareapp;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String mParam1;
    private String mParam2;
    public SettingFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        initUI(view);
        checkAdmin();
        addMoviesOnClick();
        ratingapp();
        shareapp();
        return view;
    }
private  void initUI(View view){
    linearLayout_add_movies = view.findViewById(R.id.linearLayout_add_movies);
    btn_add_movies = view.findViewById(R.id.btn_add_movies);
    linearLayout_add_movies.findViewById(R.id.linearLayout_add_movies).setVisibility(View.INVISIBLE);
    btn_rating=view.findViewById(R.id.btn_rating);
    btn_shareapp=view.findViewById(R.id.btn_shareapp);

}
    public void addMoviesOnClick() {
        btn_add_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getActivity(), AddMoviesActivity.class);
                startActivity(i1);
            }
        });
    }
    public void ratingapp() {
        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getActivity(), ReviewProductActivity.class);
                startActivity(i1);
            }
        });
    }
    public void shareapp() {
        btn_shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getActivity(), ShareAppActivity.class);
                startActivity(i1);
            }
        });
    }



    private void checkAdmin(){

        String userId = user.getUid();
        mDatabase.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        int CHECK_ADMIN = dataSnapshot.child("Admin").getValue(Integer.class);
                        if (CHECK_ADMIN == 1){
                            linearLayout_add_movies.findViewById(R.id.linearLayout_add_movies).setVisibility(View.VISIBLE);
                        } else {
                            linearLayout_add_movies.findViewById(R.id.linearLayout_add_movies).setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
    }

}