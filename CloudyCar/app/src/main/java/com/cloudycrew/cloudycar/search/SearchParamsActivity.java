package com.cloudycrew.cloudycar.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    protected TextInputLayout searchPriceInput;
    @BindView(R.id.search_filter_group)
    protected RadioGroup searchFilterGroup;
    @BindView(R.id.search_keyword)
    protected EditText searchKeyword;
    @BindView(R.id.search_radio_price)
    protected RadioButton searchRadioPrice;

    private Location userSelectedLocation;
    private int[] radiusValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_params);
        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_radius_choices, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radiusSpinner.setAdapter(spinnerAdapter);

        searchFilterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int checked) {
                RadioButton radioButton = (RadioButton) findViewById(checked);
                searchPriceInput.setHint(radioButton.getText().toString());
            }
        });

        radiusSpinner.setEnabled(false);
        this.radiusValues = getResources().getIntArray(R.array.search_radius_values);
    }

    @OnClick(R.id.search_choose_location)
    public void launchLocationSearch() {
        int radius = this.radiusValues[radiusSpinner.getSelectedItemPosition()];

        Intent intent = new Intent(this, LocationSearchActivity.class);
        intent.putExtra("radius", radius);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                userSelectedLocation = (Location) intent.getSerializableExtra("location");
                radiusSpinner.setEnabled(true);
            }
        }
    }

    @OnClick(R.id.search_submit)
    public void search() {
        SearchContext searchContext = new SearchContext();
        searchContext.withKeyword(searchKeyword.getText().toString());
        String price = searchPriceInput.getEditText().getText().toString();

        if (searchRadioPrice.isChecked() && !price.isEmpty()) {
            searchContext.withPrice(Double.parseDouble(price));
        } else if (!price.isEmpty()) {
            searchContext.withPricePerKm(Double.parseDouble(price));
        }

        if (userSelectedLocation != null) {
            double lat = userSelectedLocation.getLatitude();
            double lon = userSelectedLocation.getLongitude();
            searchContext.withLocation(lat, lon,
                    this.radiusValues[radiusSpinner.getSelectedItemPosition()]);
        }

        Log.d("location", String.valueOf(searchContext.getRadius()));

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("searchcontext", searchContext);
        startActivity(intent);
    }

}
