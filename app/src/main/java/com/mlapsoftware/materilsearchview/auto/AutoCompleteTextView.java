package com.mlapsoftware.materilsearchview.auto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Filterable;

import com.mlapsoftware.searchviewwidget.R;

public class AutoCompleteTextView extends EditText {

    protected Filterable adapter;
    protected TextWatcher autoCompleteTextWatcher;


    public interface OnAutoCompleteListener {
        void onAutoComplete();
    }

    public AutoCompleteTextView(Context context) {
        this(context, null);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardBackgroundColor);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        autoCompleteTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        };
        addTextChangedListener(autoCompleteTextWatcher);
    }

    public <T extends Filterable> void setAdapter(@NonNull T adapter) {
        this.adapter = adapter;
    }

    public void performCompletion(String s) {
        removeTextChangedListener(autoCompleteTextWatcher);
        setText(s);
        setSelection(s.length());
        addTextChangedListener(autoCompleteTextWatcher);
    }
}
