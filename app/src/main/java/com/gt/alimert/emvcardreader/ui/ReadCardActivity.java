package com.gt.alimert.emvcardreader.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gt.alimert.emvcardreader.R;
import com.gt.alimert.emvcardreader.model.TransactionAmount;
import com.gt.alimert.emvcardreader.util.AppUtils;
import com.gt.alimert.emvnfclib.CtlessCardService;
import com.gt.alimert.emvnfclib.data.model.Configuration;
import com.gt.alimert.emvnfclib.enums.BeepType;
import com.gt.alimert.emvnfclib.enums.TransactionType;
import com.gt.alimert.emvnfclib.model.Application;
import com.gt.alimert.emvnfclib.model.Card;
import com.gt.alimert.emvnfclib.model.LogMessage;

import java.util.ArrayList;
import java.util.List;

public class ReadCardActivity extends AppCompatActivity implements CtlessCardService.ResultListener {

    private String TAG = ReadCardActivity.class.getName();

    private LinearLayout llContainer;

    private CtlessCardService mCtlessCardService;

    private AlertDialog mAlertDialog;

    private ProgressDialog mProgressDialog;

    private TextView tvAmount;

    private String mAmount = "";

    public static Intent newIntent(Context context) {
        return new Intent(context, ReadCardActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);

        llContainer = findViewById(R.id.ctless_container);
        tvAmount = findViewById(R.id.tv_amount);

        String hostUrl = "https://gbmposapp-d.garanti.com.tr/api/";
        Configuration configuration = new Configuration.Builder(hostUrl, 700070)
                .setConnectTimeout(30000)
                .setReadTimeout(3000)
                .setEmvSupport(true)
                .build();
        mCtlessCardService = new CtlessCardService(configuration);

        TransactionAmount transactionAmount = getIntent().getParcelableExtra("amount");
        if(transactionAmount != null) {
            String amount = transactionAmount.getAmount();
            mAmount = amount.replaceAll("[$,.]", "");
            tvAmount.setText(amount.concat(" ₺"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String amount = "10000";
        if(mAmount != null)
            amount = mAmount;
        mCtlessCardService.startTransaction(this, TransactionType.SALES, amount, this);
    }

    @Override
    public void onConfigurationError(String error) {
        Log.d(TAG, "ON CONFIGURATION ERROR");
        showAlertDialog("ERROR", error);
    }

    @Override
    public void onCardDetect() {
        Log.d(TAG, "ON CARD DETECTED");
        playBeep(BeepType.SUCCESS);
        showProgressDialog();
    }

    @Override
    public void onCardReadSuccess(Card card) {
        dismissProgressDialog();
        showCardDetailDialog(card);
    }

    @Override
    public void onCardReadFail(String error) {
        playBeep(BeepType.FAIL);
        dismissProgressDialog();
        showAlertDialog("ERROR", error);
    }

    @Override
    public void onCardReadTimeout() {
        playBeep(BeepType.FAIL);
        dismissProgressDialog();
        AppUtils.showSnackBar(llContainer, "Timeout has been reached...", "OK");
    }

    @Override
    public void onCardMovedSoFast() {
        playBeep(BeepType.FAIL);
        dismissProgressDialog();
        AppUtils.showSnackBar(llContainer, "Please do not remove your card while reading...", "OK");
    }

    @Override
    public void onCardSelectApplication(List<Application> applications) {
        playBeep(BeepType.FAIL);
        dismissProgressDialog();
        showApplicationSelectionDialog(applications);
    }

    private void openApduLogDetail(ArrayList<LogMessage> logMessages) {
        Intent intent = ApduLogActivity.newIntent(this);
        intent.putParcelableArrayListExtra("apduLog", logMessages);
        startActivity(intent);
    }

    private void playBeep(BeepType beepType) {
        ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        switch (beepType) {
            case SUCCESS:
                toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,200);
                break;
            case FAIL:
                toneGen.startTone(ToneGenerator.TONE_SUP_ERROR,200);
                break;
            default:
                break;
        }
    }

    private void showProgressDialog() {
        dismissAlertDialog();
        runOnUiThread(()-> mProgressDialog = AppUtils.showLoading(this));
    }

    private void dismissProgressDialog() {
        runOnUiThread(() -> mProgressDialog.dismiss());
    }

    private void showAlertDialog(String title, String message) {

        runOnUiThread(() -> {
            mAlertDialog = AppUtils.showAlertDialog(this, title, message, "OK", "SHOW APDU LOGS", false, (dialogInterface, button) -> {
                mAlertDialog.dismiss();
                this.onResume();
            });
        });
    }

    private void showCardDetailDialog(Card card) {

        runOnUiThread(() -> {
            String title = "Card Detail";
            String message =
                    "Card Brand : " + card.getCardType().getCardBrand() + "\n" +
                            "Card Pan : " + card.getPan() + "\n" +
                            "Card Expire Date : " + card.getExpireDate() + "\n" +
                            "Card Track2 Data : " + card.getTrack2() + "\n";

            if(card.getEmvData() != null && !card.getEmvData().isEmpty())
                message += "\n\nCard EmvData : \n" + card.getEmvData();

            mAlertDialog = AppUtils.showAlertDialog(this, title, message, "OK", "SHOW APDU LOGS", false, (dialogInterface, button) -> {
                switch (button) {
                    case DialogInterface.BUTTON_POSITIVE:
                    case DialogInterface.BUTTON_NEUTRAL:
                        mAlertDialog.dismiss();
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if(card.getLogMessages() != null && !card.getLogMessages().isEmpty())
                            openApduLogDetail(new ArrayList<>(card.getLogMessages()));
                        mAlertDialog.dismiss();
                        break;
                }
            });
        });
    }

    private void showApplicationSelectionDialog(List<Application> applications) {

        String[] appNames = new String[applications.size()];
        int index = 0;
        for (Application application : applications) {
            appNames[index] = application.getAppLabel();
            index++;
        }

        runOnUiThread(() -> {
            String title = "Select One of Your Cards";
            mAlertDialog = AppUtils.showSingleChoiceListDialog(this, title, appNames, "OK", (dialogInterface, i) -> mCtlessCardService.setSelectedApplication(i));
        });
    }

    private void dismissAlertDialog() {
        if(mAlertDialog != null)
            runOnUiThread(() -> mAlertDialog.dismiss());
    }
}
