package com.gt.alimert.emvcardreader.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * @author AliMertOzdemir
 * @class AmountInputTextWatcher
 * @created 29.01.2020
 */
public class AmountInputTextWatcher implements TextWatcher {

    private final WeakReference<EditText> editTextWeakReference;

    public AmountInputTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String s = editable.toString();
        if (s.isEmpty()) return;
        editText.removeTextChangedListener(this);
        String formatted = FormatUtils.getHumanReadableAmount(s);
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}