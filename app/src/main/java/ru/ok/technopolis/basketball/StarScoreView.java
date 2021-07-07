package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StarScoreView extends View {
    private static final int MAX_SCORE = 10;
    private static final int MID_SCORE = 5;

    private byte score = 0;
    private byte starCount = 0;
    private boolean combo = false;
    private int starSide = 0;
    private int offsetStep = 0;
    private Drawable defaultStar = getResources().getDrawable(R.drawable.star_default);
    private Drawable comboStar = getResources().getDrawable(R.drawable.star_combo);

    public StarScoreView(Context context) {
        super(context, null);
    }

    public StarScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void incrementScore() {
        if (score == MAX_SCORE) {
            return;
        }
        score++;
        starCount++;
        if (score == MID_SCORE + 1) {
            starCount = 1;
            combo = true;
        }
        invalidate();
    }

    public void decrementScore() {
        System.out.println("kek");
        if (score == 0) {
            return;
        }
        score--;
        starCount--;
        if (score == MID_SCORE) {
            starCount = MID_SCORE;
            combo = false;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable star = combo ? comboStar : defaultStar;
        for (int i = 0; i < starCount; i++) {
            star.setBounds(i * offsetStep, 0, i * offsetStep + starSide, starSide);
            star.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        starSide = (int) Math.min(h, w / 5f);
        offsetStep = w / 5;
    }
}
