package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import ru.ok.technopolis.basketball.views.TallyCounter;

public class MainActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 60_000;

    private TextView timerView;
    private Button startGameBtn;
    private ImageView ballView;
    private TallyCounter scoreView;
    private TextView bestScoreView;

    private CountDownTimer timer;
    private boolean timerIsRunning;
    private boolean timerIsPending;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;

    private float[] target = {477, 515, 67, 85};
    private boolean wasHit;
    private int bestScore;
    private float ballStartPosX;
    private float ballStartPosY;

    private FlingAnimation flingAnimationX;
    private FlingAnimation flingAnimationY;
    private FlingAnimation dropInBasket;
    private FlingAnimation rotateBall;
    private SpringAnimation rollbackX;
    private SpringAnimation rollbackY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        convertDpToPixels(target);

        timerView = findViewById(R.id.activity_main__timer);
        startGameBtn = findViewById(R.id.activity_main__start_game);
        ballView = findViewById(R.id.activity_main__ball);
        scoreView = findViewById(R.id.activity_main__score_counter);
        bestScoreView = findViewById(R.id.activity_main__best_score);

        setBallAnimationThrowing();
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateBall = new FlingAnimation(ballView, DynamicAnimation.ROTATION)
                        .setFriction(1.1f)
                        .setStartVelocity(10_000f);
                rotateBall.start();
                startGameBtn.setVisibility(View.GONE);
                timerView.setVisibility(View.VISIBLE);
                scoreView.reset();
                startTimer();
            }
        });
        bestScoreView.setText(getString(R.string.best, 0));
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timerIsRunning = false;
        timerIsPending = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timerIsPending) {
            startTimer();
            timerIsPending = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelAllAnimations(flingAnimationX, flingAnimationY, dropInBasket,
                rotateBall, rollbackX, rollbackY);
        ballView.setX(ballStartPosX);
        ballView.setY(ballStartPosY);
    }

    private void cancelAllAnimations(DynamicAnimation... animations) {
        for (DynamicAnimation animation: animations) {
            if (animation != null && animation.isRunning()) {
                animation.cancel();
            }
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerView();
            }

            @Override
            public void onFinish() {
                bestScore = Math.max(bestScore, scoreView.getCount());
                if (bestScore == scoreView.getCount()) {
                    bestScoreView.setText(getString(R.string.best, bestScore));
                }
                timerIsRunning = false;
                timeLeftInMillis = START_TIME_IN_MILLIS;
                timerView.setVisibility(View.GONE);
                startGameBtn.setVisibility(View.VISIBLE);
            }
        }.start();

        timerIsRunning = true;
    }

    private void updateTimerView() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerView.setText(timeLeftFormatted);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setBallAnimationThrowing() {
        flingAnimationX = new FlingAnimation(ballView, DynamicAnimation.X)
                .setFriction(2f);
        flingAnimationY = new FlingAnimation(ballView, DynamicAnimation.Y)
                .setFriction(5f);

        flingAnimationX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation,
                                       boolean canceled, float value,
                                       float velocity)
            {
                float ballCenterPosX = ballView.getX() + ballView.getWidth() / 2f;
                float ballCenterPosY = ballView.getY() + ballView.getHeight() / 2f;
                if (estimateThrow(ballCenterPosX, ballCenterPosY)) {
                    if (wasHit) {
                        scoreView.setCount(scoreView.getCount() * 2);
                    } else {
                        scoreView.increment();
                        wasHit = true;
                    }
                    animateHit();
                } else {
                    wasHit = false;
                    rollbackOfThrowing();
                }

            }
        });

        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                flingAnimationX.setStartVelocity(velocityX);
                flingAnimationY.setStartVelocity(velocityY);

                flingAnimationX.start();
                flingAnimationY.start();

                return true;
            }
        });

        ballView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (timerIsRunning) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
                return false;
            }
        });

        ballView.post(new Runnable() {
            @Override
            public void run() {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                ballStartPosX = ballView.getX();
                ballStartPosY = ballView.getY();
                flingAnimationX.setMinValue(0f).setMaxValue((float) (size.x - ballView.getWidth()));
                flingAnimationY.setMinValue(0f).setMaxValue((float) (size.y - ballView.getHeight()));
            }
        });
    }

    private void animateHit(){
        dropInBasket = new FlingAnimation(ballView, DynamicAnimation.TRANSLATION_Y)
                .setFriction(1.5f)
                .setStartVelocity(5000f);
        dropInBasket.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation,
                                       boolean canceled, float value,
                                       float velocity)
            {
                rollbackOfThrowing();
            }
        });
        dropInBasket.start();
    }

    private void rollbackOfThrowing() {
        rollbackX = new SpringAnimation(ballView,DynamicAnimation.TRANSLATION_X);
        SpringForce forceX = new SpringForce(0);
        forceX.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        forceX.setStiffness(SpringForce.STIFFNESS_LOW);
        rollbackX.setSpring(forceX);

        rollbackY = new SpringAnimation(ballView,DynamicAnimation.TRANSLATION_Y);
        SpringForce forceY = new SpringForce(0);
        forceY.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        forceY.setStiffness(SpringForce.STIFFNESS_LOW);
        rollbackY.setSpring(forceX);

        rollbackX.start();
        rollbackY.start();
    }

    private boolean estimateThrow(float x, float y) {
        return x >= target[0] && x <= target[1] && y >= target[2] && y <= target[3];
    }

    public void convertDpToPixels(float[] dp) {
        for (int i = 0; i < dp.length; i++) {
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp[i],
                    this.getResources().getDisplayMetrics());
            dp[i] = px;
        }
    }
}