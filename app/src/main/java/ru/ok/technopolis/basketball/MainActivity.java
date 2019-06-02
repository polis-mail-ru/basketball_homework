package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private VelocityTracker velocityTracker;
    private FlingAnimation flingX;
    private FlingAnimation flingY;
    private ImageView ball;
    private ImageView hoop;
    private ImageView player;
    private Button buttonReset;
    private Score score;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ball = findViewById(R.id.ball);
        hoop = findViewById(R.id.hoop);
        player = findViewById(R.id.player);
        score = findViewById(R.id.score_custom_view);
        buttonReset = findViewById(R.id.button_reset);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score.reset();
            }
        });

        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    velocityTracker = VelocityTracker.obtain();
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.setX(v.getX() + x);
                    v.setY(v.getY() + y);
                    velocityTracker.addMovement(event);
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {
                    velocityTracker.computeCurrentVelocity(500);
                    Action();
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    velocityTracker.clear();
                }
                return true;
            }
        });
    }

    private void Action() {
        flingX = new FlingAnimation(ball, DynamicAnimation.X);
        flingX.setStartVelocity(velocityTracker.getXVelocity()).setFriction(0.8f);
        flingY = new FlingAnimation(ball, DynamicAnimation.Y);
        flingY.setFriction(0.8f).setStartVelocity(velocityTracker.getYVelocity());
        flingX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
                float hoopStartX = hoop.getX() + 0.3f * hoop.getWidth();
                float hoopEndX = hoop.getX() + 0.7f * hoop.getWidth();
                float hoopStartY = hoop.getY() + 0.3f * hoop.getHeight();
                float hoopEndY = hoop.getY() + hoop.getHeight();
                float ballX = ball.getX() +  0.4f * ball.getWidth();
                float ballY = ball.getY() + 0.4f * ball.getHeight();
                if ((ballX > hoopStartX & ballX < hoopEndX) & (ballY > hoopStartY & ballY < hoopEndY)) {
                    score.increment();
                }
                flingY.cancel();
                ball.setX(player.getX() + 0.4f * (player.getWidth() - ball.getWidth()));
                ball.setY(player.getY() - ball.getHeight());
            }
        });
        flingX.start();
        flingY.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        flingX.cancel();
        flingY.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flingX.cancel();
        flingY.cancel();
        velocityTracker.recycle();
    }
}
