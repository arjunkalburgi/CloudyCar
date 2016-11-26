package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.fileservices.IFileService;
import com.cloudycrew.cloudycar.models.requests.CancelledRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.utils.RequestUtils;
import com.cloudycrew.cloudycar.utils.StringUtils;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ryan on 2016-11-24.
 */

public class PersistentRequestQueue {
    private List<Request> createQueue;
    private List<PendingRequest> acceptQueue;
    private List<CancelledRequest> cancelQueue;
    private List<ConfirmedRequest> confirmQueue;

    //private Map<String, List<Request>> queueMap;

    private static final String MAP_CREATE = "Created";
    private static final String MAP_ACCEPT = "Accepted";
    private static final String MAP_CANCEL = "Cancelled";
    private static final String MAP_CONFIRM = "Confirmed";


    IFileService fileService;

    public PersistentRequestQueue(IFileService fileService) {
        this.fileService = fileService;
    }

    public void enqueueNewRequest(Request request) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CREATE).add(request);
        saveQueues(queueMap);
    }

    public void enqueueAccept(PendingRequest accepted) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_ACCEPT).add(accepted);
        saveQueues(queueMap);
    }

    public void enqueueCancellation(CancelledRequest cancel) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CANCEL).add(cancel);
        saveQueues(queueMap);
    }

    public void enqueueConfirmation(ConfirmedRequest confirm) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CONFIRM).add(confirm);
        saveQueues(queueMap);
    }

    public List<Request> getCreateQueue() {
        return new ArrayList<>(loadQueues().get(MAP_CREATE));
    }

    public List<PendingRequest> getAcceptedQueue() {
        ArrayList<Request> acceptedQueue = new ArrayList<>(loadQueues().get(MAP_ACCEPT));
        ArrayList<PendingRequest> acceptedRequests = new ArrayList<>();
        for (Request request: acceptedQueue) {
            acceptedRequests.add((PendingRequest) request);
        }
        return acceptedRequests;
    }

    public List<CancelledRequest> getCancellationQueue() {
        ArrayList<Request> cancelledQueue = new ArrayList<>(loadQueues().get(MAP_CANCEL));
        ArrayList<CancelledRequest> cancelledRequests = new ArrayList<>();
        for (Request request: cancelledQueue) {
            cancelledRequests.add((CancelledRequest)request);
        }
        return cancelledRequests;
    }

    public List<ConfirmedRequest> getConfirmationQueue() {
        ArrayList<Request> confirmedQueue = new ArrayList<>(loadQueues().get(MAP_CONFIRM));
        ArrayList<ConfirmedRequest> confirmedRequests = new ArrayList<>();
        for (Request request: confirmedQueue) {
            confirmedRequests.add((ConfirmedRequest) request);
        }
        return confirmedRequests;
    }


    private Map<String, List<Request>> loadQueues() {
        String serializedHabits = fileService.loadFileAsString(Constants.QUEUE_MAP_FILE_NAME);

        if (StringUtils.isNullOrEmpty(serializedHabits)) {
            HashMap<String, List<Request>> map = new HashMap<>();
            map.put(MAP_CREATE, new ArrayList<>());
            map.put(MAP_ACCEPT, new ArrayList<>());
            map.put(MAP_CANCEL, new ArrayList<>());
            map.put(MAP_CONFIRM, new ArrayList<>());
            return map;
        } else {
            return getGson().fromJson(serializedHabits, new TypeToken<Map<String, List<Request>>>() {}.getType());
        }
    }
    private void saveQueues(Map<String, List<Request>> queueMap) {
        String serializedQueues = getGson().toJson(queueMap);
        fileService.saveStringToFile(Constants.QUEUE_MAP_FILE_NAME, serializedQueues);
    }

    private Gson getGson() {
        return RequestUtils.getGson();
    }

}
