package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public static final int RESULT_MESSAGE = 1;
    public static final String SCORE_KEY = "score";
    public static final String MODE_KEY = "mode";
    public static final String AMOUNT_KEY = "amount";

    public enum Mode {
        EASY(0),
        MEDIUM(3000),
        HARD(1000);

        private long duration;

        Mode(long duration) {
            this.duration = duration;
        }
    }

    private boolean ready;
    private ImageView person;
    private ImageView ball;
    private ImageView hoop;
    protected RateView rateView;
    protected TextView textViewScore;
    private FrameLayout layout;

    protected int goodCountBallsPlayer1;
    protected int countBalls;
    protected int maxCount;
    private Rect window;
    private Rect rectBall;
    private Rect rectHoop;
    private Vibrator vibrator;
    private Mode mode;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSettings();
        setContentView(R.layout.activity_game);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        layout = findViewById(R.id.activity_game__container);
        ball = findViewById(R.id.activity_game__ball);
        person = findViewById(R.id.activity_game__person);
        hoop = findViewById(R.id.activity_game__hoop);
        rateView = findViewById(R.id.activity_game__rateview);
        textViewScore = findViewById(R.id.activity_game__score);
        mediaPlayer = MediaPlayer.create(this, R.raw.hlop);
        startGame();
    }

    void setSettings() {
        Intent intent = getIntent();
        maxCount = intent.getIntExtra(AMOUNT_KEY, 10);
        int modeId = intent.getIntExtra(MODE_KEY, R.id.medium_level);
        if (modeId == 0) {
            mode = Mode.EASY;
        } else {
            mode = modeId == 2 ? Mode.HARD : Mode.MEDIUM;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void startGame() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        window = new Rect();
        Display display = getWindowManager().getDefaultDisplay();
        display.getRectSize(window);
        rectBall = new Rect();
        rectHoop = new Rect();
        goodCountBallsPlayer1 = countBalls = 0;
        ready = true;

        Log.d("WINDOWSIZE", window.toShortString());
        layout.post(new Runnable() {
            @Override
            public void run() {
                setAnimationHoop();
            }
        });

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
    }

    private void setAnimationHoop() {
        if (mode == Mode.EASY) {
            return;
        }
        final ValueAnimator animatorGoRight = ValueAnimator.ofInt(window.left + window.right / 7, window.right - hoop.getWidth() - window.right / 7);
        final ValueAnimator animatorGoLeft = ValueAnimator.ofInt(window.right - hoop.getWidth() - window.right / 7, window.left + window.right / 7);

        animatorGoRight.setInterpolator(new DecelerateInterpolator());
        animatorGoLeft.setInterpolator(new DecelerateInterpolator());

        animatorGoRight.setDuration(mode.duration);
        animatorGoLeft.setDuration(mode.duration);
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                hoop.setX(value);
            }
        };

        animatorGoRight.addUpdateListener(listener);
        animatorGoLeft.addUpdateListener(listener);

        animatorGoRight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorGoLeft.start();
            }
        });

        animatorGoLeft.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorGoRight.start();
            }
        });
        animatorGoRight.start();
    }

    protected void changeScore(boolean increase) {
        if (increase) {
            goodCountBallsPlayer1++;
        }
        String s = goodCountBallsPlayer1 + "/" + countBalls;
        textViewScore.setText(s);
        int progress = countBalls == 0 ? 0 : (int) ((double) goodCountBallsPlayer1 / countBalls * 100);
        rateView.setProgress(progress);
        if (countBalls >= maxCount) {
            Intent data = new Intent();
            data.putExtra(SCORE_KEY, "YourScore is " + s);
            setResult(RESULT_MESSAGE, data);
            finish();
        }
    }

    private void vibrate() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(100);
        }
    }

    GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        private boolean isGoodBall = false;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            if (ready && velocityY < 0) {
                ready = false;
                isGoodBall = false;
                startAnimation(downEvent.getRawX(), Math.max(downEvent.getRawY(), (float) window.bottom / 2), velocityX / 7, -velocityY / 7);
            }
            return true;
        }

        private void startAnimation(final float x0_b, final float y0_b, final float width, final float height) {
            final ValueAnimator animatorX = ValueAnimator.ofFloat(x0_b, x0_b + width);
            final ValueAnimator animatorY1 = ValueAnimator.ofFloat(y0_b, y0_b - height);
            final ValueAnimator animatorY2 = ValueAnimator.ofFloat(y0_b - height, y0_b);

            person.setY(y0_b);
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
                        isGoodBall = true;
                        mediaPlayer.start();
                        animatorX.cancel();
                    }
                }
            });

            animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (Float) (animation.getAnimatedValue());
                    ball.setX(value);

                    if (value > x0_b + width / 2) if (crossWall()) {
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
                    ball.setX((float) window.right / 2);
                    ball.setY((float) window.bottom / 2);
                    ready = true;
                    countBalls++;
                    changeScore(isGoodBall);
                }
            });

            animatorX.start();
            animatorY1.start();
            animatorY2.start();
        }

        private boolean crossWall() {
            return ball.getX() > window.right || ball.getX() < window.left || ball.getY() > window.bottom;
        }

        private boolean inHoop() {
            getRectBall(rectBall);
            getRectHoop(rectHoop);
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
