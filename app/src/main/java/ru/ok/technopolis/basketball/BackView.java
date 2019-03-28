package ru.ok.technopolis.basketball;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class BackView extends android.support.v7.widget.AppCompatImageView {
    Bitmap bitmap;

    public BackView(Context context) {
        super(context);
    }

    public BackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void drawLine(Bitmap bitmap) {
        this.bitmap = bitmap;
        super.setImageBitmap(bitmap);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(new BackView.CustomDrawable(
                ((BitmapDrawable) drawable).getBitmap()));
    }

    protected class CustomDrawable extends Drawable {
        private Bitmap bitmap;

        CustomDrawable(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public boolean isStateful() {
            return true;
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        }

        @Override
        public void setAlpha(int i) {
        }

        boolean pressed = false;

        @Override
        protected boolean onStateChange(int[] states) {
            for (int state : getState()) {
                pressed = state == android.R.attr.state_pressed ||
                        state == android.R.attr.state_focused;
            }
            invalidateSelf();
            return true;
        }
    }
}
