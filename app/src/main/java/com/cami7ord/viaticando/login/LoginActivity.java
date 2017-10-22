package com.cami7ord.viaticando.login;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.R;

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {

    private EditText username;
    private EditText password;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        username = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        findViewById(R.id.login_button).setOnClickListener(this);

        presenter = new LoginPresenterImpl(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void setUsernameError() {
        username.setError("Credenciales Inválidas");
    }

    @Override
    public void setPasswordError() {
        password.setError("Credenciales Inválidas");
    }

    @Override
    public void navigateToHome() {
        Log.e("Navigate to home", "Ok");
        //startActivity(new Intent(this, MainActivity.class));
        //finish();
    }

    @Override
    public void onClick(View view) {
        presenter.validateCredentials(username.getText().toString(), password.getText().toString());
    }
}
