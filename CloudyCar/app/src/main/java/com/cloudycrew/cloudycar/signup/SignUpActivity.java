package com.cloudycrew.cloudycar.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.EditText;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.SignUpCompleteActivity;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.users.UserPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements ISignUpView {
    @BindView(R.id.signup_username)
    protected EditText usernameEditText;
    @BindView(R.id.signup_email)
    protected EditText emailEditText;
    @BindView(R.id.signup_phone)
    protected EditText phoneEditText;

    private SignUpController signUpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        resolveDependencies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        signUpController.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        signUpController.detachView();
    }

    private void resolveDependencies() {
        signUpController = getCloudyCarApplication().getSignUpController();
    }


    @OnClick(R.id.startButton)
    protected void onStartButtonClicked() {
        signUpController.registerUser(usernameEditText.getText().toString(),
                                      emailEditText.getText().toString(),
                                      phoneEditText.getText().toString());
    }

    @Override
    public void onMalformedUserFailure() {
        Snackbar.make(phoneEditText, R.string.sign_up_malformed_user_message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDuplicateUsernameFailure() {
        Snackbar.make(phoneEditText, R.string.sign_up_duplicate_user_message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSuccessfulRegistration() {
        Intent intent = new Intent(this, SignUpCompleteActivity.class);
        startActivity(intent);
        finish();
    }
}