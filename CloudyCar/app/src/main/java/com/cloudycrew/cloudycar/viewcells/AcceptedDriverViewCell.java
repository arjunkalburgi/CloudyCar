package com.cloudycrew.cloudycar.viewcells;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudycrew.cloudycar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.antonious.viewcelladapter.BaseViewHolder;
import ca.antonious.viewcelladapter.GenericSingleViewCell;

/**
 * Created by George on 2016-11-19.
 */

public class AcceptedDriverViewCell extends GenericSingleViewCell<AcceptedDriverViewCell.ViewHolder, String> {
    private OnConfirmClickedListener onConfirmClickedListener;

    public AcceptedDriverViewCell(String model) {
        super(model);
    }

    @Override
    public int getLayoutId() {
        return R.layout.accepted_driver_list_item;
    }

    @Override
    public void bindViewCell(ViewHolder viewHolder) {
        final String username = getModel();

        viewHolder.reset();
        viewHolder.setUsername(username);

        viewHolder.setOnConfirmClickedListener(v -> {
            viewHolder.showLoading();
            dispatchOnConfirmClicked(username);
        });
    }

    public interface OnConfirmClickedListener {
        void onConfirm(String username);
    }

    public OnConfirmClickedListener getOnConfirmClickedListener() {
        return onConfirmClickedListener;
    }

    public void setOnConfirmClickedListener(OnConfirmClickedListener onConfirmClickedListener) {
        this.onConfirmClickedListener = onConfirmClickedListener;
    }

    public void dispatchOnConfirmClicked(String username) {
        if (getOnConfirmClickedListener() != null) {
            getOnConfirmClickedListener().onConfirm(username);
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.driver_username)
        protected TextView driverUsername;
        @BindView(R.id.confirm_loading_indicator)
        protected ProgressBar confirmLoadingIndicator;
        @BindView(R.id.confirm_driver_button)
        protected Button confirmDriverButton;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            confirmLoadingIndicator.setVisibility(View.GONE);
        }

        public void setUsername(String username) {
            driverUsername.setText(username);
        }

        public void showLoading() {
            confirmDriverButton.setVisibility(View.GONE);
            confirmLoadingIndicator.setVisibility(View.VISIBLE);
        }

        public void reset() {
            confirmDriverButton.setVisibility(View.VISIBLE);
            confirmLoadingIndicator.setVisibility(View.GONE);
        }

        public void setOnConfirmClickedListener(View.OnClickListener onClickListener) {
            confirmDriverButton.setOnClickListener(onClickListener);
        }
    }
}
