package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class RatingView extends View {

    private Drawable star;
    private Drawable emptyStar;
    private int starWidth;
    private int starHeight;
    private int starCount = 5;
    private int activeStarCount = 0;

    public RatingView(Context context) {
        this(context, null);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        star = getResources().getDrawable(R.drawable.ic_star_black_24dp);
        emptyStar = getResources().getDrawable(R.drawable.ic_star_border_black_24dp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        starWidth = MeasureSpec.getSize(heightMeasureSpec);
        starHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measuredWidth = resolveSize(starWidth * starCount, widthMeasureSpec);
        int measuredHeight = resolveSize(starHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < starCount; i++) {
            if (i < activeStarCount) {
                star.setBounds(i * starWidth, 0, (i + 1) * starWidth, starHeight);
                star.draw(canvas);
            } else {
                emptyStar.setBounds(i * starWidth, 0, (i + 1) * starWidth, starHeight);
                emptyStar.draw(canvas);
            }
        }

    }

    public void setActiveStarCount(int count) {
        if (count < 0) {
            activeStarCount = 0;
        } else if (count > starCount) {
            activeStarCount = starCount;
        } else {
            activeStarCount = count;
        }
        invalidate();
    }

    public void setStarCount(int count) {
        starCount = count;
    }

    public int getStarCount() {
        return starCount;
    }
}
