package com.example.designseries;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regUsername, regEmail, regPhoneno, regPassword;
    Button regBtn, regToLoginBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        regName = findViewById(R.id.full_name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhoneno = findViewById(R.id.phoneNo);
        regPassword = findViewById(R.id.password);
        regBtn = findViewById(R.id.sign_up);
        regToLoginBtn = findViewById(R.id.go_to_login);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regiaterUser(v);

            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void regiaterUser(View v) {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneno.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        if (!validateName() || !validateUsername() || !validateEmail() || !validatePassword() || !validatePhoneNo()) {
            return;
        }

        UserHelperClass helperClass = new UserHelperClass(name, username, email, password, phoneNo);
        reference.child(username).setValue(helperClass);

        Toast.makeText(this, "Your account has been created successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, VerifyOtp.class);
        intent.putExtra("phoneNo", phoneNo);
        startActivity(intent);
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        }
        else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpaces = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length()>=15){
            regUsername.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpaces)) {
            regUsername.setError("White spaces are allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("invalid email");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneno.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhoneno.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneno.setError(null);
            regPhoneno.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if(!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        }else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }


}