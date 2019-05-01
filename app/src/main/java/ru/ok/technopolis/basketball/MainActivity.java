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
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final float G_ACCELERATION = 3000;
    private static final float ANIMATION_DURATION = 2000;

    private View ball;
    private RatingView ratingView;
    private View hoop;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ball = findViewById(R.id.ball);
        ratingView = findViewById(R.id.star_rating);
        hoop = findViewById(R.id.hoop);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        ball.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                ball.getLocationOnScreen(location);
                initBallX = location[0];
                initBallY = location[1];
            }
        });

        hoop.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                hoop.getLocationOnScreen(location);
                hoopTarget = new Rectangle();
                hoopTarget.setBounds(location[0], location[1], hoop.getWidth(), hoop.getHeight());
            }
        });

        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (velocityTracker != null) {
                            velocityTracker.recycle();
                        }
                        velocityTracker = VelocityTracker.obtain();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        velocityTracker.addMovement(event);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        velocityTracker.computeCurrentVelocity(1000);
                        animateBall(v, velocityTracker.getYVelocity());
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

    private void animateBall(final View ball, final float yVelocity) {

        TypeEvaluator<Float> xEvaluator = new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
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
        int starCount = ratingView.getStarCount();
        float rating = ((float) successfulThrows / allThrows) * starCount;
        ratingView.setActiveStarCount(Math.round(rating));
        TextView counter = findViewById(R.id.point_counter);
        counter.setText(String.format(Locale.ENGLISH, "%d/%d", successfulThrows, allThrows));
    }

    private boolean checkHit() {
        return hoopTarget.contains((int) ball.getX(), (int) ball.getY());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }
}
