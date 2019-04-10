package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

public class AccuracyView extends View {

    private static final int DEFAULT_ITEM_WIDTH_DP = 10;
    private static final int DEFAULT_ITEM_COLOR = Color.WHITE;

    private final Path wavePath = new Path();
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint goodPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int itemWidth;
    private List<Double> originalData;

    private List<Double> measuredData;

    public AccuracyView(Context context) {
        this(context, null);
    }

    public AccuracyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int itemWidthFromAttr = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ITEM_WIDTH_DP, displayMetrics) + 0.5f);
        int itemColorFromAttr = DEFAULT_ITEM_COLOR;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccuracyView);
            itemWidthFromAttr = typedArray.getDimensionPixelSize(R.styleable.AccuracyView_itemWidth, itemWidthFromAttr);
            itemColorFromAttr = typedArray.getColor(R.styleable.AccuracyView_itemColor, itemColorFromAttr);
            typedArray.recycle();
        }
        itemWidth = itemWidthFromAttr;
        originalData = AccuracyResource.getElements();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(itemColorFromAttr);
        linePaint.setStrokeWidth(itemWidthFromAttr);
        goodPaint.setStyle(Paint.Style.STROKE);
        goodPaint.setColor(Color.GREEN);
        goodPaint.setStrokeWidth(itemWidthFromAttr);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        if (originalData == null || originalData.size() <= 0) {
            originalData = new ArrayList<>();
            originalData.add(0d);
        }
        int len = originalData.size();
        if(len > 15){
            len = 15;
        }
        int width = len * itemWidth * 2 - itemWidth + leftPadding + rightPadding;
        width = resolveSize(width, widthMeasureSpec);
        int itemCount = originalData.size();
        measuredData = LinearInterpolation.interpolateList(originalData, itemCount);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (measuredData == null) {
            return;
        }
        wavePath.reset();
        int paddingTop = getPaddingTop();

        int measuredHeight = getMeasuredHeight() - paddingTop - getPaddingBottom();
        int currentX = itemWidth + getPaddingLeft();
        int count = 0;
        for (Double data : measuredData) {
            if (count > 15) {
                break;
            }
            double height = (data / AccuracyResource.getMaxHeight()) * measuredHeight;
            float startY = (float) (paddingTop + measuredHeight - height);
            float endY = paddingTop + startY + measuredHeight;

//            wavePath.moveTo(currentX, startY);
//            wavePath.lineTo(currentX, endY);
            if (data >= 0.99) {
                canvas.drawLine(currentX, startY, currentX, endY, goodPaint);
            } else {
                canvas.drawLine(currentX, startY, currentX, endY, linePaint);
            }
            currentX += itemWidth * 2;
            count++;
        }
//        canvas.drawPath(wavePath, linePaint);
    }
}
