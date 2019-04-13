package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
    private float lastPos;
    private float startX;

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
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            if (!threw) {
                threw = true;
                ball.setVisibility(View.VISIBLE);
                backView.setVisibility(View.VISIBLE);
                final float distanceInX = moveEvent.getRawX() - downEvent.getRawX();
                final float distanceInY = Math.abs(moveEvent.getRawY() - downEvent.getRawY());
                if (distanceInX < 0 && playerDirection == 1) {
                    player.setImageResource(R.drawable.player2mirror);
                    player.setX(player.getX() - (float) player.getWidth() / 4);
                    playerDirection = 0;
                } else if (distanceInX >= 0 && playerDirection == 0) {
                    player.setImageResource(R.drawable.player2);
                    player.setX(player.getX() + (float) player.getWidth() / 4);
                    playerDirection = 1;
                }
                animateBoy(Math.min(distanceInY, 200));
                animateMotion(distanceInX, distanceInY);
                ball.animate().start();

            }
            return true;
        }
    };

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

    private void animateMotion(final float distanceInX, final float distanceInY) {
        lastPos = player.getTranslationX();
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float w = (float) ball.getWidth() / 2, h = (float) ball.getHeight() / 2;
            int n = 0;
            int direction = -1;
            boolean dirChanged = false;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) (animation.getAnimatedValue());
                if (x == Float.POSITIVE_INFINITY) {
                    x = ball.getX() + w;
                    y = ball.getY() + h;
                }

                ball.setTranslationX(startX + (float) (distanceInX * -Math.cos(value * Math.PI) + distanceInX));
                ball.setTranslationY((float) (distanceInY * -Math.sin(value * Math.PI)));
                //Смена направления, если столкнулись с правой стеной
                if (dirChanged) {
                    ball.setX(backView.getWidth() - (ball.getX() + (float) ball.getWidth() / 2 - backView.getWidth()));
                }
                //Смена направления, если столкнулись с левой стенкой кольца
                if (direction == 2 && !dirChanged) {
                    ball.setX(point.getX() - point.getWidth() - (ball.getX() + (float) ball.getWidth() / 2 - point.getX() + point.getWidth()));
                }

                //отрисовка пути
                if (y != ball.getY() + h && n % 2 == 0) {
                    backView.drawLine(x, y, ball.getX() + w, ball.getY() + h);
                }

                //Проверка на правую стенку
                if (direction != 2 && walls && ball.getX() + ball.getWidth() / 2 >= backView.getWidth() && !dirChanged) {
                    direction *= -1;
                    dirChanged = true;
                    if (vibro && vibrator.hasVibrator()) {
                        vibrator.vibrate(100L);
                    }
                }

                //Проверка на левую стенку
                if ((y >= point.getY() - point.getHeight() / 4 && y <= point.getY() + point.getHeight()) &&
                        (x >= point.getX() - point.getWidth() && x <= point.getX() - point.getWidth() / 2)) {
                    direction = 2;
                }

                //Изменение координат
                x = ball.getX() + w;
                y = ball.getY() + h;

                //Проверка на попадание в кольцо
                if ((y >= point.getY() - point.getHeight() / 4 && y <= point.getY() + point.getHeight() / 4) &&
                        (x >= point.getX() && x <= point.getX() + point.getWidth()) && !scoredThis) {
                    score();
                    if (scoredLast) {
                        money.setText(String.valueOf(Integer.parseInt(money.getText().toString()) + 1));
                    }
                    scoredThis = true;
                }
                //Счётчик для отрисовки не всех линий пути
                n++;
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ball.setVisibility(View.INVISIBLE);
                backView.setVisibility(View.INVISIBLE);
                backView.refresh();
                if (music && !scoredThis) {
                    soundsPlayer = MediaPlayer.create(MainActivity.this, R.raw.oww);
                    soundsPlayer.start();
                }
                scoredLast = scoredThis;
                scoredThis = false;
                threw = false;
                initBall();
            }
        });
        animator.start();
    }

    private void initBall() {
        player.setTranslationX(random.nextInt(backView.getWidth() * 2 / 5));
        ball.setTranslationX(startX + (player.getTranslationX() - lastPos));
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