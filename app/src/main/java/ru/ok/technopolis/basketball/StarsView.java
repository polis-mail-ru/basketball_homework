package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StarsView extends View {

    private Paint paint;
    private Rect rect;
    private int starsAmount;

    public StarsView(Context context) {
        super(context);
        init();
    }

    public StarsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new Rect();
        starsAmount = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int singleStarWidth = getWidth() / 5 - 5;

        for (int i = 0; i < starsAmount; i++) {
            rect.left = i * (singleStarWidth + 5);
            rect.right = i * (singleStarWidth + 5) + singleStarWidth;
            rect.top = 0;
            rect.bottom = getHeight();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.YELLOW);
            canvas.drawRect(rect, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            canvas.drawRect(rect, paint);
        }
    }

    public void setStarsAmount(int starsAmount) {
        if (starsAmount > 5 || starsAmount < 0) throw new IllegalArgumentException();
        this.starsAmount = starsAmount;
        this.invalidate();
    }
}
