package com.example.appxemphim.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appxemphim.R;
import com.example.appxemphim.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;
import android.content.Context;
public class SignUpActivity extends AppCompatActivity {

    private TextView Tv_login;
    private Button Btn_signup;
    private EditText Edt_email, Edt_password, Edt_confirm_password, Edt_signup_user_birthday, Edt_signup_user_name;
    private TextInputLayout tiplayout_email, tiplayout_pass, tiplayout_confirm_pass, tiplayout_signup_user_name, tiplayout_signup_user_birthday;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private String userId,strPass;
    private CheckBox checkboxAdmin;
    private SharedPreferences sharedPreferences;
    final Calendar myCalendar= Calendar.getInstance();
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUi();
        pickDate();
        initListener();
    }


    private void initUi(){
        Tv_login = findViewById(R.id.tv_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Edt_email = findViewById(R.id.edittext_signup_email);
        Edt_password = findViewById(R.id.edittext_signup_password);
        Edt_confirm_password = findViewById(R.id.edittext_signup_confirm_password);
        Edt_signup_user_birthday = findViewById(R.id.edittext_signup_user_birthday);
        Edt_signup_user_name = findViewById(R.id.edittext_signup_user_name);
        checkboxAdmin = findViewById(R.id.checkbox_admin);
        Btn_signup = findViewById(R.id.btn_signup);
        tiplayout_email = findViewById(R.id.tiplayout_signup_email);
        tiplayout_pass = findViewById(R.id.tiplayout_signup_password);
        tiplayout_confirm_pass = findViewById(R.id.tiplayout_signup_confirm_password);
        tiplayout_signup_user_name = findViewById(R.id.tiplayout_signup_user_name);
        tiplayout_signup_user_birthday = findViewById(R.id.tiplayout_signup_user_birthday);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("remember_login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void initListener() {
        Tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(itent);
            }
        });

        btnRegisterOnClick();
    }

    public void btnRegisterOnClick(){
        Btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(Edt_signup_user_name.getText().toString())) {
                    tiplayout_signup_user_name.setError("Chỗ này không được bỏ trống");
                }
                else {
                    tiplayout_signup_user_name.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(Edt_signup_user_birthday.getText().toString())) {
                    tiplayout_signup_user_birthday.setError("Chỗ này không được bỏ trống");
                }
                else {
                    tiplayout_signup_user_birthday.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(Edt_email.getText().toString())) {
                    tiplayout_email.setError("Chỗ này không được bỏ trống");
                }
                else {
                    tiplayout_email.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(Edt_password.getText().toString())) {
                    tiplayout_pass.setError("Chỗ này không được bỏ trống");
                }
                else {
                    tiplayout_pass.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(Edt_confirm_password.getText().toString())) {
                    tiplayout_confirm_pass.setError("Chỗ này không được bỏ trống");
                }
                else {
                    tiplayout_confirm_pass.setErrorEnabled(false);
                }
                String email = Edt_email.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (!email.matches(emailPattern))
                {
                    tiplayout_email.setError("Địa chỉ email không hợp lệ");
                }

                if (Edt_password.getText().toString().trim().length() >= 6){
                    if (Edt_email.getText().toString().trim().equalsIgnoreCase("") == false
                            && Edt_password.getText().toString().trim().equalsIgnoreCase("")  == false
                            && Edt_signup_user_name.getText().toString().trim().equalsIgnoreCase("")  == false
                            && Edt_signup_user_birthday.getText().toString().trim().equalsIgnoreCase("")  == false
                            && Edt_confirm_password.getText().toString().trim().equalsIgnoreCase("") == false
                            && email.matches(emailPattern)
                           ){
                        if (Edt_password.getText().toString().trim().equals(Edt_confirm_password.getText().toString().trim())){
                            strPass=Edt_password.getText().toString().trim();
                            createAccount();
                        } else {
                            tiplayout_confirm_pass.setError("Mật khẩu nhập lại không chính xác");
                        }
                    }
                } else {
                    tiplayout_pass.setError("Mật khẩu phải >6 ký tự");
                }
            }
        });
    }


    private void createAccount() {

        final String strEmail = Edt_email.getText().toString().trim();
         strPass = Edt_password.getText().toString().trim();
        final String strName = Edt_signup_user_name.getText().toString().trim();
        final String strBirthday = Edt_signup_user_birthday.getText().toString().trim();

        progressDialog.setMessage("Đang tạo tài khoản");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(strEmail, strPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userId = mAuth.getCurrentUser().getUid();
                            writeNewUser(strName, strEmail, strBirthday);
                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void writeNewUser(String name, String email , String birthday) {
        int admin;
        if (checkboxAdmin.isChecked()) {
             admin = 1;
        }else {
             admin = 0;
        }

        String avatar = "https://firebasestorage.googleapis.com/v0/b/mobileapp-80749.appspot.com/o/user%20(3).png?alt=media&token=20d7ebcb-ccbb-4e58-8608-05654c3b4c52";
        User user = new User(name, email, birthday, admin, avatar);

        mDatabase.child("Users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,"Đăng kí thành công", Toast.LENGTH_LONG).show();
                    editor.putBoolean("checked", true);
                    editor.putString("email", email);
                    editor.putString("password", strPass);
                    editor.commit();
                    Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
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
        Edt_signup_user_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        Edt_signup_user_birthday.setText(dateFormat.format(myCalendar.getTime()));
    }

}