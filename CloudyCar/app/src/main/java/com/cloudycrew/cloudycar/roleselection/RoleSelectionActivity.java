package com.cloudycrew.cloudycar.roleselection;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.SummaryActivity;
import com.cloudycrew.cloudycar.users.IUserPreferences;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoleSelectionActivity extends BaseActivity {
    private IUserPreferences userPreferences;
    private RoleSelectionController roleSelectionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        ButterKnife.bind(this);
        resolveDependencies();
    }

    private void resolveDependencies() {
        this.userPreferences = getCloudyCarApplication().getUserPreferences();
        this.roleSelectionController = getCloudyCarApplication().getRoleSelectionController();
    }

    @OnClick(R.id.rider_card)
    protected void onRiderCardClick() {
        startRiderSummary();
    }

    @OnClick(R.id.driver_card)
    protected void onDriverCardClick() {
//        if (user.hasCarDescription()) {
//            startActivity(intent);
//        } else {
//            startAddVehicleDescription();
//        }
    }

    private void startRiderSummary() {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra("mode", "rider");
        startActivity(intent);
    }

    private void startDriverSummary() {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra("mode", "driver");
        startActivity(intent);
    }

    private void startAddVehicleDescription() {
        final View inputView = LayoutInflater.from(this).inflate(R.layout.car_info_input_dialog, null);

        new AlertDialog.Builder(this)
                .setTitle("Set Car information")
                .setView(inputView)
                .setPositiveButton("Add", (dialog, which) -> {
                    EditText carDescription = (EditText) inputView.findViewById(R.id.CarInfo);
                    roleSelectionController.addCarDescription(carDescription.getText().toString().trim());
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast toast = Toast.makeText(getApplicationContext(), "You cannot be a driver without car information", Toast.LENGTH_LONG);
                    toast.show();
                })
                .create()
                .show();
    }
}
