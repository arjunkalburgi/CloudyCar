package com.cloudycrew.cloudycar.users;

import java.util.Date;

/**
 * Created by George on 2016-11-23.
 */
public interface IUserHistoryService {
    void markRequestAsRead(String requestId);
    void markRequestAsRead(String requestId, Date timeRead);
    Date getLastReadTime(String requestId);
}
