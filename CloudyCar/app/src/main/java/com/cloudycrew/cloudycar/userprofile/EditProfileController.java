package com.cloudycrew.cloudycar.userprofile;

import com.cloudycrew.cloudycar.ViewController;
import com.cloudycrew.cloudycar.controllers.UserController;
import com.cloudycrew.cloudycar.models.User;
import com.cloudycrew.cloudycar.scheduling.ISchedulerProvider;
import com.cloudycrew.cloudycar.utils.ObservableUtils;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by Ryan on 2016-11-12.
 */

public class EditProfileController extends ViewController<IEditProfileView> {
    private UserController userController;
    private ISchedulerProvider schedulerProvider;

    public EditProfileController(UserController userController, ISchedulerProvider schedulerProvider) {
        this.userController = userController;
        this.schedulerProvider = schedulerProvider;
    }

    /**
     * Asynchronously loads a user
     * @param username - username of the user to load
     */
    public void loadUser(final String username) {
        dispatchDisplayLoading();
        User localUser = userController.getCurrentUser();
        if( !username.equals(localUser.getUsername()) ) {
            ObservableUtils.create(new Func0<User>() {
                        @Override
                        public User call() {
                            return userController.getUser(username);
                        }
                    })
                    .subscribeOn(schedulerProvider.ioScheduler())
                    .observeOn(schedulerProvider.mainThreadScheduler())
                    .subscribe(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            dispatchDisplayUser(user);
                        }
                    });
        }
        else {
            this.dispatchDisplayUser(localUser);
        }

    }

    private void dispatchDisplayLoading() {
        if (getView() != null) {
            getView().displayLoading();
        }
    }

    private void dispatchDisplayUser(User user) {
        if (getView() != null) {
            getView().displayUser(user);
        }
    }

    /**
     * Asynchronously updates a user locally and remotely, calls appropriate callbacks for failures
     * @param user - the user to update
     */
    public void updateUser(final User user) {
        ObservableUtils.create(new Action0() {
                           @Override
                           public void call() {
                               userController.updateCurrentUser(user);
                           }
                       })
                       .subscribeOn(schedulerProvider.ioScheduler())
                       .observeOn(schedulerProvider.mainThreadScheduler())
                       .subscribe(new Action1<Void>() {
                           @Override
                           public void call(Void nothing) {
                               dispatchOnEditSuccess();
                           }
                       }, new Action1<Throwable>() {
                           @Override
                           public void call(Throwable throwable) {
                               dispatchOnEditFailure();
                           }
                       });
    }

    private void dispatchOnEditSuccess() {
        if (isViewAttached()) {
            getView().onEditSuccess();
        }
    }

    private void dispatchOnEditFailure() {
        if (isViewAttached()) {
            getView().onEditFailure();
        }
    }
}
