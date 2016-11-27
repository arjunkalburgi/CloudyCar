package com.cloudycrew.cloudycar.roleselection;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import rx.functions.Action1;

/**
 * Created by George on 2016-11-23.
 */

public class RoleSelectionController extends ViewController<IRoleSelectionView> {
    private UserController userController;
    private ISchedulerProvider schedulerProvider;

    public RoleSelectionController(UserController userController, ISchedulerProvider schedulerProvider) {
        this.userController = userController;
        this.schedulerProvider = schedulerProvider;
    }

    public void selectDriverRole() {
        if (userController.getCurrentUser().hasCarDescription()) {
            dispatchDisplayDriverSummary();
        } else {
            dispatchDisplayAddCarDescription();
        }
    }

    public void addCarDescription(String carDescription) {
        User currentUser = userController.getCurrentUser();
        currentUser.setCarDescription(carDescription);

        ObservableUtils.fromAction(userController::updateCurrentUser, currentUser)
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(new Action1<Void>() {
                           @Override
                           public void call(Void nothing) {
                               dispatchOnCarDescriptionAdded();
                           }
                       });
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
