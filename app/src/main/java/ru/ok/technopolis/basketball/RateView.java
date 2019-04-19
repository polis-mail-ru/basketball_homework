package ru.ok.technopolis.basketball;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

public class RateView extends android.support.v7.widget.AppCompatSeekBar {

    private static final int DEFAULT_STAR_SIZE_DP = 10;
    private static final int DEFAULT_COUNT_STARS = 5;
    private static final int DEFAULT_STAR_COLOR = Color.YELLOW;

    private Rect progressRect;
    private int starSize;
    private int count;
    private int color;
    private Bitmap bitmap;

    private final Paint paint;

    public RateView(Context context) {
        this(context, null);
    }

    public RateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressRect = new Rect();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int starSizeFromAttr = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STAR_SIZE_DP, displayMetrics) + 0.5f);
        int countStarsFromAttr = DEFAULT_COUNT_STARS;
        int colorStarFromAttr = DEFAULT_STAR_COLOR;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RateView);
            starSizeFromAttr = typedArray.getDimensionPixelSize(R.styleable.RateView_star_size, starSizeFromAttr);
            countStarsFromAttr = typedArray.getInteger(R.styleable.RateView_count_stars, countStarsFromAttr);
            colorStarFromAttr = typedArray.getInteger(R.styleable.RateView_star_color, colorStarFromAttr);
            typedArray.recycle();
        }
        starSize = starSizeFromAttr;
        count = countStarsFromAttr;
        color = colorStarFromAttr;

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        bitmap = Bitmap.createScaledBitmap(bitmap, starSize, starSize, true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int topPadding = getPaddingTop();
        int botPadding = getPaddingBottom();

        int width = count * starSize + leftPadding + rightPadding;
        int height = starSize + topPadding + botPadding;
        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);
        starSize = Math.min(measuredHeight, starSize);

        Log.d("RateView-measure", "count = " + count + "width = " + width + " starSize = " + starSize);
        count = Math.min(count, measuredWidth / starSize);
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int measureHeight = h - getPaddingTop() - getPaddingBottom();
        int measureWidth = w - getPaddingLeft() - getPaddingRight();
        progressRect.top = getPaddingTop() + measureHeight / 2 - starSize / 2;
        progressRect.bottom = progressRect.top + starSize;
        progressRect.left = (int) ((float) measureWidth / 2f - (float) count * starSize / 2f) + getPaddingLeft();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        progressRect.right = (int) ((double) getProgress() / getMax() * (count * starSize)) + progressRect.left;
        canvas.drawRect(progressRect, paint);
        for (int i = 0, x = progressRect.left; i < count && x < getWidth() - getPaddingRight(); i++, x += starSize) {
            canvas.drawBitmap(bitmap, x, progressRect.top, paint);
        }
    }
}
