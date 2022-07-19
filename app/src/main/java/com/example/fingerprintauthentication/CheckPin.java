package com.example.fingerprintauthentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fingerprintauthentication.utils.SecurityUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class CheckPin extends AppCompatActivity {
    private OtpTextView otpTextView;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pin);

        otpTextView = findViewById(R.id.otp_view);
        error = findViewById(R.id.error);

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {

                try {
                    String pin = new SecurityUtils().retrieveEncyrptedPasscode(CheckPin.this);
                    if (pin.equals(otp)) {
                        error.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(CheckPin.this, SuccesssAct.class);
                        CheckPin.this.startActivity(intent);
                        Toast.makeText(CheckPin.this, "The Successs  ", Toast.LENGTH_LONG).show();

                    } else if (pin.isEmpty()) {
                        error.setVisibility(View.VISIBLE);
                        error.setText("Pin is empty");
                        otpTextView.showError();
                    } else {
                        error.setVisibility(View.VISIBLE);
                        error.setText("Pin is incorrect");

                        otpTextView.showError();

                    }
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}