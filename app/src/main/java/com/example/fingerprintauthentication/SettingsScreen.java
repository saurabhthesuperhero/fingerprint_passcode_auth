package com.example.fingerprintauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.fingerprintauthentication.utils.SecurityUtils;
import com.example.fingerprintauthentication.utils.SharedUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SettingsScreen extends AppCompatActivity {
    TextView fingerTv, passTV;
    SwitchCompat pass, finger;
    SharedUtils sharedUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        sharedUtils = new SharedUtils();

        pass = findViewById(R.id.passswitch);
        finger = findViewById(R.id.finger_switch);
        passTV = findViewById(R.id.tv_pass);
        fingerTv = findViewById(R.id.tv_finger);

        passTV.setOnClickListener(view -> {
            gotoSetPassCodeScreen();
        });
        pass.setChecked(sharedUtils.getPassAuth(this));
        finger.setChecked(sharedUtils.getFingerAuth(this));
        if (!sharedUtils.getFingerSupport(this)) {
            finger.setVisibility(View.INVISIBLE);
            fingerTv.setVisibility(View.INVISIBLE);

        }else{
            finger.setVisibility(View.VISIBLE);
            fingerTv.setVisibility(View.VISIBLE);
        }
        pass.setOnCheckedChangeListener((compoundButton, b) -> {
                if (checkPassCodeExist()) {
                    new SharedUtils().storePassAuth(SettingsScreen.this, b);
                }
                else
                {
                    pass.setChecked(false);
                    Toast.makeText(SettingsScreen.this, "Set Password First  ", Toast.LENGTH_LONG).show();

                }
                });

                finger.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (checkPassCodeExist()) {
                        new SharedUtils().storeFingerAuth(SettingsScreen.this, b);
                    }
                    else
                    {
                        finger.setChecked(false);
                        Toast.makeText(SettingsScreen.this, "Set Password First  ", Toast.LENGTH_LONG).show();

                    }
                });


    }

    private void gotoSetPassCodeScreen() {
        Intent intent = new Intent(SettingsScreen.this, SetPassCodeScreen.class);
        SettingsScreen.this.startActivity(intent);
    }

    private Boolean checkPassCodeExist() {
        try {
            String pin = new SecurityUtils().retrieveEncyrptedPasscode(this);
            if (pin.length() <= 3 || pin.isEmpty()) {
                return false;
            } else return true;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}