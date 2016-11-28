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
    private static final String MAP_CREATE = "Created";
    private static final String MAP_ACCEPT = "Accepted";
    private static final String MAP_CANCEL = "Cancelled";
    private static final String MAP_CONFIRM = "Confirmed";


    IFileService fileService;

    public PersistentRequestQueue(IFileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Enqueue a newly created request
     * @param request - request to enqueue
     */
    public void enqueueNewRequest(Request request) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CREATE).add(request);
        saveQueues(queueMap);
    }

    /**
     * Enqueue a newly accepted request
     * @param accepted - request to enqueue
     */
    public void enqueueAccept(PendingRequest accepted) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_ACCEPT).add(accepted);
        saveQueues(queueMap);
    }

    /**
     * Enqueue a cancelled request
     * @param cancel - cancelled request to enqueue
     */
    public void enqueueCancellation(CancelledRequest cancel) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CANCEL).add(cancel);
        saveQueues(queueMap);
    }

    /**
     * Enqueue a confirmed request
     * @param confirm - confirmed request to enqueue
     */
    public void enqueueConfirmation(ConfirmedRequest confirm) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CONFIRM).add(confirm);
        saveQueues(queueMap);
    }

    /**
     * Get the queue of created requests
     * @return The queue of created requests as a List
     */
    public List<Request> getCreateQueue() {
        return new ArrayList<>(loadQueues().get(MAP_CREATE));
    }

    /**
     * Get the queue of accepted requests
     * @return The queue of accepted requests as a List
     */
    public List<PendingRequest> getAcceptedQueue() {
        ArrayList<Request> acceptedQueue = new ArrayList<>(loadQueues().get(MAP_ACCEPT));
        ArrayList<PendingRequest> acceptedRequests = new ArrayList<>();
        for (Request request: acceptedQueue) {
            acceptedRequests.add((PendingRequest) request);
        }
        return acceptedRequests;
    }

    /**
     * Get the queue of cancelled requests
     * @return The queue of cancelled requests as a List
     */
    public List<CancelledRequest> getCancellationQueue() {
        ArrayList<Request> cancelledQueue = new ArrayList<>(loadQueues().get(MAP_CANCEL));
        ArrayList<CancelledRequest> cancelledRequests = new ArrayList<>();
        for (Request request: cancelledQueue) {
            cancelledRequests.add((CancelledRequest)request);
        }
        return cancelledRequests;
    }

    /**
     * Get the queue of confirmed requests
     * @return The queue of confirmed requests as a List
     */
    public List<ConfirmedRequest> getConfirmationQueue() {
        ArrayList<Request> confirmedQueue = new ArrayList<>(loadQueues().get(MAP_CONFIRM));
        ArrayList<ConfirmedRequest> confirmedRequests = new ArrayList<>();
        for (Request request: confirmedQueue) {
            confirmedRequests.add((ConfirmedRequest) request);
        }
        return confirmedRequests;
    }

    /**
     * Dequeue a created request
     * @param request - request to be dequeued
     */
    public void dequeueCreation(Request request) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CREATE).remove(request);
        saveQueues(queueMap);
    }

    /**
     * Dequeue an accepted request
     * @param request - request to be dequeued
     */
    public void dequeueAccept(PendingRequest request) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_ACCEPT).remove(request);
        saveQueues(queueMap);
    }

    /**
     * Dequeue a cancelled request
     * @param request - cancelled request to be dequeued
     */
    public void dequeueCancellation(CancelledRequest request) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CREATE).remove(request);
        saveQueues(queueMap);
    }

    /**
     * Dequeue a confirmed request
     * @param request - confirmed request to dequeue
     */
    public void dequeueConfirmation(ConfirmedRequest request) {
        Map<String, List<Request>> queueMap = loadQueues();
        queueMap.get(MAP_CREATE).remove(request);
        saveQueues(queueMap);
    }


    private Map<String, List<Request>> loadQueues() {
        String serializedHabits = fileService.loadFileAsString(Constants.QUEUE_MAP_FILE_NAME);

        if (StringUtils.isNullOrEmpty(serializedHabits)) {
            HashMap<String, List<Request>> map = new HashMap<>();
            map.put(MAP_CREATE, new ArrayList<Request>());
            map.put(MAP_ACCEPT, new ArrayList<Request>());
            map.put(MAP_CANCEL, new ArrayList<Request>());
            map.put(MAP_CONFIRM, new ArrayList<Request>());
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
