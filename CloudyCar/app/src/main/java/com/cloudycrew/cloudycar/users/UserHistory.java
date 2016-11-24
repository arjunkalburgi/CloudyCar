package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.Constants;
import com.cloudycrew.cloudycar.fileservices.IFileService;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.cloudycrew.cloudycar.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by George on 2016-11-23.
 */

public class UserHistory {
    private IFileService fileService;

    public UserHistory(IFileService fileService) {
        this.fileService = fileService;
    }

    public void markRequestAsRead(String requestId) {
        markRequestAsRead(requestId, new Date());
    }

    public void markRequestAsRead(String requestId, Date timeRead) {
        Map<String, Date> historyMap = loadHistory();
        historyMap.put(requestId, timeRead);

        saveHistory(historyMap);
    }

    public Date getLastReadTime(String requestId) {
        return loadHistory().get(requestId);
    }

    private Map<String, Date> loadHistory() {
        String serializedHistory = fileService.loadFileAsString(Constants.USER_HISTORY_FILE_NAME);

        if (StringUtils.isNullOrEmpty(serializedHistory)) {
            return new HashMap<>();
        } else {
            return getGson().fromJson(serializedHistory, new TypeToken<Map<String, Date>>() {}.getType());
        }
    }

    private void saveHistory(Map<String, Date> historyMap) {
        String serializedHistory = getGson().toJson(historyMap);
        fileService.saveStringToFile(Constants.USER_HISTORY_FILE_NAME, serializedHistory);
    }

    private Gson getGson() {
        return new Gson();
    }
}
