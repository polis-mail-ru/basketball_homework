package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BackView extends View {
    private Paint paint;
    private List<Line> stack;

    public BackView(Context context) {
        super(context);
    }

    public BackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        stack = new ArrayList<>();
    }


    public void drawLine(float x1, float y1, float x2, float y2) {
        stack.add(new Line(x1, y1, x2, y2));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Line line : stack) {
            canvas.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), paint);
        }
    }

    public void refresh() {
        stack.clear();
    }
}
