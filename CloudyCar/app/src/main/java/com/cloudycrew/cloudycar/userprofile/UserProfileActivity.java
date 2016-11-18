package com.cloudycrew.cloudycar.userprofile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.PhoneNumber;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.signup.SignUpActivity;


/**
 * Created by George on 2016-11-05.
 */

public class UserProfileActivity extends BaseActivity implements IUserProfileView {
    private static final int REQUEST_PHONE_PERMISSIONS = 2;
    private UserProfileController userProfileController;
    private UserController userController;

    private String username,email;
    private PhoneNumber phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_summary);
        Intent myIntent = getIntent();
        username = myIntent.getStringExtra("username");

        resolveDependencies();
        ImageButton userDetailsButton = (ImageButton) findViewById(R.id.editUserDetailsButton);
        if (!username.equals(userController.getCurrentUser().getUsername())) {

            userDetailsButton.setVisibility(View.GONE);
            userDetailsButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //This too shall pass
                }
            });
        } else {
            userDetailsButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    editUserDetails();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userProfileController.attachView(this);
        userProfileController.loadUser(username);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userProfileController.detachView();
    }

    private void resolveDependencies() {
        this.userProfileController = getCloudyCarApplication().getUserProfileController();
        this.userController = getCloudyCarApplication().getUserController();
    }

    @Override
    public void displayLoading() {
        //lol
    }

    /**
     * Launches an intent to edit the current user
     */
    protected void editUserDetails() {
        Intent editingIntent = new Intent(this, EditProfileActivity.class);
        editingIntent.putExtra("username", username);
        startActivity(editingIntent);
    }

    /**
     * Initiates a phone call to the current user
     * @param v
     */
    public void initiatePhoneCall(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSIONS);
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber.getPhoneNumber()));
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

    /**
     * Initiates an email to the current user
     * @param v
     */
    public void initiateEmail(View v) {
        //From https://developer.android.com/guide/components/intents-common.html#Email
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void displayUser(User user) {
        TextView usernameView = (TextView)findViewById(R.id.username);
        TextView phoneNumberView = (TextView)findViewById(R.id.PhoneNumberText);
        TextView emailAddressView = (TextView)findViewById(R.id.emailAddressText);

        usernameView.setText(user.getUsername());
        phoneNumber = user.getPhoneNumber();
        phoneNumberView.setText(phoneNumber.prettyPrint());
        email = user.getEmail().getEmail();
        emailAddressView.setText(email);

        //Probably set the image here
    }

    public void displayErrorToast() {
        //If the user hasn't enabled phone calls then show them a toast instead of failing silently
        Context context = getApplicationContext();
        CharSequence text = "That user doesn't exist anymore.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);

        toast.show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /**
         * Kiuwan suggests that this should have a defualt in the switch. After closer inspection
         * this should probably just be an if since we don't actually look for any other cases.
         */
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Phone permission granted", Toast.LENGTH_SHORT).show();
                    this.recreate();
                } else {
                    Toast.makeText(this, "No phone permissions", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
