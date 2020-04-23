package com.gt.alimert.emvcardreader.ui.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public final class AppUtils {

    private AppUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }

    public static AlertDialog showAlertDialog(Activity activity, String title, String message, String positiveBtnText, String negativeBtnText, boolean isCancelable, Dialog.OnClickListener listener)  {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnText, listener)
                .setCancelable(isCancelable);

        if(negativeBtnText != null && !negativeBtnText.isEmpty())
            alertDialog.setNegativeButton(negativeBtnText, listener);

        return alertDialog.show();

    }

    public static Snackbar showSnackBar(View containerView, String message, String buttonText) {
        Snackbar snackbar = Snackbar.make(containerView, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(buttonText, view -> snackbar.dismiss());
        snackbar.setDuration(2500);
        snackbar.show();
        return snackbar;
    }

    public static AlertDialog showSingleChoiceListDialog(Activity activity, String title, String[] list, String positiveBtnText, Dialog.OnClickListener listener) {
        // setup the alert builder
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setPositiveButton(positiveBtnText, null)
                .setCancelable(false);

        alertDialog.setSingleChoiceItems(list, -1, listener);

        return alertDialog.show();
    }
}
