package com.cloudycrew.cloudycar.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.PhoneNumber;
import com.cloudycrew.cloudycar.models.User;

import butterknife.BindView;

public class EditProfileActivity extends BaseActivity
{
    private EditProfileController editProfileController;
    private UserController userController;
    private PhoneNumber phoneNumber;
    private String username, email, newPhoneNumber;


    @BindView(R.id.signup_email)
    protected EditText emailEditText;
    @BindView(R.id.signup_phone)
    protected EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent myIntent = getIntent();
        username = myIntent.getStringExtra("username");

        resolveDependencies();

        User localUser = userController.getCurrentUser();
        phoneNumber = localUser.getPhoneNumber();

    }

    private void resolveDependencies() {
        this.editProfileController = getCloudyCarApplication().getEditProfileController();
        this.userController = getCloudyCarApplication().getUserController();
    }
}
