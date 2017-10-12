package com.cami7ord.viaticando;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    public ProgressDialog mProgressDialog;

    public void displayLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }

        if(active) {
            mProgressDialog.setMessage("Cargando");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

}