package com.cloudycrew.cloudycar.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.ISignUpView;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.SignUpCompleteActivity;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.email.Email;
import com.cloudycrew.cloudycar.models.PhoneNumber;
import com.cloudycrew.cloudycar.models.User;

public class SignUpActivity extends BaseActivity implements ISignUpView {

    String username;
    Email email;
    PhoneNumber phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        EditText usernamestr = (EditText) this.findViewById(R.id.signup_username);
        username = usernamestr.getText().toString();
        EditText emailstr = (EditText) this.findViewById(R.id.signup_email);
        email = new Email(emailstr.getText().toString());
        EditText phonestr = (EditText) this.findViewById(R.id.signup_phone);
        phoneNumber = new PhoneNumber(phonestr.getText().toString());

        Button lessgo = (Button) findViewById(R.id.startButton);
        lessgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    public void signUp() {
        User u = new User(username);
        u.setEmail(email);
        u.setPhoneNumber(phoneNumber);

        //TODO: Make user!

        Intent intent = new Intent(this, SignUpCompleteActivity.class);
        startActivity(intent);
    }
}