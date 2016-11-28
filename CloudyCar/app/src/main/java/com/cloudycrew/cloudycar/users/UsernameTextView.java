package com.cloudycrew.cloudycar.users;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.userprofile.UserProfileActivity;

/**
 * Created by Ryan on 2016-11-09.
 *
 * A textview specifically for displaying usernames. Provides a centralized way of fetching the
 * information from that username and displaying it to the user.
 *
 * Given the username it will navigate to the user profile activity.
 */

public class UsernameTextView extends TextView
{
    private final Context context;

    public UsernameTextView(Context contextin) {
        this(contextin, null);
    }

    public UsernameTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UsernameTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UsernameOnClick(v);
            }
        });
    }

    /**
     * On click handler of the UsernameTextView, creates and launches a new intent to the
     * user profile activity
     *
     * @param v
     */
    public void UsernameOnClick(View v) {
        Intent profileView = new Intent(context, UserProfileActivity.class);
        profileView.putExtra("username", this.getText().toString());
        this.context.startActivity(profileView);
    }
}
