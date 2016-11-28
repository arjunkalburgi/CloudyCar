package com.cloudycrew.cloudycar.roleselection;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by George on 2016-11-23.
 */

public class RoleSelectionController extends ViewController<IRoleSelectionView> {
    private UserController userController;
    private ISchedulerProvider schedulerProvider;

    /**
     * Instantiate the RoleSelectionController
     *
     * @param userController the usercontroller
     * @param schedulerProvider the schedulerprovider for async tasks
     */
    public RoleSelectionController(UserController userController, ISchedulerProvider schedulerProvider) {
        this.userController = userController;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Handler for user selecting driver role
     */
    public void selectDriverRole() {
        if (userController.getCurrentUser().hasCarDescription()) {
            dispatchDisplayDriverSummary();
        } else {
            dispatchDisplayAddCarDescription();
        }
    }

    /**
     * Adds a car description to the user
     *
     * @param carDescription the description of the car
     */
    public void addCarDescription(final String carDescription) {
        final User updatedUser = getUpdatedUser(carDescription);

        ObservableUtils.create(new Action0() {
                           @Override
                           public void call() {
                               userController.updateCurrentUser(updatedUser);
                           }
                       })
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(new Action1<Void>() {
                           @Override
                           public void call(Void nothing) {
                               dispatchOnCarDescriptionAdded();
                           }
                       });
    }
    
    private User getUpdatedUser(String carDescription) {
        User currentUser = userController.getCurrentUser();
        currentUser.setCarDescription(carDescription);
        return currentUser;
    }

    private void dispatchOnCarDescriptionAdded() {
        if (isViewAttached()) {
            getView().onCarDescriptionAdded();
        }
    }

    private void dispatchDisplayAddCarDescription() {
        if (isViewAttached()) {
            getView().displayAddCarDescription();
        }
    }

    private void dispatchDisplayDriverSummary() {
        if (isViewAttached()) {
            getView().displayDriverSummary();
        }
    }
}
