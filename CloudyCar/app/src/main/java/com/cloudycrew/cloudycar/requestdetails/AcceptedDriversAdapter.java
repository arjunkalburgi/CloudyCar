package com.cloudycrew.cloudycar.requestdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cloudycrew.cloudycar.ArrayAdapter;
import com.cloudycrew.cloudycar.BaseViewHolder;
import com.cloudycrew.cloudycar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George on 2016-11-11.
 */

public class AcceptedDriversAdapter extends ArrayAdapter<String, AcceptedDriversAdapter.ViewHolder> {
    private OnConfirmClickedListener onConfirmClickedListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.accepted_driver_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        final String username = get(position);

        holder.setUsername(username);
        holder.setOnConfirmClickedListener(v -> dispatchOnConfirmClicked(position, username));
    }

    public interface OnConfirmClickedListener {
        void onConfirm(int position, String username);
    }

    public OnConfirmClickedListener getOnConfirmClickedListener() {
        return onConfirmClickedListener;
    }

    public void setOnConfirmClickedListener(OnConfirmClickedListener onConfirmClickedListener) {
        this.onConfirmClickedListener = onConfirmClickedListener;
    }

    public void dispatchOnConfirmClicked(int position, String username) {
        if (getOnConfirmClickedListener() != null) {
            getOnConfirmClickedListener().onConfirm(position, username);
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.driver_username)
        protected TextView driverUsername;
        @BindView(R.id.confirm_driver_button)
        protected Button confirmDriverButton;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setUsername(String username) {
            driverUsername.setText(username);
        }

        public void setOnConfirmClickedListener(View.OnClickListener onClickListener) {
            confirmDriverButton.setOnClickListener(onClickListener);
        }
    }
}
