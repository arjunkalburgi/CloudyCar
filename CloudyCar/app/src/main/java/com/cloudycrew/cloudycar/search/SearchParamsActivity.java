package com.cloudycrew.cloudycar.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cloudycrew.cloudycar.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchParamsActivity extends AppCompatActivity {
    @BindView(R.id.search_submit)
    protected Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_params);
        ButterKnife.bind(this);

        submitButton.setOnClickListener((view) -> {
            startActivity(new Intent(this, SearchActivity.class));
        });
    }


}
