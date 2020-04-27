package com.gt.alimert.emvcardreader.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gt.alimert.emvcardreader.R;
import com.gt.alimert.emvnfclib.model.LogMessage;

import java.util.List;

public class ApduLogActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = ApduLogActivity.class.getName();
    private TextView tvResult;

    public static Intent newIntent(Context context) {
        return new Intent(context, ApduLogActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apdu_log);

        tvResult = findViewById(R.id.tv_result);
        Button btnOk = findViewById(R.id.button_ok);
        FloatingActionButton fab = findViewById(R.id.fab);

        List<LogMessage> logMessages = getIntent().getParcelableArrayListExtra("apduLog");

        StringBuilder stringBuilder = new StringBuilder();

        if(logMessages != null && !logMessages.isEmpty()) {
            for (LogMessage logMessage : logMessages) {
                String command = "<font color=#b37700>" + logMessage.getCommand() + "</font><br>";
                String reqMessage = "<font color=#77b300>" + "--&gt " + logMessage.getRequest() + "</font><br>";
                String respMessage = "<font color=#00ccff>" + "&lt-- " + logMessage.getResponse() + "</font><br><br>";
                stringBuilder.append(command).append(reqMessage).append(respMessage);
            }
        }

        tvResult.setText(Html.fromHtml(stringBuilder.toString()));

        fab.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.button_ok:
                finish();
                break;
            case R.id.fab:
                shareLog();
                break;
            default:
                break;
        }
    }

    private void shareLog() {
        Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_TEXT, tvResult.getText().toString());
        startActivity(Intent.createChooser(intent2, "Send logs"));
    }
}
