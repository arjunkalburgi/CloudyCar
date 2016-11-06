package com.cloudycrew.cloudycar;

import android.app.Application;

import com.cloudycrew.cloudycar.createrequest.CreateRequestController;
import com.cloudycrew.cloudycar.driversummary.DriverSummaryController;
import com.cloudycrew.cloudycar.requestdetails.RequestDetailsController;
import com.cloudycrew.cloudycar.ridersummary.RiderSummaryController;
import com.cloudycrew.cloudycar.search.SearchController;
import com.cloudycrew.cloudycar.userprofile.UserProfileController;

/**
 * Created by George on 2016-10-25.
 */

public class CloudyCarApplication extends Application {

    public DriverSummaryController getDriverSummaryController() {
        return new DriverSummaryController();
    }

    public RiderSummaryController getRiderSummaryController() {
        return new RiderSummaryController();
    }

    public RequestDetailsController getRequestDetailsController() {
        return new RequestDetailsController();
    }

    public UserProfileController getUserProfileController() {
        return new UserProfileController();
    }

    public SearchController getSearchController() {
        return new SearchController();
    }

    public CreateRequestController getCreateRequestController() {
        return new CreateRequestController();
    }
}
