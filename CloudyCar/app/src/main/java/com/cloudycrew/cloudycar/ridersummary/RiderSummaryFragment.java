package com.cloudycrew.cloudycar.ridersummary;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudycrew.cloudycar.BaseFragment;
import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.createrequest.RouteSelector;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requestdetails.RiderRequestDetailsActivity;
import com.cloudycrew.cloudycar.viewcells.AcceptedRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.BaseRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.ConfirmedRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.HeaderViewCell;
import com.cloudycrew.cloudycar.viewcells.PendingRequestViewCell;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.antonious.viewcelladapter.SectionWithHeaderViewCell;
import ca.antonious.viewcelladapter.ViewCellAdapter;
import rx.Observable;

/**
 * Created by George on 2016-11-05.
 */

public class RiderSummaryFragment extends BaseFragment implements IRiderSummaryView {
    @BindView(R.id.rider_requests)
    protected RecyclerView requestView;
    @BindView(R.id.rider_summary_swipe_container)
    protected SwipeRefreshLayout swipeRefreshLayout;

    private ViewCellAdapter viewCellAdapter;

    private SectionWithHeaderViewCell confirmedRequestsSection;
    private SectionWithHeaderViewCell acceptedRequestsSection;
    private SectionWithHeaderViewCell pendingRequestsSection;

    private RiderSummaryController riderSummaryController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rider_summary, container, false);
        ButterKnife.bind(this, view);

        resolveDependencies();
        setUpRecyclerView();
        setUpSwipeRefreshLayout();

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setActivityTitle(R.string.rider_summary_header);
    }

    @Override
    public void onResume() {
        riderSummaryController.attachView(this);
        super.onResume();
    }

    @Override
    public void onFirstResume() {
        super.onFirstResume();
        riderSummaryController.refreshRequests();
    }

    @Override
    public void onPause() {
        super.onPause();
        riderSummaryController.detachView();
    }

    private void resolveDependencies() {
        riderSummaryController = getCloudyCarApplication().getRiderSummaryController();
    }

    private void setUpSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> riderSummaryController.refreshRequests());
    }

    private void setUpRecyclerView() {
        viewCellAdapter = new ViewCellAdapter();
        viewCellAdapter.setHasStableIds(true);

        confirmedRequestsSection = new SectionWithHeaderViewCell();
        confirmedRequestsSection.setShowHeaderIfEmpty(false);
        confirmedRequestsSection.setSectionHeader(new HeaderViewCell("Confirmed Requests"));

        acceptedRequestsSection = new SectionWithHeaderViewCell();
        acceptedRequestsSection.setShowHeaderIfEmpty(false);
        acceptedRequestsSection.setSectionHeader(new HeaderViewCell("Accepted Requests"));

        pendingRequestsSection = new SectionWithHeaderViewCell();
        pendingRequestsSection.setShowHeaderIfEmpty(false);
        pendingRequestsSection.setSectionHeader(new HeaderViewCell("Pending Requests"));

        viewCellAdapter.add(confirmedRequestsSection);
        viewCellAdapter.add(acceptedRequestsSection);
        viewCellAdapter.add(pendingRequestsSection);

        viewCellAdapter.addListener(onRequestClickedListener);

        requestView.setAdapter(viewCellAdapter);
        requestView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @OnClick(R.id.fab)
    protected void startRequestActivity() {
        Intent intent = new Intent(getActivity(), RouteSelector.class);
        startActivity(intent);
    }

    private void launchRequestDetailsActivity(String requestId) {
        Intent intent = new Intent(getActivity(), RiderRequestDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_REQUEST_ID, requestId);
        startActivity(intent);
    }

    @Override
    public void displayLoading() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    public void stopLoading() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void displayPendingRequests(List<PendingRequest> pendingRequests) {
        stopLoading();
        pendingRequestsSection.setAll(getPendingRequestViewCells(pendingRequests));
        viewCellAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayAcceptedRequests(List<PendingRequest> acceptedRequests) {
        stopLoading();
        acceptedRequestsSection.setAll(getAcceptedRequestViewCells(acceptedRequests));
        viewCellAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayConfirmedRequests(List<ConfirmedRequest> confirmedRequests) {
        stopLoading();
        confirmedRequestsSection.setAll(getConfirmedRequestViewCells(confirmedRequests));
        viewCellAdapter.notifyDataSetChanged();
    }

    private BaseRequestViewCell.OnRequestClickedListener onRequestClickedListener = request -> {
        launchRequestDetailsActivity(request.getId());
    };

    private List<PendingRequestViewCell> getPendingRequestViewCells(List<? extends PendingRequest> pendingRequests) {
        return Observable.from(pendingRequests)
                         .map(PendingRequestViewCell::new)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    private List<AcceptedRequestViewCell> getAcceptedRequestViewCells(List<? extends PendingRequest> pendingRequests) {
        return Observable.from(pendingRequests)
                         .map(AcceptedRequestViewCell::new)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    private List<ConfirmedRequestViewCell> getConfirmedRequestViewCells(List<? extends ConfirmedRequest> confirmedRequests) {
        return Observable.from(confirmedRequests)
                         .map(ConfirmedRequestViewCell::new)
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<>());
    }

    // standard support library way of implementing "swipe to delete"
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
//                TestAdapter testAdapter = (TestAdapter)recyclerView.getAdapter();
//                if (testAdapter.isPendingRemoval(position)) {
//                    return 0;
//                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();

                // use position to get the request and delete only if it's a pending request
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(requestView);
    }

    // ItemDecorator for drawing the red background
    private void setUpAnimationDecoratorHelper() {
        requestView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

}
