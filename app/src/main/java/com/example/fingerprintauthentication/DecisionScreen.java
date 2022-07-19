package com.example.fingerprintauthentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.fingerprintauthentication.utils.SecurityUtils;
import com.example.fingerprintauthentication.utils.SharedUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executor;

public class DecisionScreen extends AppCompatActivity {
    SharedUtils sharedUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedUtils=new SharedUtils();
        setContentView(R.layout.activity_main);
        TextView msgtex = findViewById(R.id.msgtext);
        TextView pass = findViewById(R.id.textView3);
        final ImageButton loginbutton = findViewById(R.id.login);


        pass.setOnClickListener(view -> gotoCheckPassCodeScreen());


        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {

            case BiometricManager.BIOMETRIC_SUCCESS:
                new SharedUtils().deviceSupportFingerprint(DecisionScreen.this, true);
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                loginbutton.setVisibility(View.GONE);
                new SharedUtils().deviceSupportFingerprint(DecisionScreen.this, false);
                break;

        }
        Executor executor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(DecisionScreen.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                gotoSuccessScreen();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });





        if (sharedUtils.getFingerSupport(this) && sharedUtils.getFingerAuth(this)) {
            final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("helo")
                    .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
            loginbutton.setOnClickListener(v -> {
                biometricPrompt.authenticate(promptInfo);
            });

        }

       else if (sharedUtils.getPassAuth(DecisionScreen.this))
        {
            gotoCheckPassCodeScreen();
            finish();


        }
       else {
            gotoSuccessScreen();
            finish();
        }

    }

    private Boolean checkPassCodeExist() {
        try {
            String pin = new SecurityUtils().retrieveEncyrptedPasscode(this);
            if (pin.length() <= 3 || pin.isEmpty()){ return false;} else return true;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void gotoCheckPassCodeScreen() {
        Intent intent = new Intent(DecisionScreen.this, CheckPin.class);
        DecisionScreen.this.startActivity(intent);
    }

    private void gotoSetPassCodeScreen() {
        Intent intent = new Intent(DecisionScreen.this, SetPassCodeScreen.class);
        DecisionScreen.this.startActivity(intent);
    }


    private void gotoSuccessScreen() {
        Intent intent = new Intent(DecisionScreen.this, SuccesssAct.class);
        DecisionScreen.this.startActivity(intent);
    }


}