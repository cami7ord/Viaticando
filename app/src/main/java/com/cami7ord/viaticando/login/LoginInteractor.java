package com.cami7ord.viaticando.login;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface LoginInteractor {

    interface OnLoginFinishedListener {

        void onUsernameError();
        void onPasswordError();
        void onSuccess();

    }

    void login(String username, String password, OnLoginFinishedListener listener);

}
