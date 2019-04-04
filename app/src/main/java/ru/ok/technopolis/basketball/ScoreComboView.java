package ru.ok.technopolis.basketball;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;

public class ScoreComboView extends AppCompatRatingBar {

    private int currentScoreCombo = 0;
    private int maxScoreCombo = 0;

    public ScoreComboView(Context context) {
        super(context);
    }

    public ScoreComboView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void resetScore() {
        setCurrentScoreCombo(0);
    }

    public void resetMaxScore() {
        setMaxScoreCombo(0);
    }

    public void incrementScore() {
        setCurrentScoreCombo(currentScoreCombo + 1);
    }

    public void setCurrentScoreCombo(int score) {
        currentScoreCombo = score;
        if (currentScoreCombo > maxScoreCombo) {
            setMaxScoreCombo(currentScoreCombo);
        }
        if (maxScoreCombo > 0) {
            setRating((float) currentScoreCombo / maxScoreCombo * 5);
        }
        invalidate();
    }

    public void setMaxScoreCombo(int score) {
        maxScoreCombo = score;
    }

    public int getCurrentScoreCombo() {
        return currentScoreCombo;
    }

    public int getMaxScoreCombo() {
        return maxScoreCombo;
    }
}
