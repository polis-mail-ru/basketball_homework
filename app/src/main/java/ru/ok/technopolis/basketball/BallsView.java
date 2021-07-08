package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class BallsView extends View {
    public static final int INIT_BALLS = 7;

    private final Drawable ballImage = getResources().getDrawable(R.drawable.ball);
    private int balls = INIT_BALLS;
    private int ballSize = 0;
    private int offsetStep = 0;

    public BallsView(Context context) {
        super(context);
    }

    public BallsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void refresh() {
        balls = INIT_BALLS;
        invalidate();
    }

    public void decrease() {
        balls--;
        invalidate();
    }

    public boolean isEmpty() {
        return balls == 0;
    }

    public int thrown() {
        return INIT_BALLS - balls;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = INIT_BALLS; i >= INIT_BALLS - balls; i--) { // from right to left
            ballImage.setBounds(i * offsetStep, 0, i * offsetStep + ballSize, ballSize);
            ballImage.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        ballSize = Math.min(h, w / (INIT_BALLS - 1));
        offsetStep = w / (INIT_BALLS - 1);
    }
}

