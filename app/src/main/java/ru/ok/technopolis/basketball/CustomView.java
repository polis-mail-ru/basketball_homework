package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {
    private Paint paint;
    private int count;
    private Path path;

    public CustomView(Context context) {
        super(context);
    }

    public void drawStar(Canvas canvas, int from) {
        float mid = (float) canvas.getHeight() / 2;
        float min = Math.min(getWidth(), getHeight());
        float fat = min / 17;
        float half = min / 2;
        mid = mid - half;
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(fat);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        paint.setStyle(Paint.Style.FILL);
        // top left
        path.moveTo(from + mid + half * 0.5f, half * 0.84f);
        // top right
        path.lineTo(from + mid + half * 1.5f, half * 0.84f);
        // bottom left
        path.lineTo(from + mid + half * 0.68f, half * 1.45f);
        // top tip
        path.lineTo(from + mid + half * 1.0f, half * 0.5f);
        // bottom right
        path.lineTo(from + mid + half * 1.32f, half * 1.45f);
        // top left
        path.lineTo(from + mid + half * 0.5f, half * 0.84f);
        path.close();
        canvas.drawPath(path, paint);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setTextSize(65.0f);
        paint.setStrokeWidth(7.5f);
        count = 0;
    }

    void increase() {
        count++;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (count <= 5) {
            for (int i = 0; i < count; i++) {
                drawStar(canvas, getHeight() * i);
            }
        } else {
            drawStar(canvas, 0);
            paint.setColor(Color.WHITE);
            canvas.drawText("x" + count, 115, 85, paint);
        }
    }
}