package ru.ok.technopolis.basketball;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;
import java.util.Random;

public class RatingView extends View {

    private final String COUNTER_TEXT_DEFAULT = "00";
    private final int[] colors = {android.R.color.black,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_purple,
            android.R.color.holo_red_light};
    private int counter;
    private String counterText;
    private Random random = new Random();

    private Paint textPaint;

    public RatingView(Context context) {
        this(context, null);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setSubpixelText(true);

        float textSize = Math.round(40f * getResources().getDisplayMetrics().density);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingView);
            textSize = typedArray.getDimensionPixelSize(R.styleable.RatingView_android_textSize, Math.round(textSize));
            typedArray.recycle();
        }
        textPaint.setTextSize(textSize);
        setCounter(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int textWidth = Math.round(textPaint.measureText(COUNTER_TEXT_DEFAULT)
                + getPaddingLeft() + getPaddingRight());
        int textHeight = Math.round(textPaint.getFontMetrics().bottom -
                textPaint.getFontMetrics().top + getPaddingTop() + getPaddingBottom());
        int measuredWidth = resolveSize(textWidth, widthMeasureSpec);
        int measuredHeight = resolveSize(textHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int textStartCoord = Math.round(0.5f * (getWidth() - textPaint.measureText(counterText)));
        int textBaselineCoord = Math.round(getHeight() * 0.8f);
        canvas.drawText(counterText, textStartCoord, textBaselineCoord, textPaint);
    }

    public void resetCounter() {
        setCounter(0);
    }

    public void incrementCounter() {
        setCounter(counter + 1);
    }

    private void setCounter(int counter) {
        this.counter = counter;
        counterText = String.format(Locale.getDefault(), "%2d", counter);
        textPaint.setColor(getResources().getColor(colors[random.nextInt(colors.length)]));
        invalidate();
    }
}
