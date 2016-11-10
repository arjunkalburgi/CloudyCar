package com.cloudycrew.cloudycar.userprofile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.User;


/**
 * Created by George on 2016-11-05.
 */

public class UserProfileActivity extends BaseActivity implements IUserProfileView {
    private UserProfileController userProfileController;
    private String username, phoneNumber, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_summary);

        Intent myIntent = getIntent();
        username = myIntent.getStringExtra("username");

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
        super.onPause();
        userProfileController.detachView();
    }

    private void resolveDependencies() {
        this.userProfileController = getCloudyCarApplication().getUserProfileController();
    }

    @Override
    public void displayLoading() {
        //lol
    }

    public void initiatePhoneCall(View v) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(callIntent);
        }
        catch (SecurityException e) {

            //If the user hasn't enabled phone calls then show them a toast instead of failing silently
            Context context = getApplicationContext();
            CharSequence text = "To make phone calls please enable phone permissions in settings.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);

            toast.show();
        }
    }

    public void initiateEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        Intent mailerIntent = Intent.createChooser(intent, null);
        startActivity(mailerIntent);
    }

    @Override
    public void displayUser(User user) {
        TextView usernameView = (TextView)findViewById(R.id.username);
        TextView phoneNumberView = (TextView)findViewById(R.id.PhoneNumberText);
        TextView emailAddressView = (TextView)findViewById(R.id.emailAddressText);

        usernameView.setText(user.getUsername());
        phoneNumber = user.getPhoneNumber().getPhoneNumber();
        phoneNumberView.setText(String.format("(%s)-%s-%s",
                                phoneNumber.substring(0, 3),
                                phoneNumber.substring(3, 6),
                                phoneNumber.substring(6)));
        email = user.getEmail().getEmail();
        emailAddressView.setText(email);

        //Probably set the image here
    }
}
