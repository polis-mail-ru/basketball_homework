package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView mViewTobeFlung;
    private ImageView point;
    private ImageView player;
    private TextView money;
    private int score = 0;
    private BackView backView;
    boolean scoredThis = false;
    boolean scoredLast;
    int inRow = 0;
    float x = Float.POSITIVE_INFINITY, y;
    boolean walls;
    boolean vibro;
    boolean music;
    boolean threw = false;
    int playerDirection = 1;
    Vibrator vibrator;
    MediaPlayer mPlayer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mViewTobeFlung = findViewById(R.id.iv_translate_fling);
        final RelativeLayout mMainLayout = findViewById(R.id.main_layout);
        point = findViewById(R.id.empty);
        money = findViewById(R.id.money);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        backView = findViewById(R.id.back);
        player = findViewById(R.id.player);
        getExtra();
        final GestureDetector gestureDetector = new GestureDetector(this, mGestureListener);

        mMainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mMainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });


    }

    private void getExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            walls = extras.getBoolean("walls", true);
            vibro = extras.getBoolean("vibro", false);
            music = extras.getBoolean("music", true);
            mViewTobeFlung.setImageResource(extras.getInt("ball", R.drawable.ball2));
        }
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            if (!threw) {
                threw = true;
                mViewTobeFlung.setVisibility(View.VISIBLE);
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
                animateBoy(Math.min(distanceInY, 500));
                final Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(10);
                final Bitmap bitmap = Bitmap.createBitmap(backView.getWidth(), backView.getHeight(), Bitmap.Config.ARGB_8888);
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    float w = (float) mViewTobeFlung.getWidth() / 2, h = (float) mViewTobeFlung.getHeight() / 2;
                    Canvas canvas = new Canvas(bitmap);
                    int n = 0;
                    int direction = -1;
                    boolean dirChanged = false;

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (x == Float.POSITIVE_INFINITY) {
                            x = mViewTobeFlung.getX() + w;
                            y = mViewTobeFlung.getY() + h;
                        }
                        float value = (Float) (animation.getAnimatedValue());
                        mViewTobeFlung.setTranslationX((float) (distanceInX * -Math.cos(value * Math.PI) + distanceInX));
                        if (dirChanged) {
                            mViewTobeFlung.setX(backView.getWidth() - (mViewTobeFlung.getX() + (float) mViewTobeFlung.getWidth() / 2 - backView.getWidth()));
                        }
                        if (direction == 2 && !dirChanged) {
                            mViewTobeFlung.setX(point.getX() - point.getWidth() - (mViewTobeFlung.getX() + (float) mViewTobeFlung.getWidth() / 2 - point.getX() + point.getWidth()));
                        }
                        mViewTobeFlung.setTranslationY((float) (distanceInY * -Math.sin(value * Math.PI)));
                        if (y != mViewTobeFlung.getY() + h && n % 2 == 0) {
                            canvas.drawLine(x, y, mViewTobeFlung.getX() + w, mViewTobeFlung.getY() + h, paint);
                        }
                        if (direction != 2 && walls && mViewTobeFlung.getX() + mViewTobeFlung.getWidth() / 2 >= backView.getWidth() && !dirChanged) {
                            direction *= -1;
                            dirChanged = true;
                            if (vibro && vibrator.hasVibrator()) {
                                vibrator.vibrate(100L);
                            }
                        }
                        n++;
                        if ((y >= point.getY() - point.getHeight() / 4 && y <= point.getY() + point.getHeight()) && (x >= point.getX() - point.getWidth() && x <= point.getX() - point.getWidth() / 2))
                            direction = 2;
                        x = mViewTobeFlung.getX() + w;
                        y = mViewTobeFlung.getY() + h;

                        if ((y >= point.getY() - point.getHeight() / 4 && y <= point.getY() + point.getHeight() / 4) && (x >= point.getX() - point.getWidth() / 4 && x <= point.getX() + point.getWidth() / 2) && !scoredThis) {
                            score();
                            if (scoredLast) {
                                inRow++;
                                money.setText(String.valueOf(Integer.parseInt(money.getText().toString()) + 1));
                            }
                            scoredThis = true;
                        }
                    }
                });

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mViewTobeFlung.setVisibility(View.INVISIBLE);
                        backView.setVisibility(View.INVISIBLE);
                        if (!scoredThis)
                            inRow = 0;
                        if (music && !scoredThis) {
                            mPlayer = MediaPlayer.create(MainActivity.this, R.raw.oww);
                            mPlayer.start();
                        }
                        scoredLast = scoredThis;
                        scoredThis = false;
                        threw = false;
                    }

                });

                animator.start();
                mViewTobeFlung.animate().start();
                backView.drawLine(bitmap);
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

    private void score() {
        if (vibro && vibrator.hasVibrator()) {
            vibrator.vibrate(100L);
        }
        if (music) {
            mPlayer = MediaPlayer.create(this, R.raw.wow);
            mPlayer.start();
        }
        score++;
        Log.d("", "score: SCORED");
        CustomView view = findViewById(R.id.view);
        view.initStar(view.getWidth(), view.getHeight());
        view.drawStar(score);

    }
}