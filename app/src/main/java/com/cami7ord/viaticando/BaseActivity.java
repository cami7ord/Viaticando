package com.cami7ord.viaticando;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(BaseActivity.this);
                    mProgressDialog.setMessage("Cargando");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                }

                mProgressDialog.show();
            }
        });
    }

    public void hideProgressDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
