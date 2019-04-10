package ru.ok.technopolis.basketball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.annotation.SuppressLint;

public class MainActivity extends AppCompatActivity {
    private ImageView ball;
    private ImageView hoop;
    private float beginX;
    private float beginY;
    public int hits = 0;
    private boolean hit = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ball = findViewById(R.id.activity_main_ball);
        hoop = findViewById(R.id.activity_main_hoop);

        final ConstraintLayout activityMain = findViewById(R.id.activity_main);

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                beginX = ball.getX();
                beginY = ball.getY();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                float targetSize = (float) hoop.getWidth() / 3 - (float) ball.getWidth() *0.8f;
                float xHoop = hoop.getX();
                float yHoop = hoop.getY();

                return AnimatorTrack((e2.getRawX() - e1.getRawX()), (e2.getRawY() - e1.getRawY()), xHoop, yHoop, targetSize);
            }
        });

        activityMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public boolean AnimatorTrack(final float X, final float Y, final float xHoop, final float yHoop, final float targetSize) {
        ValueAnimator aniView = ValueAnimator.ofFloat(0, 1);

        aniView.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float x = ball.getX();
                float y = ball.getY();
                int rotation = 50 + (int) (Math.random() * 101);

                float value = (Float) valueAnimator.getAnimatedValue();
                float angle = value * 3.14f;

                ball.setRotation(rotation * value);
                ball.setTranslationX((float) ((1 - Math.cos(angle)) * X));
                ball.setTranslationY((float) (-Math.sin(angle) * Math.abs(Y)));

                if ((Math.abs(xHoop - x) <= targetSize) && (Math.abs(yHoop - y) <= targetSize)) hit = true;
            }
        });

        aniView.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ball.setX(beginX);
                ball.setY(beginY);
                if (hits ==5 || hits ==0) {
                    hits =0;
                    SetStars(hits);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationStart(animation);
                ball.setX(beginX);
                ball.setY(beginY);
                if (hit) {
                    hits++;
                    SetStars(hits);
                    hit = false;
                }
            }
        });
        aniView.setDuration(1200);
        aniView.start();

        return true;
    }

    public void SetStars(int hits) {
        ImageView star;
        switch (hits) {
            case 1:
                star = findViewById(R.id.imageStar1);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_red_24dp));
                break;
            case 2:
                star = findViewById(R.id.imageStar2);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_red_24dp));
                break;
            case 3:
                star = findViewById(R.id.imageStar3);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_red_24dp));
                break;
            case 4:
                star = findViewById(R.id.imageStar4);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_red_24dp));
                break;
            case 5:
                star = findViewById(R.id.imageStar5);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_red_24dp));
                hits = 0;
                break;
            default:
                star = findViewById(R.id.imageStar1);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_black_24dp));
                star = findViewById(R.id.imageStar2);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_black_24dp));
                star = findViewById(R.id.imageStar3);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_black_24dp));
                star = findViewById(R.id.imageStar4);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_black_24dp));
                star = findViewById(R.id.imageStar5);
                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_grade_black_24dp));
                hits = 0;
                break;
        }
        return;
    }
}
