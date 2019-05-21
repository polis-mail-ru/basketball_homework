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
import android.util.TypedValue;
import android.view.View;

import ru.ok.technopolis.basketball.R;

public class ScoreView extends View {

    private static final int DEFAULT_MARK_COLOR = Color.BLACK;
    private Path markPath = new Path();
    private Paint markPaint = new Paint();

    public ScoreView(Context context) {
        super(context);
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int markStrokeWidthFromAttr = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, R.dimen.default_point_stroke_width, displayMetrics) + 0.5f);
        int markColorFromAttr = DEFAULT_MARK_COLOR;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScoreView);
            markStrokeWidthFromAttr = typedArray.getDimensionPixelSize(R.styleable.ScoreView_markStrokeWidth, markStrokeWidthFromAttr);
            markColorFromAttr = typedArray.getColor(R.styleable.ScoreView_markColor, markColorFromAttr);
            typedArray.recycle();
        }
        markPaint.setStyle(Paint.Style.STROKE);
        markPaint.setColor(markColorFromAttr);
        markPaint.setStrokeWidth(markStrokeWidthFromAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(markPath, markPaint);
    }

    public void setMark(int mark) {
        resetMark();
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();
        int width = getWidth() / 5 - leftPadding - rightPadding;
        int xStart = width / 2 + leftPadding;
        int yStart = getHeight() - bottomPadding;
        int xEnd;
        int yEnd;
        for (int i = 0; i < mark; i++) {
            markPath.moveTo(xStart, yStart);
            xEnd = xStart + width / 2;
            yEnd = yStart - getHeight() + topPadding + bottomPadding;
            markPath.lineTo(xEnd, yEnd);
            markPath.moveTo(xStart, yStart);
            xEnd = xStart - width / 2;
            yEnd = yStart - getHeight() / 2 + bottomPadding;
            markPath.lineTo(xEnd, yEnd);
            xStart += (width + leftPadding + rightPadding);
        }
        invalidate();
    }

    public void resetMark() {
        markPath.reset();
        invalidate();
    }
}
