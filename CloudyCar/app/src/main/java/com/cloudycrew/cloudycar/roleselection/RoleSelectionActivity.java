package com.cloudycrew.cloudycar.roleselection;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.summarycontainer.SummaryActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoleSelectionActivity extends BaseActivity implements IRoleSelectionView {
    private RoleSelectionController roleSelectionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        ButterKnife.bind(this);
        resolveDependencies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        roleSelectionController.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        roleSelectionController.detachView();
    }

    @OnClick(R.id.rider_card)
    protected void onRiderCardClick() {
        startRiderSummary();
    }

    @OnClick(R.id.driver_card)
    protected void onDriverCardClick() {
        roleSelectionController.selectDriverRole();
    }

    private void resolveDependencies() {
        this.roleSelectionController = getCloudyCarApplication().getRoleSelectionController();
    }

    @Override
    public void displayAddCarDescription() {
        final View inputView = LayoutInflater.from(this).inflate(R.layout.car_info_input_dialog, null);
        Toast toast = Toast.makeText(getApplicationContext(), "You cannot be a driver without car information", Toast.LENGTH_LONG);

        new AlertDialog.Builder(this)
                .setTitle("Set Car information")
                .setView(inputView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText carDescription = (EditText) inputView.findViewById(R.id.CarInfo);
                        if (carDescription.getText().toString().trim() == "") {
                            toast.show();
                            return;
                        }
                        roleSelectionController.addCarDescription(carDescription.getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast.show();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void displayDriverSummary() {
        startDriverSummary();
    }

    @Override
    public void onCarDescriptionAdded() {
        startDriverSummary();
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
}
