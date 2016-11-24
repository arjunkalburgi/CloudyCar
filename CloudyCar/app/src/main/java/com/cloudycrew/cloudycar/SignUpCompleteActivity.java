package com.cloudycrew.cloudycar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpCompleteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_complete);

        CardView riderCV = (CardView)findViewById(R.id.rider_card);
        riderCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRiderSummary();
            }
        });

        CardView driverCV = (CardView)findViewById(R.id.driver_card);
        driverCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDriverSummary();
            }
        });

    }

    private void startRiderSummary() {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra("mode", "rider");
        startActivity(intent);
    }

    private void startDriverSummary() {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra("mode", "driver");
        // TODO implment logic!! 
//         if (user.hasCarDescription()) {
//             startActivity(intent);
//         } else {
        startAddVehicleDescription();
//         }
    }

    private void startAddVehicleDescription() {
        final View inputView = LayoutInflater.from(this).inflate(R.layout.car_info_input_dialog, null);
        Toast toast = Toast.makeText(getApplicationContext(), "You cannot be a driver without a description of your car", Toast.LENGTH_LONG);
        Activity a = this;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Set Car information")
                .setView(inputView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("SignUpCompleteActivity", "Thing");
                        EditText carDescription = (EditText) inputView.findViewById(R.id.CarInfo);
                        if (carDescription.getText().toString().trim() == "") {
                            toast.show();
                            return;
                        }

                        // TODO: add car description to user.

                        Intent intent = new Intent(a, SummaryActivity.class);
                        intent.putExtra("mode", "driver");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast.show();
                    }
                })
                .create();
        dialog.show();
    }

}
