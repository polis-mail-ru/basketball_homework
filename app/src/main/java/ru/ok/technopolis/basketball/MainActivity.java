package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.support.animation.FlingAnimation;
import android.os.Bundle;
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

    String LOG_TAG = "Main_Activity_logs";
    ImageView ballView;
    int[] ballSideHoopLocation = new int[2];
    int[] leftSideHoopLocation = new int[2];
    int[] rightSideHoopLocation = new int[2];
    private float startX;
    private RelativeLayout mainLayout;
    private double radius;
    boolean doing = false;
    private float startY;
    private View leftSideHoop;
    private View rightSideView;
    private TextView scoreView;

    private int score = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mainLayout = findViewById(R.id.main_activity_layout);
        ballView = findViewById(R.id.main_activity_ball);
        scoreView = findViewById(R.id.main_activity_score_text);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        leftSideHoop = findViewById(R.id.main_activity_left_hoop_view);

        leftSideHoop.getLocationOnScreen(leftSideHoopLocation);
        rightSideView = findViewById(R.id.main_activity_right_hoop_view);

        rightSideView.getLocationInWindow(rightSideHoopLocation);
        Log.d(LOG_TAG, "Start1 " +
        rightSideView.getTop());

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        //Remove the listener before proceeding
                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        // measure your views here
                        rightSideView.getLocationOnScreen(rightSideHoopLocation);
                    }
                }
        );

        Log.d(LOG_TAG, "Start1 " +
                rightSideHoopLocation[0]);

        radius = ballView.getHeight() * 1.414213;
        Rect myViewRect = new Rect();
        leftSideHoop.getGlobalVisibleRect(myViewRect);
        float x = myViewRect.left;
        float y = myViewRect.top;

        ballView.getLocationInWindow(ballSideHoopLocation);

        Log.d(LOG_TAG, "Start " + leftSideHoopLocation[0] + " " + leftSideHoopLocation[1]);
        final GestureDetector gestureDetector = new GestureDetector(this, myGestureListener);


        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }


    private GestureDetector.OnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        private boolean isHit = false;


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!doing) {
                Log.d(LOG_TAG, "Fling");
                doing = true;
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(2000);
                final float dY = Math.abs((e2.getRawY() - e1.getRawY()) / (e2.getEventTime() - e1.getEventTime()));
                final float dX = (e2.getRawX() - e1.getRawX()) / (e2.getEventTime() - e1.getEventTime());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    private float speedX = dX / 3;
                    private float speedY = dY / 3;
                    private long lastCollisionTime = 0;
                    private int collisionCounter = 1;


                    private int closerX = Integer.MAX_VALUE;
                    private int closerY = Integer.MAX_VALUE;


                    {
                        Log.d(LOG_TAG, "start " + speedY + " ");
                    }

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        long time = animation.getCurrentPlayTime();
                        speedY = speedY - 0.0001f * (time - lastCollisionTime);
                        //ballView.setY(ballSideHoopLocation[1] - (speedY) * (time - lastCollisionTime));
                        ballView.setY(870 - (speedY) * (time - lastCollisionTime));
                        ballView.setX(60 + speedX * time);
                        if (ballView.getY() + radius > 900) {
                            if (speedY < 0) {
                                Log.d(LOG_TAG, "direction change " + speedY + " " + ballView.getY());
                                speedY = (float) ((dY / 3) * Math.pow(0.75, collisionCounter));
                                lastCollisionTime = time;
                                collisionCounter++;
                            }

                            if (ballView.getX() + radius + speedX/2 >= leftSideHoopLocation[0]
                                    && ballView.getX() + radius - speedX/2<= rightSideHoopLocation[0]
                                    && ballView.getY() + radius == leftSideHoopLocation[1]) {
                                score++;
                                isHit = true;

                            }
                        }
                    }


                });

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //ballView.setVisibility(View.INVISIBLE);
                        if (isHit) {
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager
                                    .beginTransaction();

                            // добавляем фрагмент
                            WinFragment myFragment = new WinFragment();
                            fragmentTransaction.add(R.id.main_activity_layout, myFragment);
                            fragmentTransaction.commit();

                            isHit = false;
                            scoreView.setText(score);
                        }
                        doing = false;
                    }

                });

                animator.start();
                ballView.animate().start();

            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(LOG_TAG, "Double tap " + ballView.getX() + " " + ballView.getY());
            ballView.setX(60);
            ballView.setY(870);
            return true;
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}
