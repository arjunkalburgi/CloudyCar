package com.cloudycrew.cloudycar.userprofile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.elasticsearch.ElasticSearchService;
import com.cloudycrew.cloudycar.models.User;

import org.w3c.dom.Text;

/**
 * Created by George on 2016-11-05.
 */

public class UserProfileActivity extends BaseActivity implements IUserProfileView {
    private UserProfileController userProfileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_summary);

        Intent myIntent = getIntent();
        String username = myIntent.getStringExtra("username");

        resolveDependencies();

        userProfileController.loadUser(username);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userProfileController.attachView(this);
    }

    @Override
    protected void onPause() {
        userProfileController.detachView();
    }

    private void resolveDependencies() {
        this.userProfileController = getCloudyCarApplication().getUserProfileController();
    }

    @Override
    public void displayLoading() {

    }

    @Override
    public void displayUser(User user) {
        TextView usernameView = (TextView)findViewById(R.id.username);
        TextView phoneNumberView = (TextView)findViewById(R.id.PhoneNumberText);
        TextView emailAddressView = (TextView)findViewById(R.id.emailAddressText);

        usernameView.setText(user.getUsername());
        phoneNumberView.setText(user.getPhoneNumber().getPhoneNumber());
        emailAddressView.setText(user.getEmail().getEmail());

        //Probably set the image here
    }
}
