package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class CustomCounterView extends android.support.v7.widget.AppCompatTextView {
    private int currScore;

    @SuppressLint("SetTextI18n")
    public CustomCounterView(Context context) {
        super(context);
        currScore = 0;
        setText(Integer.toString(currScore));
    }

    public CustomCounterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("SetTextI18n")
    public void resetScore() {
        currScore = 0;
        setText(Integer.toString(currScore));
    }

    @SuppressLint("SetTextI18n")
    public void incrementScore() {
        currScore++;
        setText(Integer.toString(currScore));
    }
}
