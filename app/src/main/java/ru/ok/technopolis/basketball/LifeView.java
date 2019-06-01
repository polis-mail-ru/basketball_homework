package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class LifeView extends View {

    interface GameEventListener{
        void endGame();
    }

    private final int LIVES = 5;

    private GameEventListener listener;
    private byte lifeCount = LIVES;
    private int heartSide = 0;
    private int offsetStep = 0;
    private Drawable drawable = getResources().getDrawable(R.drawable.heart);

    public LifeView(Context context) {
        super(context, null);
        listener = (GameEventListener) context;
    }

    public LifeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listener = (GameEventListener) context;
    }

    public void restart() {
        lifeCount = LIVES;
        invalidate();
    }

    public void decLife() {
        if (lifeCount == 0) {
            return;
        }
        lifeCount--;
        invalidate();
        if (lifeCount == 0) {
            listener.endGame();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < lifeCount; i++) {
            drawable.setBounds(i * offsetStep, 0, i * offsetStep + heartSide, heartSide);
            drawable.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        heartSide = (int) Math.min(h, w / 5f);
        offsetStep = w / 5;
    }
}