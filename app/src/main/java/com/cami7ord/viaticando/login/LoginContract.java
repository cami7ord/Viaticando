package com.cami7ord.viaticando.login;

import com.cami7ord.viaticando.BasePresenter;
import com.cami7ord.viaticando.BaseView;
import com.cami7ord.viaticando.data.User;

/**
 * This specifies the contract between the view and the presenter.
 */

public interface LoginContract {

    interface Presenter extends BasePresenter {

        void attemptLogin(String email, String password);

    }

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

    }

    interface Model {
        User authUser(String email, String password);
    }

}
