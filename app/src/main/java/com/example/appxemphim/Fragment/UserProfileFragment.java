package com.example.appxemphim.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.appxemphim.Activities.ChangeInformationActivity;
import com.example.appxemphim.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.appxemphim.Activities.MoviesActivity;
import com.squareup.picasso.Picasso;
import com.example.appxemphim.Activities.LoginActivity;

public class UserProfileFragment extends Fragment {

    private ProgressDialog progressDialog;
    private Button Btn_logout;
    private CardView list_favourite, list_comments, list_stars;
    private TextView Tv_user_name, Tv_user_id, Tv_user_email, Tv_user_birthday, Tv_user_joindate;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userId, email, name, birthday;
    private ImageView Btn_change_information, imageView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        initUi(view);
        getUserInfor();
        onClickLogoutBtn();
        btnChangeInformationOnclick();
        btnListFavouriteOnClick();
        btnListCommentsOnClick();
        btnListStarsOnClick();
        return view;
    }

    private void initUi(View view){
        progressDialog = new ProgressDialog(getActivity());
        Btn_logout = view.findViewById(R.id.btn_logout);
        Btn_change_information = view.findViewById(R.id.btn_change_information);
        Tv_user_name = view.findViewById(R.id.texview_user_name);
        Tv_user_email = view.findViewById(R.id.texview_user_email);
        Tv_user_id = view.findViewById(R.id.texview_user_id);
        Tv_user_birthday = view.findViewById(R.id.texview_user_birthday);
        list_favourite = view.findViewById(R.id.list_favourite);
        list_comments = view.findViewById(R.id.list_comments);
        list_stars = view.findViewById(R.id.list_stars);
        imageView = view.findViewById(R.id.imageView);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
    }


    public void btnChangeInformationOnclick(){
        Btn_change_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(getActivity(), ChangeInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnListFavouriteOnClick(){
        list_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , MoviesActivity.class);
                intent.putExtra("id",  "Danh sách phim yêu thích");
                intent.putExtra("userID",  userId);
                startActivity(intent);
            }
        });
    }
    private void btnListCommentsOnClick(){
        list_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , MoviesActivity.class);
                intent.putExtra("id",  "Danh sách phim đã bình luận");
                intent.putExtra("userID",  userId);
                startActivity(intent);
            }
        });
    }

    private void btnListStarsOnClick(){
        list_stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , MoviesActivity.class);
                intent.putExtra("id",  "Danh sách phim đã đánh giá");
                intent.putExtra("userID",  userId);
                startActivity(intent);
            }
        });
    }

    private void getUserInfor(){
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();

        mDatabase.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        name = String.valueOf(dataSnapshot.child("name").getValue());
                        email = String.valueOf(dataSnapshot.child("email").getValue());
                        birthday = String.valueOf(dataSnapshot.child("birthday").getValue());
                        String avatar = String.valueOf(dataSnapshot.child("avatar").getValue());
                        Picasso.get().load(avatar).into(imageView);
                        Tv_user_name.setText(name);
                        Tv_user_id.setText(userId);
                        Tv_user_email.setText(email);
                        Tv_user_birthday.setText(birthday);
                    }else {
                        Toast.makeText(getActivity(),"Không thể tìm thấy tài khoản của bạn!",Toast.LENGTH_SHORT).show();

                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    public void onClickLogoutBtn(){
        Btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Đang đăng xuất, Vui lòng chờ...");
                progressDialog.show();
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(getActivity(), "Đăng xuất thành công!", Toast.LENGTH_LONG).show();
                Intent i1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(i1);
                progressDialog.dismiss();
                getActivity().finish();
            }
        });
    }

}