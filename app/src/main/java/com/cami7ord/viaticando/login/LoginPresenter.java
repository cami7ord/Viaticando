package com.cami7ord.viaticando.login;

import android.support.annotation.NonNull;

import com.cami7ord.viaticando.data.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link LoginFragment}), retrieves the data and updates the
 * UI as required.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private final User mLoginUser;

    private final LoginContract.View mLoginView;

    public LoginPresenter(@NonNull User loginRepository, @NonNull LoginContract.View loginView) {
        mLoginUser = checkNotNull(loginRepository, "loginUser cannot be null");
        mLoginView = checkNotNull(loginView, "loginView cannot be null!");
        mLoginView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void attemptLogin(String email, String password) {
        mLoginView.setLoadingIndicator(true);
    }
}
