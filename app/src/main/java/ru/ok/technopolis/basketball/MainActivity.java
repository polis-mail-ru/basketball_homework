package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final float G_ACCELERATION = 3000;
    private static final float ANIMATION_DURATION = 2000;

    private VelocityTracker velocityTracker;
    private AnimatorSet animatorSet;
    private float initBallX;
    private float initBallY;

    private Rectangle hoopTarget;
    private boolean hit;
    private int allThrows;
    private int successfulThrows;

    private int width;
    private int height;

    private View getBall() {
        return findViewById(R.id.ball);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        final View ball = getBall();
        ViewTreeObserver ballObserver = ball.getViewTreeObserver();
        ballObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                ball.getLocationOnScreen(location);
                initBallX = location[0];
                initBallY = location[1];
                ball.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        final View hoop = findViewById(R.id.hoop);
        ViewTreeObserver hoopObserver = hoop.getViewTreeObserver();
        hoopObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                hoop.getLocationOnScreen(location);
                hoopTarget = new Rectangle();
                hoopTarget.setBounds(location[0], location[1], hoop.getWidth(), hoop.getHeight());
                hoop.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        velocityTracker = VelocityTracker.obtain();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        velocityTracker.addMovement(event);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        velocityTracker.computeCurrentVelocity(1000);
                        animateBall(v, velocityTracker.getXVelocity(), velocityTracker.getYVelocity());
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        velocityTracker.clear();
                    }
                }
                return true;
            }
        });
    }

    private void animateBall(final View ball, final float xVelocity, final float yVelocity) {

        TypeEvaluator<Float> xEvaluator = new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
//                float result = startValue + fraction * xVelocity;
                return startValue + fraction * (endValue - startValue);
            }
        };
        final ValueAnimator xAnimator = ValueAnimator.ofFloat(initBallX, width);
        xAnimator.setEvaluator(xEvaluator);

        xAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ball.setX((Float) xAnimator.getAnimatedValue());
                calculateRating();
            }
        });

        TypeEvaluator<Float> yEvaluator = new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return startValue + fraction * yVelocity + (G_ACCELERATION * (float) Math.pow(fraction, 2)) / 2;
            }
        };
        final ValueAnimator yAnimator = ValueAnimator.ofFloat(initBallY, height);
        yAnimator.setEvaluator(yEvaluator);
        yAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ball.setY((Float) yAnimator.getAnimatedValue());
                calculateRating();
            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(xAnimator, yAnimator);
        animatorSet.setDuration((long) ANIMATION_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ball.setX(initBallX);
                ball.setY(initBallY);
                hit = false;
            }
        });
        animatorSet.start();
        allThrows++;
        updateViews();
    }

    private void calculateRating() {
        if (checkHit() && !hit) {
            successfulThrows++;
            updateViews();
            hit = true;
        }
    }

    private void updateViews() {
        RatingView ratingView = findViewById(R.id.star_rating);
        int starCount = ratingView.getStarCount();
        float rating = ((float) successfulThrows / allThrows) * starCount;
        ratingView.setActiveStarCount(Math.round(rating));
        TextView counter = findViewById(R.id.point_counter);
        counter.setText(String.format("%d/%d", successfulThrows, allThrows));
    }

    private boolean checkHit() {
        View ball = getBall();
        return hoopTarget.contains((int) ball.getX(), (int) ball.getY());
    }

    @Override
    protected void onStop() {
        super.onStop();
        velocityTracker.recycle();
        animatorSet.cancel();
    }
}
