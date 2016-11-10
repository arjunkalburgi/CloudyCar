package com.cloudycrew.cloudycar.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.SignUpCompleteActivity;
import com.cloudycrew.cloudycar.models.User;

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
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
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

    }


    @Override
    public void onDuplicateUsernameFailure() {

    }

    @Override
    public void onSuccessfulRegistration() {
        Intent intent = new Intent(this, SignUpCompleteActivity.class);
        startActivity(intent);
        finish();
    }
}