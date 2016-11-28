package com.cloudycrew.cloudycar.summarycontainer;

/**
 * Created by George on 2016-11-23.
 */

public interface ISummaryMenuView {
    /**
     * Displays the total number of unread rider requests
     *
     * @param numberUnread - the number of unread requests
     */
    void displayTotalUnreadRiderRequests(int numberUnread);

    /**
     * Displays the total number of unread driver requests
     *
     * @param numberUnread - the number of unread requests
     */
    void displayTotalUnreadDriverRequests(int numberUnread);
}
