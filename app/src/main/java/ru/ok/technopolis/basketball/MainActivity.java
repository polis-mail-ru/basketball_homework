package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import ru.ok.technopolis.basketball.animation.SwipeAnimation;
import ru.ok.technopolis.basketball.animation.SwipeAnimationBall;
import ru.ok.technopolis.basketball.view.CounterView;

public class MainActivity extends AppCompatActivity  {

    private SwipeAnimation swipeAnimationBall;

    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView player = findViewById(R.drawable.player);
        ImageView ball = findViewById(R.drawable.ball);
        ImageView hoop = findViewById(R.drawable.hoop);
        CounterView counterView = findViewById(R.id.main_activity__count_layout);
        final GestureDetector gestureDetector = new GestureDetector(this, gestureListener);
        swipeAnimationBall = new SwipeAnimationBall(ball);
        ball.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private final GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            swipeAnimationBall.throwing(velocityX,velocityY);
            return true;
        }
    };
}
