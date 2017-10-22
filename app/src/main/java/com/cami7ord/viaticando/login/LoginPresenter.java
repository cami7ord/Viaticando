package com.cami7ord.viaticando.login;

public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();

}
