package com.cloudycrew.cloudycar.ridersummary;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.cloudycrew.cloudycar.viewcells.BaseRequestViewHolder;
import com.cloudycrew.cloudycar.viewcells.ConfirmedRequestViewCell;
import com.cloudycrew.cloudycar.viewcells.HeaderViewCell;
import com.cloudycrew.cloudycar.viewcells.PendingRequestViewCell;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.antonious.viewcelladapter.SectionWithHeaderViewCell;
import ca.antonious.viewcelladapter.ViewCell;
import ca.antonious.viewcelladapter.ViewCellAdapter;
import rx.Observable;
import rx.functions.Func1;

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

    private List<ViewCell> pendingRemovals = new ArrayList<ViewCell>();

    private RiderSummaryController riderSummaryController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rider_summary, container, false);
        ButterKnife.bind(this, view);

        resolveDependencies();
        setUpRecyclerView();
        setUpSwipeRefreshLayout();

        setUpItemTouchHelper(view);

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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                riderSummaryController.refreshRequests();
            }
        });
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
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
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

    private BaseRequestViewCell.OnRequestClickedListener onRequestClickedListener = new BaseRequestViewCell.OnRequestClickedListener() {
        @Override
        public void onRequestClicked(Request request) {
            launchRequestDetailsActivity(request.getId());

        }
    };

    private List<PendingRequestViewCell> getPendingRequestViewCells(List<? extends PendingRequest> pendingRequests) {
        return Observable.from(pendingRequests)
                         .map(new Func1<PendingRequest, PendingRequestViewCell>() {
                             @Override
                             public PendingRequestViewCell call(PendingRequest pendingRequest) {
                                 return new PendingRequestViewCell(pendingRequest);
                             }
                         })
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<PendingRequestViewCell>());
    }

    private List<AcceptedRequestViewCell> getAcceptedRequestViewCells(List<? extends PendingRequest> pendingRequests) {
        return Observable.from(pendingRequests)
                         .map(new Func1<PendingRequest, AcceptedRequestViewCell>() {
                             @Override
                             public AcceptedRequestViewCell call(PendingRequest pendingRequest) {
                                 return new AcceptedRequestViewCell(pendingRequest);
                             }
                         })
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<AcceptedRequestViewCell>());
    }

    private List<ConfirmedRequestViewCell> getConfirmedRequestViewCells(List<? extends ConfirmedRequest> confirmedRequests) {
        return Observable.from(confirmedRequests)
                         .map(new Func1<ConfirmedRequest, ConfirmedRequestViewCell>() {
                             @Override
                             public ConfirmedRequestViewCell call(ConfirmedRequest confirmedRequest) {
                                 return new ConfirmedRequestViewCell(confirmedRequest);
                             }
                         })
                         .toList()
                         .toBlocking()
                         .firstOrDefault(new ArrayList<ConfirmedRequestViewCell>());
    }

    private boolean isViewCellSwipeable(ViewCell viewCell) {
        return viewCell instanceof PendingRequestViewCell ||
                viewCell instanceof AcceptedRequestViewCell;
    }

    // From https://github.com/nemanja-kovacevic/recycler-view-swipe-to-delete
    private void undoPendingDeletes() {
        for (ViewCell pendingRemoval : pendingRemovals) {
            if (pendingRemoval instanceof PendingRequestViewCell) {
                pendingRequestsSection.add(pendingRemoval);
            } else if (pendingRemoval instanceof AcceptedRequestViewCell) {
                acceptedRequestsSection.add(pendingRemoval);
            }
        }

        pendingRemovals.clear();
        viewCellAdapter.notifyDataSetChanged();
    }

    private void executePendingDeletes() {
        for (ViewCell pendingRemoval : pendingRemovals) {
            if (pendingRemoval instanceof  BaseRequestViewCell) {
                Request request = (PendingRequest)((BaseRequestViewCell) pendingRemoval).getModel();
                riderSummaryController.deleteRequest(request.getId());
            }
            pendingRequestsSection.add(pendingRemoval);
        }

        pendingRemovals.clear();
    }

    // standard support library way of implementing "swipe to delete"
    private void setUpItemTouchHelper(final View v) {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;
            //From: http://www.androidhive.info/2015/09/android-material-design-snackbar-example/
            Snackbar snackbar = Snackbar
                    .make(v, "Request cancelled", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            undoPendingDeletes();
                        }
                    })
                    .setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event){
                            executePendingDeletes();
                        }
                    });

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.deleteRed));
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;

                snackbar.setActionTextColor(Color.WHITE);
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                ViewCell viewCell = viewCellAdapter.get(position);

                if (!isViewCellSwipeable(viewCell)) {
                    return 0;
                }

                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ViewCell viewCell = viewCellAdapter.get(viewHolder.getAdapterPosition());
                if (isViewCellSwipeable(viewCell)) {
                    snackbar.show();
                    viewCellAdapter.remove(viewHolder.getAdapterPosition());
                    viewCellAdapter.notifyDataSetChanged();
                    pendingRemovals.add(viewCell);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getLeft() + xMarkMargin;
                int xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
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

}
