package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private ImageView ball;
    private ImageView point;
    private TextView money;
    private ImageView player;
    private BackView backView;
    private boolean scoredThis = false;
    private boolean scoredLast;
    private float x;
    private float y;
    private boolean walls;
    private boolean vibro;
    private boolean music;
    private boolean threw = false;
    private int playerDirection = 1;
    private Vibrator vibrator;
    private MediaPlayer soundsPlayer;
    public static final String WALL_KEY = "walls";
    public static final String VIBRO_KEY = "vibro";
    public static final String MUSIC_KEY = "music";
    public static final String BALL_KEY = "ball";
    private ValueAnimator animator;
    private StarView scoreView;
    private final Random random = new Random();
    private float startPosY;
    private float startPosX;
    private float startX;
    private final float g = 10;
    private byte state = 0;
    private ObjectAnimator anim;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        x = Float.POSITIVE_INFINITY;
        ball = findViewById(R.id.iv_translate_fling);
        final RelativeLayout mainLayout = findViewById(R.id.main_layout);
        point = findViewById(R.id.empty);
        money = findViewById(R.id.money);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        backView = findViewById(R.id.back);
        scoreView = findViewById(R.id.scoreView);
        player = findViewById(R.id.player);
        getExtra();
        ball.setVisibility(View.VISIBLE);
        startPosY = ball.getTranslationY();
        startPosX = ball.getTranslationX();
        anim = ObjectAnimator.ofFloat(ball, "rotation", 360);
        anim.setDuration(3000);
        final GestureDetector gestureDetector = new GestureDetector(this, gestureListener);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private void getExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            walls = extras.getBoolean(WALL_KEY, true);
            vibro = extras.getBoolean(VIBRO_KEY, false);
            music = extras.getBoolean(MUSIC_KEY, true);
            ball.setImageResource(extras.getInt(BALL_KEY, R.drawable.ball2));
        }
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!threw && state != 1) {
                rotateBall();
                ball.setVisibility(View.VISIBLE);
                threw = true;
                animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(4000);
                final float dY = Math.abs((e2.getRawY() - e1.getRawY()) / (e2.getEventTime() - e1.getEventTime()));
                final float dX = (e2.getRawX() - e1.getRawX()) / (e2.getEventTime() - e1.getEventTime());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    private float speedX = dX / 2;
                    private float speedY = dY / 2;
                    private long lastCollisionYTime = 0;
                    private long lastCollisionXTime = 0;
                    private int collisionCounter = 1;
                    private float[] hitCoords = {ball.getTranslationX(), startPosY};
                    private float lastPosY = ball.getY();
                    private boolean dir = (!(speedX > 0));

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        long time = animation.getCurrentPlayTime();
                        speedY = speedY - 0.0001f * (time - lastCollisionYTime);
                        ball.setTranslationY(hitCoords[1] - (speedY) * (time - lastCollisionYTime));
                        ball.setTranslationX(hitCoords[0] + speedX * (time - lastCollisionXTime));

                        if (ball.getY() + ball.getHeight() * 2 > backView.getHeight()) {
                            if (speedY < 0) {
                                speedY = (float) ((dY / 2) * Math.pow(0.75, collisionCounter));
                                lastCollisionYTime = time;
                                hitCoords[1] = (int) ball.getTranslationY();
                                collisionCounter++;
                            }
                            if (Math.abs(lastPosY - ball.getY()) < 3f && state == 0) {
                                fadeBall();
                            }
                            if (state == 2) {
                                ball.setAlpha(1f);
                                ball.setVisibility(View.INVISIBLE);
                                state = 0;
                                threw = false;
                                animation.cancel();
                                anim.cancel();
                                ball.setTranslationX(startPosX);
                                ball.setTranslationY(startPosY);
                            }
                            lastPosY = ball.getY();
                        }

                        if (ball.getX() + ball.getWidth() >= backView.getWidth()
                                && ball.getX() + ball.getHeight() / 2 < backView.getWidth() + 30 && !dir && lastCollisionXTime != time) {
                            hitCoords[0] = ball.getX();
                            lastCollisionXTime = time;
                            speedX = -speedX / 2;
                            dir = true;
                        } else if (ball.getX() <= 0
                                && dir && lastCollisionXTime != time) {
                            hitCoords[0] = ball.getX();
                            Log.d(TAG, "onAnimationUpdate: ");
                            lastCollisionXTime = time;
                            speedX = -speedX / 2;
                            dir = false;
                        }
                    }
                });
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ball.setAlpha(1f);
                        ball.setVisibility(View.INVISIBLE);
                        state = 0;
                        threw = false;
                        anim.cancel();
                        ball.setTranslationX(startPosX);
                        ball.setTranslationY(startPosY);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
            return false;
        }

    };

    private void rotateBall() {
        anim.start();
    }

    private void fadeBall() {
        state = 1;
        ObjectAnimator anim = ObjectAnimator.ofFloat(ball, "alpha", 0);
        anim.setDuration(1000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                state = 2;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.start();

    }

    private void animateBoy(final float y) {
        player.animate().setDuration(500).translationYBy(-y).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                player.animate().setDuration(500).translationYBy(y).setListener(null).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).start();

    }


    private void initBall() {
        player.setTranslationX(random.nextInt(backView.getWidth() * 2 / 5));
        ball.setTranslationX(startX + (player.getTranslationX() - startPosY));
        startX = ball.getTranslationX();
        Log.d(TAG, "initBall: " + player.getTranslationX());
    }

    private void score() {
        if (vibro && vibrator.hasVibrator()) {
            vibrator.vibrate(100L);
        }
        if (music) {
            soundsPlayer = MediaPlayer.create(this, R.raw.wow);
            soundsPlayer.start();
        }
        scoreView.increase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animator != null) {
            animator.cancel();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (soundsPlayer != null) {
            soundsPlayer.release();
        }
    }
}