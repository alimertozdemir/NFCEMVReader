package com.gt.alimert.emvcardreader.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gt.alimert.emvcardreader.R;
import com.gt.alimert.emvcardreader.lib.CtlessCardService;
import com.gt.alimert.emvcardreader.lib.enums.BeepType;
import com.gt.alimert.emvcardreader.lib.model.Card;
import com.gt.alimert.emvcardreader.lib.model.LogMessage;
import com.gt.alimert.emvcardreader.ui.util.AppUtils;

import java.util.ArrayList;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity implements CtlessCardService.ResultListener {

    private String TAG = MainActivity.class.getName();

    private LinearLayout llContainer;

    private CtlessCardService mCtlessCardService;

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llContainer = findViewById(R.id.ctless_container);

        mCtlessCardService = new CtlessCardService(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCtlessCardService.start();
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
        showAlertDialog(card);
    }

    @Override
    public void onCardReadFail(String error) {
        playBeep(BeepType.FAIL);
        dismissProgressDialog();
        Log.d(TAG, "CARD READ FAILED ON UI -> " + error);
    }

    @Override
    public void onCardMovedSoFast() {
        playBeep(BeepType.FAIL);
        dismissProgressDialog();
        AppUtils.showSnackBar(llContainer, "Please do not remove your card while reading...", "OK");
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
        runOnUiThread(()-> mProgressDialog = AppUtils.showLoadingDialog(this, "Reading Card", "Please do not remove your card while reading..."));
    }

    private void dismissProgressDialog() {
        runOnUiThread(() -> mProgressDialog.dismiss());
    }

    private void showAlertDialog(Card card) {

        runOnUiThread(() -> {
            String title = "Card Detail";
            String message =
                    "Card Brand : " + card.getCardBrand() + "\n" +
                            "Card Pan : " + card.getPan() + "\n" +
                            "Card Expire Date : " + card.getExpireDate() + "\n" +
                            "Card Track2 Data : " + card.getTrack2() + "\n";

            if(card.getEmvData() != null && !card.getEmvData().isEmpty())
                message += "Card EmvData : " + card.getEmvData();

            mAlertDialog = AppUtils.showAlertDialog(this, title, message, "OK", "SHOW APDU LOGS", false, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int button) {
                    switch (button) {
                        case BUTTON_POSITIVE:
                        case BUTTON_NEUTRAL:
                            mCtlessCardService.start();
                            mAlertDialog.dismiss();
                            break;
                        case BUTTON_NEGATIVE:
                            if(card.getLogMessages() != null && !card.getLogMessages().isEmpty())
                                openApduLogDetail(new ArrayList<>(card.getLogMessages()));
                            mAlertDialog.dismiss();
                            break;
                    }
                }
            });
        });
    }

    private void dismissAlertDialog() {
        if(mAlertDialog != null)
            runOnUiThread(() -> mAlertDialog.dismiss());
    }
}
