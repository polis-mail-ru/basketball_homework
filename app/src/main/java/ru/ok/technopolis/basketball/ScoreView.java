package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ScoreView extends View {
    private int score;
    private Paint paint;
    private Path path;
    private float starBlockWidth;
    private float starBlockMidY;
    private float starBlockMidX;
    private float starDiam;

    public ScoreView(Context context) {
        super(context, null);
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        score = 0;
        path = new Path();
        paint = new Paint();
    }

    public void incrementScore() {
        score++;
        if (score > 39) {
            paint.setColor(getResources().getColor(R.color.colorGold));
        } else if (score > 19) {
            paint.setColor(getResources().getColor(R.color.colorSilver));
        } else if (score > 9) {
            paint.setColor(getResources().getColor(R.color.colorBronze));
        }
        invalidate();
    }

    public void resetScore() {
        score = 0;
        paint.setColor(getResources().getColor(R.color.colorBlack));
        invalidate();
    }

    private void createStarPath(float biasX) {
        path.reset();
        path.moveTo(biasX + starBlockMidX - starDiam * 0.5f, starBlockMidY - starDiam * 0.16f);
        path.lineTo(biasX + starBlockMidX + starDiam * 0.5f, starBlockMidY - starDiam * 0.16f);
        path.lineTo(biasX + starBlockMidX - starDiam * 0.32f, starBlockMidY + starDiam * 0.45f);
        path.lineTo(biasX + starBlockMidX, starBlockMidY - starDiam * 0.5f);
        path.lineTo(biasX + starBlockMidX + starDiam * 0.32f, starBlockMidY + starDiam * 0.45f);
        path.lineTo(biasX + starBlockMidX - starDiam * 0.5f, starBlockMidY - starDiam * 0.16f);
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float biasX = 0;
        for (int i = 0; i < score; i++) {
            createStarPath(biasX);
            canvas.drawPath(path, paint);
            if (score > 3) {
                canvas.drawText("x" + score, starBlockWidth, starBlockMidY + starDiam * 0.3f, paint);
                break;
            }
            biasX += starBlockWidth;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        starBlockWidth = w / 3f;
        starBlockMidX = starBlockWidth / 2f;
        starBlockMidY = h / 2f;
        starDiam = Math.min(starBlockMidX, starBlockMidY);
        paint.setColor(getResources().getColor(R.color.colorBlack));
        paint.setTextSize(starDiam);
    }
}
