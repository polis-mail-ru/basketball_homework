package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


public class CustomView extends android.support.v7.widget.AppCompatImageView {
    private Bitmap bitmap;

    public CustomView(Context context) {
        super(context);
    }

    public void initStar(int w, int h) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float mid = h / 2;
        float min = Math.min(w, h);
        float fat = min / 17;
        float half = min / 2;
        mid = mid - half;
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        Path path = new Path();
        paint.setStrokeWidth(fat);
        paint.setStyle(Paint.Style.STROKE);

        path.reset();

        paint.setStyle(Paint.Style.FILL);

        // top left
        path.moveTo(mid + half * 0.5f, half * 0.84f);
        // top right
        path.lineTo(mid + half * 1.5f, half * 0.84f);
        // bottom left
        path.lineTo(mid + half * 0.68f, half * 1.45f);
        // top tip
        path.lineTo(mid + half * 1.0f, half * 0.5f);
        // bottom right
        path.lineTo(mid + half * 1.32f, half * 1.45f);
        // top left
        path.lineTo(mid + half * 0.5f, half * 0.84f);
        path.close();
        canvas.drawPath(path, paint);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(new CustomDrawable(
                ((BitmapDrawable) drawable).getBitmap()));
    }

    public void drawStar(int count) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap);
        Canvas canvas = new Canvas(bitmap1);
        for (int i = 0; i < count; i++) {
            canvas.drawBitmap(bitmap, bitmap.getHeight() * i, 0, paint);
        }
        super.setImageBitmap(bitmap1);
    }

    protected class CustomDrawable extends Drawable {
        //
        private Bitmap bitmap;

        public CustomDrawable(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public boolean isStateful() {
            // always return true
            return true;
        }

        @Override
        public int getOpacity() {
            // see documentation on android developers site
            return PixelFormat.OPAQUE;
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

        @Override
        public void setAlpha(int i) {
        }

        boolean pressed = false;

        @Override
        protected boolean onStateChange(int[] states) {
            // simplified but working
            for (int state : getState()) {
                if (state == android.R.attr.state_pressed ||
                        state == android.R.attr.state_focused)
                    pressed = true;
                else pressed = false;
            }
            invalidateSelf();
            return true;
        }
    }
}