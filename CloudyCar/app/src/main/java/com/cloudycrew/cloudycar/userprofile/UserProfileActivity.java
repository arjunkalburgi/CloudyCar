package com.cloudycrew.cloudycar.userprofile;

import android.os.Bundle;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.models.User;

/**
 * Created by George on 2016-11-05.
 */

public class UserProfileActivity extends BaseActivity implements IUserProfileView {
    private UserProfileController userProfileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: setContentView

        resolveDependencies();
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

    }
}
