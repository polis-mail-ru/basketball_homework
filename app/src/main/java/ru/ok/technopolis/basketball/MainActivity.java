package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean isReady;
    private ImageView person;
    private ImageView ball;
    private ImageView hoop;
    private FrameLayout layout;
    private RateView rateView;
    private TextView textViewScore;

    private int goodCount;
    private int count;
    private Rect window;
    private Rect rectBall;
    private Rect rectHoop;
    private Vibrator vibrator;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        layout = findViewById(R.id.container);
        ball = findViewById(R.id.ball);
        person = findViewById(R.id.person);
        hoop = findViewById(R.id.hoop);
        rateView = findViewById(R.id.rateview);
        textViewScore = findViewById(R.id.score);
        final GestureDetector gestureDetector = new GestureDetector(this, gestureListener);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    person.setX(event.getX() - (float) person.getWidth() / 2);
                }
                return gestureDetector.onTouchEvent(event);
            }
        });


        startGame();
    }

    private void startGame() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        window = new Rect();
        Display display = getWindowManager().getDefaultDisplay();
        display.getRectSize(window);
        rectBall = new Rect();
        rectHoop = new Rect();
        goodCount = count = 0;
        changeScore();
        isReady = true;
        final ValueAnimator animator1 = ValueAnimator.ofInt(window.left, window.right);
        final ValueAnimator animator2 = ValueAnimator.ofInt(window.right, window.left);

        animator1.setInterpolator(new DecelerateInterpolator());
        animator2.setInterpolator(new DecelerateInterpolator());
        animator1.setDuration(1000);
        animator2.setDuration(1000);
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value >= 0 && value <= window.right - hoop.getWidth()) {
                    hoop.setX(value);
               } else {
                    animation.cancel();
                }
            }
        };
        animator1.addUpdateListener(listener);
        animator2.addUpdateListener(listener);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animator2.start();
            }
        });

        animator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animator1.start();
            }
        });
        animator1.start();
    }


    @SuppressLint("SetTextI18n")
    private void changeScore() {
        textViewScore.setText(goodCount + "/" + count);
        int progress = count == 0 ? 0 : (int) ((double) goodCount / count * 100);
        rateView.setProgress(progress);
    }

    private void vibrate() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(100);
        }
    }

    GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            if (isReady && velocityY < 0) {
                isReady = false;
                Log.d("Velocity", "vx = " + velocityX + " vy = " + velocityY);
                startAnimation(downEvent.getRawX(), downEvent.getRawY(), velocityX / 7, -velocityY / 7);
            }
            return true;
        }


        private void startAnimation(final float x0_b, final float y0_b, final float width, final float height) {
            final ValueAnimator animatorX = ValueAnimator.ofFloat(x0_b, x0_b + width);
            final ValueAnimator animatorY1 = ValueAnimator.ofFloat(y0_b, y0_b - height);
            final ValueAnimator animatorY2 = ValueAnimator.ofFloat(y0_b - height, y0_b);

            animatorY1.setInterpolator(new DecelerateInterpolator());
            animatorY2.setInterpolator(new AccelerateInterpolator());
            animatorX.setDuration(1000);
            animatorY1.setDuration(500);
            animatorY2.setDuration(500);
            animatorY2.setStartDelay(500);

            animatorY1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) (animation.getAnimatedValue());
                    ball.setY(value);
                    if (inHoop()) {
                        vibrate();
                        animatorX.cancel();
                    }
                }
            });

            animatorY2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) (animation.getAnimatedValue());
                    ball.setY(value);
                    if (inHoop()) {
                        goodCount++;
                        animatorX.cancel();
                    }
                }
            });

            animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) (animation.getAnimatedValue());
                    ball.setX(value);
                    if (crossWall()) {
                        vibrate();
                        animation.cancel();
                    }

                }
            });

            animatorX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animatorY1.cancel();
                    animatorY2.cancel();
                    ball.setX(200);
                    ball.setY(1000);
                    isReady = true;
                    count++;
                    changeScore();
                }
            });

            animatorX.start();
            animatorY1.start();
            animatorY2.start();
        }

        private boolean crossWall() {
            return ball.getX() > window.right || ball.getX() < window.left || ball.getY() > window.bottom || ball.getY() < window.top;
        }

        private boolean inHoop() {
            getRectBall(rectBall);
            getRectHoop(rectHoop);
            Log.d("MainActivityWall", "hoop = " + rectHoop.toShortString() + " ball = " + rectBall.toShortString());

            return Rect.intersects(rectBall, rectHoop);
        }


    };


    private void getRectBall(Rect rect) {
        if (rect == null) {
            return;
        }
        rect.left = (int) ball.getX();
        rect.right = (int) (ball.getX() + ball.getWidth());
        rect.top = (int) (ball.getY());
        rect.bottom = (int) (ball.getY() + ball.getHeight());
    }

    private void getRectHoop(Rect rect) {
        if (rect == null) {
            return;
        }
        rect.left = (int) (hoop.getX() + hoop.getWidth() / 4);
        rect.right = (int) (hoop.getX() + hoop.getWidth() * 0.75);
        rect.top = (int) (hoop.getY() + hoop.getHeight() / 2);
        rect.bottom = (int) (hoop.getY() + hoop.getHeight());
    }

}
