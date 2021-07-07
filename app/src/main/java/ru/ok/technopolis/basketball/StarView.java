package ru.ok.technopolis.basketball;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StarView extends View {
    public static final int STAR_WIDTH = 50;
    public static final int STAR_HEIGHT = STAR_WIDTH;
    public static final int DEFAULT_STAR_COLOR = 0xFFFFC100;
    public static final int DEFAULT_STAR_COUNT = 0;
    public static final int MAX_STAR_COUNT = 5;

    private int starCount;
    private int starColor;

    public StarView(Context context) {
        super(context);
    }

    public StarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int starColorAttr = DEFAULT_STAR_COLOR;
        int starCountAttr = DEFAULT_STAR_COUNT;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StarView);
            starColorAttr = typedArray.getColor(R.styleable.StarView_color, starColorAttr);
            starCountAttr = typedArray.getInt(R.styleable.StarView_count, starCountAttr);
            typedArray.recycle();
        }
        starColor = starColorAttr;
        starCount = starCountAttr;
    }

    public void setCount(int starCount){
        this.starCount = starCount;
        this.invalidate();
    }

    public int getCount(){
        return starCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int width = (STAR_WIDTH + leftPadding + rightPadding) * starCount;
        width = resolveSize(width, widthMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (starCount == 0) {
            return;
        }
        int paddingTop = getPaddingTop();
        int measuredHeight = getMeasuredHeight() - paddingTop - getPaddingBottom();
        Paint paint = new Paint();
        Path path = new Path();

        for (int i = 0; i < starCount; i++) {
            int size = measuredHeight;
            int leftPadding = size * i + 10;

            paint.setStrokeWidth(1);
            paint.setColor(starColor);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);

            Point a = new Point(leftPadding + size / 2, 0);
            Point b = new Point(leftPadding + size * 2 / 3, size / 3);
            Point c = new Point(leftPadding + size, size / 3);
            Point d = new Point(leftPadding + size * 4 / 5, size * 3 / 5);
            Point e = new Point(leftPadding + size * 6 / 7, size);
            Point f = new Point(leftPadding + size / 2, size * 4 / 5);
            Point g = new Point(leftPadding + size - size * 6 / 7, size);
            Point h = new Point(leftPadding + size - size * 4 / 5, size * 3 / 5);
            Point j = new Point(leftPadding, size / 3);
            Point k = new Point(leftPadding + size - size * 2 / 3, size / 3);


            path.setFillType(Path.FillType.WINDING);
            path.lineTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(d.x, d.y);
            path.lineTo(e.x, e.y);
            path.lineTo(f.x, f.y);
            path.lineTo(g.x, g.y);
            path.lineTo(h.x, h.y);
            path.lineTo(j.x, j.y);
            path.lineTo(k.x, k.y);
            path.lineTo(a.x, a.y);
            path.close();

        }
        canvas.drawPath(path, paint);
    }
}
