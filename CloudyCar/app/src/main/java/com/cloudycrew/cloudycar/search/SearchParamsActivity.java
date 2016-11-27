package com.cloudycrew.cloudycar.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.Point;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchParamsActivity extends AppCompatActivity {
    @BindView(R.id.search_submit)
    protected Button submitButton;
    @BindView(R.id.radius_entry)
    protected EditText radiusEntry;

    private Point userSelectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_params);
        ButterKnife.bind(this);
        submitButton.setOnClickListener((view) -> {
            startActivity(new Intent(this, SearchActivity.class));
        });
    }

    @OnClick(R.id.enter_location_search)
    public void launchLocationSearch(){
        Double radius = Double.parseDouble(radiusEntry.getText().toString());
        Bundle b = new Bundle();
        b.putDouble("radius",radius);
        Intent intent = new Intent(this,LocationSearchActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(getIntent().hasExtra("location")){
            userSelectedLocation= (Point)getIntent().getExtras().get("location");
        }
    }



}
