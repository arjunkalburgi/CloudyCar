package com.cloudycrew.cloudycar.users;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cloudycrew.cloudycar.userprofile.UserProfileActivity;

/**
 * Created by Ryan on 2016-11-09.
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
        this.setOnClickListener(this::UsernameOnClick);
    }

    public void UsernameOnClick(View v) {
        Intent profileView = new Intent(context, UserProfileActivity.class);
        profileView.putExtra("username", this.getText().toString());
        this.context.startActivity(profileView);
    }
}
