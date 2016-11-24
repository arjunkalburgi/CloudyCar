package com.cloudycrew.cloudycar;

import android.os.Bundle;
import android.content.Intent;

import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.signup.SignUpActivity;
import com.cloudycrew.cloudycar.users.UserDoesNotExistException;

public class SplashScreen extends BaseActivity {
    private UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveDependencies();

        try {
            userController.getCurrentUser();
            startRolePickerActivity();
        }
        catch (UserDoesNotExistException e){
            startSignupActivity();
        }

    }

    private void resolveDependencies() {
        this.userController = getCloudyCarApplication().getUserController();
    }

    private void startSignupActivity() {
        Intent main = new Intent(SplashScreen.this, SignUpActivity.class);
        startActivity(main);
        finish();
    }

    private void startRolePickerActivity() {
        Intent intent = new Intent(SplashScreen.this, RoleSelectionActivity.class);
        startActivity(intent);
        finish();
    }
}