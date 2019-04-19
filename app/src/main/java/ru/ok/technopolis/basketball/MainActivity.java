package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
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
    private Button resetButton;

    private FlingAnimation flingX;
    private FlingAnimation flingY;
    private SpringAnimation springX;
    private SpringAnimation springY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detector = new GestureDetector(this, new MyGesturedListener());

        ballView = findViewById(R.id.activity_main_ball);
        scoreView = findViewById(R.id.activity_main_score);
        hoopView = findViewById(R.id.activity_main_hoop);
        resetButton = findViewById(R.id.activity_main_reset);

        flingX = new FlingAnimation(ballView, DynamicAnimation.X);
        flingY = new FlingAnimation(ballView, DynamicAnimation.Y);
        flingY.addEndListener(animationEndListener);
        springX = new SpringAnimation(ballView, DynamicAnimation.TRANSLATION_X, 0);
        springY = new SpringAnimation(ballView, DynamicAnimation.TRANSLATION_Y, 0);

        ballView.setOnTouchListener(touchListener);
        resetButton.setOnClickListener(clickListener);
    }

    private DynamicAnimation.OnAnimationEndListener animationEndListener = new DynamicAnimation.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
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
            }
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
        springX.cancel();
        springY.cancel();
    }
}