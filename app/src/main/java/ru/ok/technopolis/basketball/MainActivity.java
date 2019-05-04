package ru.ok.technopolis.basketball;

import android.graphics.drawable.Drawable;
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

        final ConstraintLayout idMain = findViewById(R.id.activity_main);

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

        idMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    public boolean AnimatorTrack(final float X, final float Y, final float xHoop, final float yHoop, final float targetSize) {
        ValueAnimator anyView = ValueAnimator.ofFloat(0, 1);

        anyView.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float x = ball.getX();
                float y = ball.getY();
                int rotation = 50 + (int) (Math.random() * 101);

                float value = (float) valueAnimator.getAnimatedValue();
                float angle = value * 3.14f;

                ball.setRotation(rotation * value);
                ball.setTranslationX((float) ((1 - Math.cos(angle)) * X));
                ball.setTranslationY((float) (-Math.sin(angle) * Math.abs(Y)));

                if ((Math.abs(xHoop - x) <= targetSize) && (Math.abs(yHoop - y) <= targetSize)) { hit = true;}
            }
        });

        anyView.addListener(new AnimatorListenerAdapter() {

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
        anyView.setDuration(1200);
        anyView.start();

        return true;
    }

    public void SetStars(int hits) {
        Drawable redStar = getResources().getDrawable(R.drawable.ic_grade_red_24dp);
        Drawable blackStar = getResources().getDrawable(R.drawable.ic_grade_black_24dp);
        ImageView star1=findViewById(R.id.image_Star1);
        ImageView star2=findViewById(R.id.image_Star2);
        ImageView star3=findViewById(R.id.image_Star3);
        ImageView star4=findViewById(R.id.image_Star4);
        ImageView star5=findViewById(R.id.image_Star5);

        switch (hits) {
            case 1:
                star1.setImageDrawable(redStar);
                break;
            case 2:
                star2.setImageDrawable(redStar);
                break;
            case 3:
                star3.setImageDrawable(redStar);
                break;
            case 4:
                star4.setImageDrawable(redStar);
                break;
            case 5:
                star5.setImageDrawable(redStar);
                hits = 0;
                break;
            default:
                star1.setImageDrawable(blackStar);
                star2.setImageDrawable(blackStar);
                star3.setImageDrawable(blackStar);
                star4.setImageDrawable(blackStar);
                star5.setImageDrawable(blackStar);
                hits = 0;
                break;
        }
        return;
    }
}
