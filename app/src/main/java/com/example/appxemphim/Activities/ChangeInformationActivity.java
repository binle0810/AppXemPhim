package com.example.appxemphim.Activities;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appxemphim.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChangeInformationActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private ImageView Btn_change_information_back, Btn_save_information;
    private EditText Edittext_change_infor_birthday, Edittext_change_infor_name, Edittext_change_link_avatar;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private ImageView imageView_avatar;
    private String userId, link_avatar, name, birthday;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        initUi();
        getUserInfor();
        btnBackOnclick();
        btnSaveOnClick();
        pickDate();
    }
    private void initUi(){
        progressDialog = new ProgressDialog(this);
        imageView_avatar=findViewById(R.id.imageView_avatar);
        Btn_change_information_back = findViewById(R.id.btn_change_information_back);
        Btn_save_information = findViewById(R.id.btn_save_information);
        Edittext_change_infor_birthday = findViewById(R.id.edittext_change_infor_birthday);
        Edittext_change_infor_name = findViewById(R.id.edittext_change_infor_name);
        Edittext_change_link_avatar = findViewById(R.id.edittext_change_link_avatar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    }
    private void btnBackOnclick(){
        Btn_change_information_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void btnSaveOnClick(){
        Btn_save_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });
    }
    private void getUserInfor(){
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();
        userId = user.getUid();

        mDatabase.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){

                        DataSnapshot dataSnapshot = task.getResult();
                        name = String.valueOf(dataSnapshot.child("name").getValue());
                        link_avatar = String.valueOf(dataSnapshot.child("avatar").getValue());
                        birthday = String.valueOf(dataSnapshot.child("birthday").getValue());
                        String avatar = String.valueOf(dataSnapshot.child("avatar").getValue());

                        Picasso.get().load(avatar).into(imageView_avatar);

                        Edittext_change_infor_name.setText(name);
                        Edittext_change_link_avatar.setText(link_avatar);
                        Edittext_change_infor_birthday.setText(birthday);

                    }else {
                        Toast.makeText(ChangeInformationActivity.this,"Không thể tìm thấy tài khoản của bạn!",Toast.LENGTH_SHORT).show();

                    }
                }
                progressDialog.dismiss();
            }
        });
    }
    private void updateUserProfile(){
        String link_avatarUpdate = Edittext_change_link_avatar.getText().toString().trim();
        String nameUpdate = Edittext_change_infor_name.getText().toString().trim();
        String birthdaylUpdate = Edittext_change_infor_birthday.getText().toString().trim();
        userId = user.getUid();
        mDatabase.child("Users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    mDatabase.child(userId).child("avatar").setValue(link_avatarUpdate);
                    mDatabase.child(userId).child("name").setValue(nameUpdate);
                    mDatabase.child(userId).child("birthday").setValue(birthdaylUpdate);
                    Toast.makeText(ChangeInformationActivity.this,"Cập nhật thông tin thành công!",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(ChangeInformationActivity.this,"Cập nhật thông tin thất bại!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void pickDate(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        Edittext_change_infor_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ChangeInformationActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        Edittext_change_infor_birthday.setText(dateFormat.format(myCalendar.getTime()));
    }
}
