package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StarScoreView extends View {
    byte score = 0;
    byte starCount = 0;
    boolean isCombo = false;
    int starSide = 0;
    int offsetStep = 0;
    Drawable defaultStar = getResources().getDrawable(R.drawable.star_default);
    Drawable comboStar = getResources().getDrawable(R.drawable.star_combo);

    public StarScoreView(Context context) {
        super(context, null);
    }

    public StarScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void incrementScore() {
        System.out.println("kek");
        if (score == 10)
            return;
        score++;
        starCount++;
        if (score == 6) {
            starCount = 1;
            isCombo = true;
        }
        invalidate();
    }

    public void decrementScore() {
        System.out.println("kek");
        if (score == 0)
            return;
        score--;
        starCount--;
        if (score == 5) {
            starCount = 5;
            isCombo = false;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable star = isCombo ? comboStar : defaultStar;
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
