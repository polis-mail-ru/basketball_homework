package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ImageView mViewTobeFlung;
    private ImageView point;
    private RelativeLayout mMainLayout;
    private int score = 0;
    private BackView backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mViewTobeFlung = findViewById(R.id.iv_translate_fling);
        mMainLayout = findViewById(R.id.main_layout);
        point = findViewById(R.id.empty);

        final GestureDetector gestureDetector = new GestureDetector(this, mGestureListener);

        mMainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            backView = findViewById(R.id.back);
            mViewTobeFlung.setVisibility(View.VISIBLE);
            backView.setVisibility(View.VISIBLE);
            final float distanceInX = moveEvent.getRawX() - downEvent.getRawX();
            final float distanceInY = Math.abs(moveEvent.getRawY() - downEvent.getRawY());
            final Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(10);
            final Bitmap bitmap = Bitmap.createBitmap(backView.getWidth(), backView.getHeight(), Bitmap.Config.ARGB_8888);
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                boolean scored = false;
                float w = mViewTobeFlung.getWidth() / 2, h = mViewTobeFlung.getHeight() / 2;
                float x = mViewTobeFlung.getX() + w, y = mViewTobeFlung.getY() + h;
                Canvas canvas = new Canvas(bitmap);
                int n = 0;

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) (animation.getAnimatedValue()))
                            .floatValue();
                    mViewTobeFlung.setTranslationX((float) (distanceInX * -Math.cos(value * Math.PI) + distanceInX));
                    mViewTobeFlung.setTranslationY((float) (distanceInY * -Math.sin(value * Math.PI)));
                    if (y != mViewTobeFlung.getY() + h && n % 2 == 0) {
                        canvas.drawLine(x, y, mViewTobeFlung.getX() + w, mViewTobeFlung.getY() + h, paint);
                    }
                    n++;
                    x = mViewTobeFlung.getX() + w;
                    y = mViewTobeFlung.getY() + h;
                    if ((y >= point.getY() - point.getHeight() / 2 && y <= point.getY() + point.getHeight() / 2) && (x >= point.getX() - point.getWidth() / 2 && x <= point.getX()) && !scored) {
                        score();
                        scored = true;
                    }
                }
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mViewTobeFlung.setVisibility(View.INVISIBLE);
                    backView.setVisibility(View.INVISIBLE);
                }
            });

            animator.start();
            mViewTobeFlung.animate().start();
            backView.drawLine(bitmap);

            return true;
        }
    };

    private void score() {
        score++;
        Log.d("", "score: SCORED");
        CustomView view = findViewById(R.id.view);
        view.initStar(view.getWidth(), view.getHeight());
        view.drawStar(score);

    }
}