package com.michaelnovakjr.numberpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

public class NumberPickerPreference extends DialogPreference {
    private NumberPicker mPicker;
    private int mStartRange;
    private int mEndRange;
    private int mDefault;
    private long mSpeed;
    private int mStep;

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs == null) {
            return;
        }

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.numberpicker);
        mStartRange = arr.getInteger(R.styleable.numberpicker_startRange, 0);
        mEndRange = arr.getInteger(R.styleable.numberpicker_endRange, 200);
        mDefault = arr.getInteger(R.styleable.numberpicker_defaultValue, 0);
        mSpeed = arr.getInteger(R.styleable.numberpicker_speed, 300);
        mStep = arr.getInteger(R.styleable.numberpicker_step, 1);

        arr.recycle();

        setDialogLayoutResource(R.layout.pref_number_picker);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public NumberPickerPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mPicker = (NumberPicker) view.findViewById(R.id.pref_num_picker);
        mPicker.setRange(mStartRange, mEndRange);
        mPicker.setCurrent(getValue());
        mPicker.setSpeed(mSpeed);
        mPicker.setStep(mStep);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        final int origValue = getValue();
        final int curValue = mPicker.getCurrent();

        if (positiveResult && (curValue != origValue)) {
            if (callChangeListener(curValue)) {
                saveValue(curValue);
            }
        }
    }

    public void setRange(int start, int end) {
        mPicker.setRange(start, end);
    }

    private boolean saveValue(int val) {
        return persistInt(val);
    }

    private int getValue() {
        return getSharedPreferences().getInt(getKey(), mDefault);
    }
}
