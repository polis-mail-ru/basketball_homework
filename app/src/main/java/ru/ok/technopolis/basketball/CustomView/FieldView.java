package ru.ok.technopolis.basketball.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import ru.ok.technopolis.basketball.R;

public class FieldView extends View {

    private Float currentX;
    private Float currentY;
    private Paint dotPaint = new Paint();
    private Path dotPath = new Path();
    private int pointRadius;
    private static final int DEFAULT_POINT_COLOR = Color.BLACK;
    public FieldView(Context context) {
        super(context);
    }

    public FieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Utils.pxFromDP(displayMetrics, R.dimen.default_point_stroke_width);
        int pointStrokeWidthFromAttr = Utils.pxFromDP(displayMetrics, R.dimen.default_point_stroke_width);
        int pointRadiusFromAttr = Utils.pxFromDP(displayMetrics, R.dimen.default_point_radius);
        int pointColorFromAttr = DEFAULT_POINT_COLOR;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FieldView);
            pointStrokeWidthFromAttr = typedArray.getDimensionPixelOffset(R.styleable.FieldView_pointStrokeWidth, pointStrokeWidthFromAttr);
            pointRadiusFromAttr = typedArray.getDimensionPixelSize(R.styleable.FieldView_pointRadius, pointRadiusFromAttr);
            pointColorFromAttr = typedArray.getColor(R.styleable.FieldView_pointColor, pointColorFromAttr);
            typedArray.recycle();
        }

        dotPaint.setStyle(Paint.Style.STROKE);
        dotPaint.setColor(pointColorFromAttr);
        dotPaint.setStrokeWidth(pointStrokeWidthFromAttr);
        pointRadius = pointRadiusFromAttr;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentX == null || currentY == null) {
            canvas.drawPath(dotPath, dotPaint);
            return;
        }
        dotPath.addCircle(currentX, currentY, pointRadius, Path.Direction.CW);
        canvas.drawPath(dotPath, dotPaint);
    }

    public void drawDot(float x, float y) {
        currentX = x;
        currentY = y;
        invalidate();
    }

    public void clearField() {
        dotPath.reset();
        currentX = null;
        currentY = null;
        invalidate();
    }
}
