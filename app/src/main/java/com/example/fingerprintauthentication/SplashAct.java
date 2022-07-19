package com.example.fingerprintauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.fingerprintauthentication.utils.SecurityUtils;
import com.example.fingerprintauthentication.utils.SharedUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executor;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class SplashAct extends AppCompatActivity {
    SharedUtils sharedUtils;
    BiometricPrompt biometricPrompt;
    Integer failCounter = 0;
    Boolean isaidNo=false;
    BiometricPrompt.PromptInfo promptInfo=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedUtils = new SharedUtils();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },1000);

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {

            case BiometricManager.BIOMETRIC_SUCCESS:
                new SharedUtils().deviceSupportFingerprint(SplashAct.this, true);
                Log.e("checkme", "onCreate: yes ");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("checkme", "onCreate: no ");
                new SharedUtils().deviceSupportFingerprint(SplashAct.this, false);
                break;

        }
        Executor executor = ContextCompat.getMainExecutor(this);
         biometricPrompt = new BiometricPrompt(SplashAct.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                failCounter = failCounter + 1;
                Log.e("checkme", "onAuthenticationEror: " );

//                    biometricPrompt.cancelAuthentication();
//
//                    showBottomSheetDialog();
                if (failCounter >= 4) {
                    biometricPrompt.cancelAuthentication();
                    showBottomSheetDialog();

                }


            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashAct.this, SuccesssAct.class);
                SplashAct.this.startActivity(intent);
                isaidNo=true;
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                failCounter = failCounter + 1;
                Log.e("checkme", "onAuthenticationFailed: " );
                if (failCounter >= 4) {
                    biometricPrompt.cancelAuthentication();
                    showBottomSheetDialog();

                }
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Verify your login")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        if (!sharedUtils.getPassAuth(SplashAct.this) && !sharedUtils.getFingerAuth(SplashAct.this)) {
            Intent intent = new Intent(SplashAct.this, SuccesssAct.class);
            SplashAct.this.startActivity(intent);
            finish();
        } else if (sharedUtils.getFingerSupport(this) && sharedUtils.getFingerAuth(this)) {

            biometricPrompt.authenticate(promptInfo);
        } else {
            showBottomSheetDialog();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (isaidNo) return;
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Verify your login")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        if (!sharedUtils.getPassAuth(SplashAct.this) && !sharedUtils.getFingerAuth(SplashAct.this)) {
            Intent intent = new Intent(SplashAct.this, SuccesssAct.class);
            SplashAct.this.startActivity(intent);
            finish();
        } else if (sharedUtils.getFingerSupport(this) && sharedUtils.getFingerAuth(this)) {

            biometricPrompt.authenticate(promptInfo);
        } else {
            showBottomSheetDialog();
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        OtpTextView otpView = bottomSheetDialog.findViewById(R.id.otp_view);
        TextView error = bottomSheetDialog.findViewById(R.id.error);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
        otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {

                try {
                    String pin = new SecurityUtils().retrieveEncyrptedPasscode(SplashAct.this);
                    if (pin.equals(otp)) {
                        error.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(SplashAct.this, SuccesssAct.class);
                        SplashAct.this.startActivity(intent);
                        finish();
                        Toast.makeText(SplashAct.this, "The Successs  ", Toast.LENGTH_LONG).show();

                    } else if (pin.isEmpty()) {
                        error.setVisibility(View.VISIBLE);
                        error.setText("Pin is empty");
                        otpView.showError();
                    } else {
                        error.setVisibility(View.VISIBLE);
                        error.setText("Pin is incorrect");
                        otpView.showError();

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