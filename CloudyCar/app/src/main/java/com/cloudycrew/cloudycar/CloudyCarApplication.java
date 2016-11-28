package com.cloudycrew.cloudycar;

import android.support.multidex.MultiDexApplication;

import com.cloudycrew.cloudycar.connectivity.AndroidConnectivityService;
import com.cloudycrew.cloudycar.connectivity.IConnectivityService;
import com.cloudycrew.cloudycar.controllers.RequestController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.createrequest.CreateRequestController;
import com.cloudycrew.cloudycar.driversummary.DriverSummaryController;
import com.cloudycrew.cloudycar.elasticsearch.ElasticSearchService;
import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.fileservices.AndroidFileService;
import com.cloudycrew.cloudycar.fileservices.IFileService;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.requestdetails.RequestDetailsController;
import com.cloudycrew.cloudycar.requeststorage.CloudRequestService;
import com.cloudycrew.cloudycar.requeststorage.CompositeRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestService;
import com.cloudycrew.cloudycar.requeststorage.IRequestStore;
import com.cloudycrew.cloudycar.requeststorage.LocalRequestService;
import com.cloudycrew.cloudycar.requeststorage.PersistentRequestQueue;
import com.cloudycrew.cloudycar.requeststorage.RequestStore;
import com.cloudycrew.cloudycar.ridersummary.RiderSummaryController;
import com.cloudycrew.cloudycar.roleselection.RoleSelectionController;
import com.cloudycrew.cloudycar.scheduling.AndroidSchedulerProvider;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.search.ISearchService;
import com.cloudycrew.cloudycar.search.SearchController;
import com.cloudycrew.cloudycar.search.SearchService;
import com.cloudycrew.cloudycar.signup.SignUpController;
import com.cloudycrew.cloudycar.summarycontainer.SummaryMenuController;
import com.cloudycrew.cloudycar.userprofile.EditProfileController;
import com.cloudycrew.cloudycar.userprofile.UserProfileController;
import com.cloudycrew.cloudycar.users.IUserHistoryService;
import com.cloudycrew.cloudycar.users.IUserPreferences;
import com.cloudycrew.cloudycar.users.IUserService;
import com.cloudycrew.cloudycar.users.UserHistoryService;
import com.cloudycrew.cloudycar.users.UserPreferences;
import com.cloudycrew.cloudycar.users.UserService;
import com.cloudycrew.cloudycar.utils.RequestUtils;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import io.searchbox.client.JestClient;

/**
 * Created by George on 2016-10-25.
 */

public class CloudyCarApplication extends MultiDexApplication {
    private IRequestStore requestStore;
    private IRequestService requestService;
    private JestClient jestClient;

    private IRequestStore getRequestStore() {
        if (requestStore == null) {
            requestStore = new RequestStore(getSchedulerProvider());
        }
        return requestStore;
    }

    private IFileService getFileService() {
        return new AndroidFileService(getApplicationContext());
    }

    private IRequestService getLocalRequestService() {
        return new LocalRequestService(getFileService());
    }

    private JestClient getJestClient() {
        if (jestClient == null) {
            DroidClientConfig config = new DroidClientConfig.Builder(Constants.ELASTIC_SEARCH_BASE_URL)
                    .gson(RequestUtils.getGson())
                    .build();


            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            jestClient = factory.getObject();
        }
        return jestClient;
    }

    private IElasticSearchService<Request> getRequestElasticSearchService() {
        return new ElasticSearchService.Builder<Request>()
                .setIndex("cmput301f16t12")
                .setType("request_v2")
                .setTypeClass(Request.class)
                .setJestClient(getJestClient())
                .build();
    }

    private IElasticSearchService<User> getUserElasticSearchService() {
        return new ElasticSearchService.Builder<User>()
                .setIndex("cmput301f16t12")
                .setType("user")
                .setTypeClass(User.class)
                .setJestClient(getJestClient())
                .build();
    }

    private ISearchService getSearchService() {
        return new SearchService(getUserPreferences(), getRequestController());
    }

    private IRequestService getCloudRequestService() {
        return new CloudRequestService(getRequestElasticSearchService());
    }

    public IConnectivityService getConnectivityService() {
        return new AndroidConnectivityService(getApplicationContext());
    }

    public IRequestService getRequestService() {
        if (requestService != null) {
            return requestService;
        }
        else {
            requestService = new CompositeRequestService(getCloudRequestService(),
                    getLocalRequestService(),
                    getConnectivityService(),
                    getPersistentRequestQueue(),
                    getSchedulerProvider(),
                    getGeoDecoder());
            return  requestService;
        }
    }

    private PersistentRequestQueue getPersistentRequestQueue() {
        return new PersistentRequestQueue(getFileService());
    }

    private ISchedulerProvider getSchedulerProvider() {
        return new AndroidSchedulerProvider();
    }

    private RequestController getRequestController() {
        return new RequestController(getUserController(), getRequestStore(), getRequestService(), getSchedulerProvider());
    }

    private IUserService getUserService() {
        return new UserService(getUserElasticSearchService(), getUserPreferences());
    }

    private IUserHistoryService getUserHistoryService() {
        return new UserHistoryService(getFileService());
    }

    public IUserPreferences getUserPreferences() {
        return new UserPreferences(getApplicationContext());
    }

    public UserController getUserController() {
        return new UserController(getUserService(), getUserHistoryService());
    }

    public DriverSummaryController getDriverSummaryController() {
        return new DriverSummaryController(getRequestController(), getUserPreferences(), getRequestStore(), getSchedulerProvider());
    }

    public RiderSummaryController getRiderSummaryController() {
        return new RiderSummaryController(getRequestController(), getUserPreferences(), getRequestStore(), getSchedulerProvider());
    }

    public RequestDetailsController getRequestDetailsController(String requestId) {
        return new RequestDetailsController(requestId, getRequestController(), getSchedulerProvider(), getRequestStore());
    }

    public UserProfileController getUserProfileController() {
        return new UserProfileController(getUserController(), getSchedulerProvider());
    }

    public EditProfileController getEditProfileController() {
        return new EditProfileController(getUserController(), getSchedulerProvider());
    }

    public SearchController getSearchController() {
        return new SearchController(getSearchService(), getSchedulerProvider());
    }

    public CreateRequestController getCreateRequestController() {
        return new CreateRequestController(getRequestController(), getSchedulerProvider());
    }

    public SignUpController getSignUpController() {
        return new SignUpController(getUserController(), getSchedulerProvider());
    }

    public SummaryMenuController getSummaryMenuController() {
        return new SummaryMenuController(getUserController(), getRequestStore());
    }

    public RoleSelectionController getRoleSelectionController() {
        return new RoleSelectionController(getUserController(), getSchedulerProvider());
    }

    public GeoDecoder getGeoDecoder() {
        return new GeoDecoder(getBaseContext());
    }
}
