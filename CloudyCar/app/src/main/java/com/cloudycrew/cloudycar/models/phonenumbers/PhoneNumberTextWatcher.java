package com.cloudycrew.cloudycar.models.phonenumbers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ryan on 2016-11-13.
 *
 * A textwatcher to guarantee that phone numbers are formatted properly.
 *
 */

public class PhoneNumberTextWatcher implements TextWatcher
{
    private boolean enabled;
    private String lastEntry;
    private EditText attachedTo;
    int cursorPos;

    public PhoneNumberTextWatcher(EditText attachedTo) {
        enabled = true;
        lastEntry = "";
        this.attachedTo = attachedTo;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (enabled) {
            cursorPos = attachedTo.getSelectionStart();
        }
    }

    @Override
    public synchronized void afterTextChanged(Editable s) {

        if (enabled)
        {
            int difference = s.length() - lastEntry.length();
            boolean batchEdit = difference > 1 || difference < -1;
            boolean deleted = difference < 0;

            StringBuilder sb = new StringBuilder();
            char[] theText = s.toString().toCharArray();

            String phoneNumber = "";
            for (char c : theText) {
                if (Character.isDigit(c)) {
                    phoneNumber += c;
                }
            }

            int length = phoneNumber.length();
            if (length != 0) {
                sb.append('(');
            }
            else {
                cursorPos--;
            }
            for (int i = 0; i < length; i++) {
                sb.append(phoneNumber.toCharArray()[i]);
                if (i == 2 && length >=4) {
                    sb.append(")-");
                }
                if (i == 5 && length >=7) {
                    sb.append("-");
                }
            }


            enabled = false;
            lastEntry = sb.toString();
            this.setText(s, sb, batchEdit, deleted);
        }
    }

    private void setText(Editable s, StringBuilder sb, boolean batchEdit, boolean deleted) {

        s.clear();
        s.insert(0, sb);
        if (!batchEdit && (cursorValidAfterAdding() || deleted)) {
            attachedTo.setSelection( cursorPos <= s.length() ? cursorPos : s.length() );
        }

        enabled = true;

    }

    private boolean cursorValidAfterAdding() {
        Integer[] validPositions = {2,3,7,8,11,12,13};
        ArrayList<Integer> valid = new ArrayList<Integer>(Arrays.asList(validPositions));
        return valid.contains(cursorPos);
    }
}
