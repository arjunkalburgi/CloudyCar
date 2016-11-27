package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.fileservices.IFileService;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.utils.RequestUtils;
import com.cloudycrew.cloudycar.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by George on 2016-10-13.
 */

public class LocalRequestService implements IRequestService {
    IFileService fileService;

    public LocalRequestService(IFileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public List<Request> getRequests() {
        return new ArrayList<>(loadRequests().values());
    }

    @Override
    public void createRequest(Request request) {
        Map<String, Request> requests = loadRequests();

        if (!requests.containsKey(request.getId())) {
            requests.put(request.getId(), request);
            saveRequests(requests);
        }
    }

    @Override
    public void updateRequest(Request request) {
        Map<String, Request> requests = loadRequests();

        requests.put(request.getId(), request);
        saveRequests(requests);
    }

    @Override
    public void deleteRequest(String requestId) {
        Map<String, Request> requests = loadRequests();

        if (requests.containsKey(requestId)) {
            requests.remove(requestId);
            saveRequests(requests);
        }
    }

    @Override
    public List<Request> search() {
        return this.getRequests();
    }

    private Map<String, Request> loadRequests() {
        String serializedHabits = fileService.loadFileAsString(Constants.REQUEST_MAP_FILE_NAME);

        if (StringUtils.isNullOrEmpty(serializedHabits)) {
            return new HashMap<>();
        } else {
            return getGson().fromJson(serializedHabits, new TypeToken<Map<String, Request>>() {}.getType());
        }
    }

    private void saveRequests(Map<String, Request> requestMap) {
        String serializedHabits = getGson().toJson(requestMap);
        fileService.saveStringToFile(Constants.REQUEST_MAP_FILE_NAME, serializedHabits);
    }

    private Gson getGson() {
        return RequestUtils.getGson();
    }
}
