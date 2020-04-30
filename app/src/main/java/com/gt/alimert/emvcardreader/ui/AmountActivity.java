package com.gt.alimert.emvcardreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gt.alimert.emvcardreader.R;
import com.gt.alimert.emvcardreader.model.TransactionAmount;
import com.gt.alimert.emvcardreader.util.AmountInputTextWatcher;
import com.gt.alimert.emvnfclib.enums.TransactionType;

/**
 * @author AliMertOzdemir
 * @class AmountActivity
 * @created 30.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class AmountActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private String TAG = AmountActivity.class.getName();

    private EditText etAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);

        etAmount = findViewById(R.id.et_amount);
        etAmount.requestFocus();

        etAmount.setOnEditorActionListener(this);
        etAmount.addTextChangedListener(new AmountInputTextWatcher(etAmount));

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            Long inputAmount = etAmount.getText().toString().isEmpty()? 0L : Long.valueOf(etAmount.getText().toString().replaceAll("[$,.]", ""));
            if(inputAmount > 0)
                openReadCardActivity();
            else
                return true;
        }
        return false;
    }

    private void openReadCardActivity() {
        String amount = etAmount.getText().toString();
        TransactionAmount transactionAmount = new TransactionAmount(amount, TransactionType.SALES);
        Intent intent = ReadCardActivity.newIntent(this);
        intent.putExtra("amount", transactionAmount);
        startActivity(intent);
    }
}
