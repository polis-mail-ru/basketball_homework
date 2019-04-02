package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class BackView extends ImageView {
    private Paint paint;
    private ArrayList<Line> stack;

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
        Log.d("", "onDraw: " + stack.size());
        for (int i = 0; i < stack.size(); i++) {
            canvas.drawLine(stack.get(i).getX1(), stack.get(i).getY1(), stack.get(i).getX2(), stack.get(i).getY2(), paint);
        }
    }

    public void refresh() {
        stack = new ArrayList<>();
    }
}
