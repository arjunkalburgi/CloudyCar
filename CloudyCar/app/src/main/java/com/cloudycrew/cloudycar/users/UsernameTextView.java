package com.cloudycrew.cloudycar.users;

import android.content.Context;
import android.content.Intent;
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
        super(contextin);
        this.context = contextin;
        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UsernameOnClick(v);
            }
        });
    }

    public void UsernameOnClick(View v) {
        Intent profileView = new Intent(context, UserProfileActivity.class);
        profileView.putExtra("username", this.getText().toString());
        this.context.startActivity(profileView);
    }
}
