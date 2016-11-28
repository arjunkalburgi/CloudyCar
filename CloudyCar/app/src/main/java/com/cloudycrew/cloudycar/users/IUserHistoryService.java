package com.cloudycrew.cloudycar.users;

import java.util.Date;

/**
 * Created by George on 2016-11-23.
 */
public interface IUserHistoryService {

    /**
     * Marks a request as read by the user
     *
     * @param requestId the requestId of the read request
     */
    void markRequestAsRead(String requestId);

    /**
     * Marks a request as read by the user at the specified time
     *
     * @param requestId the requestId of the read request
     * @param timeRead the time that the request was read
     */
    void markRequestAsRead(String requestId, Date timeRead);

    /**
     * Gets the last time a request was read
     * @param requestId the requestId
     * @return the time the request was read
     */
    Date getLastReadTime(String requestId);
}
