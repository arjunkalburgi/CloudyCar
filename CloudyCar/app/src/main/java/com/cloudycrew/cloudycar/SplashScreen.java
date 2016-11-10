package com.cloudycrew.cloudycar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import com.cloudycrew.cloudycar.signup.SignUpActivity;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent main = new Intent(SplashScreen.this, SignUpActivity.class);
        startActivity(main);
        finish();
    }
}