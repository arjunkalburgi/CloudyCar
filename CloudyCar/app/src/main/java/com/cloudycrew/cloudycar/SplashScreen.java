package com.cloudycrew.cloudycar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread timerThread = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                } finally {
                    navigateToMainActivity();
                }
            }
        };

        timerThread.start();
    }

    private void navigateToMainActivity() {
        Intent main = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(main);
        finish();
    }
}