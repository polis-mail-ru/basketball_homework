package ru.ok.technopolis.basketball;

import android.animation.Animator;
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

import ru.ok.technopolis.basketball.controllers.AnimationController;
import ru.ok.technopolis.basketball.controllers.ScoreController;
import ru.ok.technopolis.basketball.objects.Ball;
import ru.ok.technopolis.basketball.objects.Basket;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
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
    private Vibrator vibrator;
    private MediaPlayer soundsPlayer;
    public static final String WALL_KEY = "walls";
    public static final String VIBRO_KEY = "vibro";
    public static final String MUSIC_KEY = "music";
    public static final String BALL_KEY = "ballView";
    private ValueAnimator animator;
    private StarView scoreView;
    private final Random random = new Random();
    private final float g = 10;
    private Ball ball;
    private Basket basket;
    private AnimationController animationController;
    private ScoreController scoreController;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        x = Float.POSITIVE_INFINITY;
        final RelativeLayout mainLayout = findViewById(R.id.main_layout);
        money = findViewById(R.id.money);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        backView = findViewById(R.id.back);
        scoreView = findViewById(R.id.scoreView);
        player = findViewById(R.id.player);
        getExtra();
        basket = new Basket((ImageView) findViewById(R.id.empty));
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
            //ballView.setImageResource(extras.getInt(BALL_KEY, R.drawable.ball2));
        }
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (ball == null || (!ball.isThrown() && AnimationController.state != 1)) {
                animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(AnimationController.BALL_DURATION);
                final float dY = Math.abs((e2.getRawY() - e1.getRawY()) / (e2.getEventTime() - e1.getEventTime()));
                final float dX = (e2.getRawX() - e1.getRawX()) / (e2.getEventTime() - e1.getEventTime());
                if (ball == null) {
                    ball = new Ball(dX > 0 ? Ball.Direction.RIGHT : Ball.Direction.LEFT, (ImageView) findViewById(R.id.iv_translate_fling));
                    setUpControllers();
                } else {
                    ball = new Ball(dX > 0 ? Ball.Direction.RIGHT : Ball.Direction.LEFT, ball.getObject());
                }
                ball.throwBall(dY / 2, dX / 2);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    private int collisionCounter = 1;
                    private float[] hitCoords = {ball.getObject().getTranslationX(), ball.getObject().getTranslationY()};
                    private float lastPosY = ball.getY();

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        long time = animation.getCurrentPlayTime();
                        ball.setSpeedY(ball.getSpeedY() - 0.000075f * (time - ball.getLastCollisionYTime()));
                        ball.update(hitCoords[1] - (ball.getSpeedY()) * (time - ball.getLastCollisionYTime()),
                                hitCoords[0] + ball.getSpeedX() * (time - ball.getLastCollisionXTime()));
                        if (ball.getY() + ball.getRadius() * 4 > backView.getHeight()) {
                            if (ball.getSpeedY() < 0) {
                                ball.setSpeedY((float) ((dY / 3 * 2) * Math.pow(0.75, collisionCounter)));
                                ball.setLastCollisionYTime(time);
                                hitCoords[1] = ball.getObject().getTranslationY();
                                collisionCounter++;
                                if (collisionCounter == 2) { // ball hits ground
                                    if (ball.getDirection() == Ball.Direction.RIGHT) {
                                        ball.rotateRight();
                                    } else {
                                        ball.rotateLeft();
                                    }
                                }
                            }
                            if (Math.abs(lastPosY - ball.getY()) < 3f && AnimationController.state == 0) { //fade ball
                                ball.fade();
                            }
                            if (AnimationController.state == 2) {
                                AnimationController.state = 0;
                                animation.cancel();
                            }
                            lastPosY = ball.getY();
                        }
                        if (ball.hitBasket(basket) && !ball.isScored()) {
                            scoreController.score();
                            ball.score();
                        }
                        if (ball.hitLeftBasket(basket, time)) {
                            hitCoords[0] = ball.getX();
                            ball.changeDirection(time);
                        } else if (ball.hitRightWall(backView, time)) {
                            hitCoords[0] = ball.getX();
                            ball.changeDirection(time);
                        } else if (ball.hirLeftWall(time)) {
                            hitCoords[0] = ball.getX();
                            ball.changeDirection(time);
                        }
                    }
                });
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        AnimationController.state = 0;
                        ball.resetBall();
                        AnimationController.anim.cancel();
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

    private void setUpControllers() {
        animationController = new AnimationController(ball.getObject());
        scoreController = new ScoreController((StarView) findViewById(R.id.scoreView));
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