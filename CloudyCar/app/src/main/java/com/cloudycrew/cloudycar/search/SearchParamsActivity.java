package com.cloudycrew.cloudycar.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.Location;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchParamsActivity extends AppCompatActivity {
    @BindView(R.id.search_submit)
    protected Button submitButton;
    @BindView(R.id.search_radius_spinner)
    protected Spinner radiusSpinner;
    @BindView(R.id.search_price_wrapper)
    protected TextInputLayout searchPrice;
    @BindView(R.id.search_filter_group)
    protected RadioGroup searchFilterGroup;

    private Location userSelectedLocation;
    private int[] radiusValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_params);
        ButterKnife.bind(this);
        submitButton.setOnClickListener((view) -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_radius_choices, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radiusSpinner.setAdapter(spinnerAdapter);

        searchFilterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int checked) {
                RadioButton radioButton = (RadioButton) findViewById(checked);
                searchPrice.setHint(radioButton.getText().toString());
            }
        });
    }

    @OnClick(R.id.search_choose_location)
    public void launchLocationSearch() {
        radiusValues = getResources().getIntArray(R.array.search_radius_values);

        int radius = radiusValues[radiusSpinner.getSelectedItemPosition()];
        Intent intent = new Intent(this, LocationSearchActivity.class);
        intent.putExtra("radius", radius);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().hasExtra("location")) {
            userSelectedLocation = (Location) getIntent().getExtras().get("location");
        }
    }

}
