package com.example.designseries;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {

    Button verifyBtn;
    EditText otpEnteredByUser;
    TextView verification_text;
    ProgressBar progressBar;
    String verificationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        verifyBtn = findViewById(R.id.verify_btn);
        otpEnteredByUser = findViewById(R.id.otpEnteredByUser);
        progressBar = findViewById(R.id.progress_bar);
        verification_text = findViewById(R.id.verification_text);

        String phoneNo = getIntent().getStringExtra("phoneNo");
        verification_text.setText("Enter one time password sent on +91" + phoneNo);
        sendVerificationCodeToUser(phoneNo);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpEnteredByUser.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otpEnteredByUser.setError("Enter valid code");
                    otpEnteredByUser.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNo,
                60,
                TimeUnit.SECONDS,
                VerifyOtp.this,
                mCallbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, verificationCode);
        signInTheUserBycredential(credential);
    }
    private void signInTheUserBycredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOtp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(VerifyOtp.this, LoginActivity.class);
                            Toast.makeText(VerifyOtp.this, "Verification has been completed. Log into the account.", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(VerifyOtp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}