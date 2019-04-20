package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private GestureDetector detector;

    private ImageView ballView;
    private ScoreView scoreView;
    private ImageView hoopView;
    private Guideline leftGuideLine;
    private Guideline rightGuideLine;
    private Guideline bottomGuideLine;

    private FlingAnimation flingX;
    private FlingAnimation flingY;
    private FlingAnimation flingFallY;
    private SpringAnimation springX;
    private SpringAnimation springY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detector = new GestureDetector(this, new MyGesturedListener());

        leftGuideLine = findViewById(R.id.activity_main_guide_line_left);
        rightGuideLine = findViewById(R.id.activity_main_guide_line_right);
        bottomGuideLine = findViewById(R.id.activity_main_guide_line_bottom);
        ballView = findViewById(R.id.activity_main_ball);
        scoreView = findViewById(R.id.activity_main_score);
        hoopView = findViewById(R.id.activity_main_hoop);
        Button resetButton = findViewById(R.id.activity_main_reset);

        flingX = new FlingAnimation(ballView, DynamicAnimation.X);
        flingY = new FlingAnimation(ballView, DynamicAnimation.Y);
        flingFallY = new FlingAnimation(ballView, DynamicAnimation.Y);

        flingX.addEndListener(animationEndListenerX);
        flingFallY.addEndListener(animationEndListenerFallY);

        springX = new SpringAnimation(ballView, DynamicAnimation.TRANSLATION_X, 0);
        springY = new SpringAnimation(ballView, DynamicAnimation.TRANSLATION_Y, 0);

        ballView.setOnTouchListener(touchListener);
        resetButton.setOnClickListener(clickListener);
    }

    private DynamicAnimation.OnAnimationEndListener animationEndListenerX = new DynamicAnimation.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
            flingY.cancel();
            float ballMidX = ballView.getX() + ballView.getWidth() / 2f;
            float ballMidY = ballView.getY() + ballView.getHeight() / 2f;

            float targetWidth = hoopView.getWidth() / 3f;
            float targetHeight = hoopView.getHeight() / 3f;

            float leftTargetBorder = hoopView.getLeft() + targetWidth;
            float rightTargetBorder = hoopView.getRight() - targetWidth;
            float bottomTargetBorder = hoopView.getBottom() - targetHeight;
            float topTargetBorder = hoopView.getTop() + targetHeight;

            if (ballMidX > leftTargetBorder && ballMidX < rightTargetBorder && ballMidY > topTargetBorder && ballMidY < bottomTargetBorder) {
                scoreView.incrementScore();
                flingFallY.setMaxValue(bottomGuideLine.getBottom());
                flingFallY.setFriction(1.2f);
                flingFallY.setStartVelocity(5000);
                flingFallY.start();
            } else {
                springX.start();
                springY.start();
            }
        }
    };

    private DynamicAnimation.OnAnimationEndListener animationEndListenerFallY = new DynamicAnimation.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
            springX.start();
            springY.start();
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            scoreView.resetScore();
            springX.start();
            springY.start();
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return detector.onTouchEvent(event);
        }
    };

    private class MyGesturedListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            flingX.setStartVelocity(velocityX);
            flingY.setStartVelocity(velocityY);

            flingX.setFriction(2f);
            flingY.setFriction(4f);

            flingX.setMinValue(leftGuideLine.getLeft());
            flingY.setMinValue(0);

            flingX.setMaxValue(rightGuideLine.getRight());
            flingY.setMaxValue(bottomGuideLine.getBottom());

            flingX.start();
            flingY.start();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        flingX.cancel();
        flingY.cancel();
        flingFallY.cancel();

        springX.cancel();
        springY.cancel();
    }
}