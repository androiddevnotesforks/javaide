package com.duy.testapplication;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.duy.testapplication.autocomplete.AutoCompleteProvider;

/**
 * Created by Duy on 20-Jul-17.
 */

public class AutoCompleteCodeEditText extends AppCompatEditText {
    private static final String TAG = "AutoCompleteCodeEditTex";
    private AutoCompleteProvider mAutoCompleteProvider;

    public void setAutoCompleteProvider(AutoCompleteProvider mAutoCompleteProvider) {
        this.mAutoCompleteProvider = mAutoCompleteProvider;
    }

    public AutoCompleteCodeEditText(Context context) {
        super(context);
    }

    public AutoCompleteCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
//        mAutoCompleteProvider.getSuggestions(this, getSelectionEnd(), )
    }
}