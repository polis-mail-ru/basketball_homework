package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private VelocityTracker velocityTracker;
    private FlingAnimation flingAnimationX;
    private FlingAnimation flingAnimationY;
    private RatingView ratingView;
    private ImageView ballImageView;
    private ImageView hoopImageView;
    private ImageView playerImageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingView = findViewById(R.id.rating_custom_view);
        Button resetScoreButton = findViewById(R.id.reset_score);
        ballImageView = findViewById(R.id.ball);
        hoopImageView = findViewById(R.id.hoop);
        playerImageView = findViewById(R.id.player);

        resetScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingView.resetCounter();
            }
        });

        ballImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        velocityTracker = VelocityTracker.obtain();
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        v.setX(v.getX() + x);
                        v.setY(v.getY() + y);
                        velocityTracker.addMovement(event);
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        velocityTracker.computeCurrentVelocity(300);
                        startAnimations(velocityTracker.getXVelocity(), velocityTracker.getYVelocity());
                        velocityTracker.recycle();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flingAnimationX.cancel();
        flingAnimationY.cancel();
        velocityTracker.recycle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        flingAnimationX.cancel();
        flingAnimationY.cancel();
    }

    private void startAnimations(final float xVelocity, final float yVelocity) {
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        flingAnimationX = new FlingAnimation(ballImageView, DynamicAnimation.X);
        flingAnimationX.setStartVelocity(xVelocity).setFriction(0.5f);
        flingAnimationY = new FlingAnimation(ballImageView, DynamicAnimation.Y);
        flingAnimationY.setFriction(0.5f).setStartVelocity(yVelocity);
        flingAnimationX.setMinValue(-size.x * 0.5f).setMaxValue(size.x * 1.5f);
        flingAnimationY.setMinValue(-size.y * 0.5f).setMaxValue(size.y * 1.5f);
        flingAnimationX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
                if (checkHit()) {
                    ratingView.incrementCounter();
                }
                flingAnimationY.cancel();
                ballImageView.setX(playerImageView.getX() + 0.5f * (playerImageView.getWidth() - ballImageView.getWidth()));
                ballImageView.setY(playerImageView.getY() - ballImageView.getHeight());
            }
        });
        flingAnimationX.start();
        flingAnimationY.start();
    }

    private boolean checkHit() {
        float ballX = ballImageView.getX() + ballImageView.getWidth() * 0.5f;
        float ballY = ballImageView.getY() + ballImageView.getHeight() * 0.5f;
        float hoopX1 = hoopImageView.getX() + hoopImageView.getWidth() * 0.33f;
        float hoopX2 = hoopImageView.getX() + hoopImageView.getWidth() * 0.66f;
        float hoopY1 = hoopImageView.getY() + hoopImageView.getHeight() * 0.33f;
        float hoopY2 = hoopImageView.getY() + hoopImageView.getHeight();
        return (ballX > hoopX1 & ballX < hoopX2) & (ballY > hoopY1 & ballY < hoopY2);
    }
}
