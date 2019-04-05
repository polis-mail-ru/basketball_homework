package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.ok.technopolis.basketball.animation.SwipeAnimation;
import ru.ok.technopolis.basketball.animation.SwipeAnimationBall;
import ru.ok.technopolis.basketball.view.Counter;

public class MainActivity extends AppCompatActivity  implements EventContext {

    private SwipeAnimation swipeAnimationBall;
    public boolean throwing;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ball_target = findViewById(R.id.main_activity__target);
        ImageView ball = findViewById(R.id.main_activity__ball);
        Counter counterView = findViewById(R.id.main_activity__count_layout);
        gestureDetector = new GestureDetector(this, gestureListener);
        final ViewGroup mainLayout = findViewById(R.id.main_activity__mainLayout);
        swipeAnimationBall = new SwipeAnimationBall(ball, counterView, ball_target, mainLayout);
        swipeAnimationBall.setEventContext(this);
        mainLayout.setOnTouchListener(onTouchListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        swipeAnimationBall.rollback();
    }

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };

    private final GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(!throwing) {
                swipeAnimationBall.throwing(velocityX,velocityY);
                throwing = true;
                return true;
            }
            return false;
        }
    };

    @Override
    public void throwing(boolean throwing) {
        this.throwing = throwing;
        Log.d("rollback throwing", String.valueOf(this.throwing));
    }
}