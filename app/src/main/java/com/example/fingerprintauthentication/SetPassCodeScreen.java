package com.example.fingerprintauthentication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.fingerprintauthentication.utils.SecurityUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class SetPassCodeScreen extends AppCompatActivity {
    private OtpTextView otpTextView,otpTextView2;
    private String oldpass;
    private String newpass;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        otpTextView = findViewById(R.id.otp_view);
        otpTextView2 = findViewById(R.id.otp_view2);
        confirm = findViewById(R.id.confirm);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
            }

            @Override
            public void onOTPComplete(String pin) {

                    oldpass=pin;
            }
        });
        otpTextView2.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                
            }

            @Override
            public void onOTPComplete(String otp) {
                newpass=otp;
                if (!oldpass.equals(newpass)){
                    Toast.makeText(SetPassCodeScreen.this,"Password doesnt match",Toast.LENGTH_LONG).show();
                    otpTextView2.showError();
                    otpTextView.showError();
                }
                else {
                    otpTextView2.showSuccess();
                    otpTextView.showSuccess();
                }
            }
        });

        confirm.setOnClickListener(view -> {
            if (oldpass.isEmpty() || newpass.isEmpty() || !oldpass.equalsIgnoreCase(newpass)) {
                otpTextView2.showError();
                otpTextView.showError();
                return;
            }
            try {
                new SecurityUtils().storedEncryptedPassCode(newpass, SetPassCodeScreen.this);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            onBackPressed();
        });
    }
}