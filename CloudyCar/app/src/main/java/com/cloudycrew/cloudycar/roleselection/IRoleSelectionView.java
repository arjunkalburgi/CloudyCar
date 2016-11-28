package com.cloudycrew.cloudycar.roleselection;

/**
 * Created by George on 2016-11-23.
 */

public interface IRoleSelectionView {
    /**
     * Callback for displaying the add car notification
     */
    void displayAddCarDescription();

    /**
     * Callback for displaying driver summary
     */
    void displayDriverSummary();

    /**
     * Callback for after a car description is added
     */
    void onCarDescriptionAdded();
}
