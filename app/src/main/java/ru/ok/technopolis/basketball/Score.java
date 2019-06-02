package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

public class Score extends View {

    private int score;
    private static final float TEXT_SIZE = 50f;
    private String count;
    private Paint paint;

    public Score(Context context) {
        this(context, null);
    }

    public Score(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);

        float textSize = Math.round(TEXT_SIZE * getResources().getDisplayMetrics().density);
        paint.setTextSize(textSize);
        reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int textStart = Math.round(0.3f * getWidth());
        int textBaseline = Math.round(getHeight() * 0.6f);
        canvas.drawText(count, textStart, textBaseline, paint);
    }

    public void reset() {
        score = 0;
        count = String.format(Locale.getDefault(), "%2d", score);
        invalidate();
    }

    public void increment() {
        score = score + 1;
        count = String.format(Locale.getDefault(), "%2d", score);
        invalidate();
    }
}
