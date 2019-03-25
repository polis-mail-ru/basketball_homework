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
import android.util.Log;

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
        Log.d("", "drawLine: dawdadwa1");
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(new BackView.CustomDrawable(
                ((BitmapDrawable) drawable).getBitmap()));
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
            Log.d("", "draw: ddwadwa");
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
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
