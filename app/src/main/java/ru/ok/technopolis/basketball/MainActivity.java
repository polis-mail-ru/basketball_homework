package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String LOG_TAG = "Main_Activity_logs";
    ImageView ballView;
    int[] ballStartLocation = new int[2];
    int[] leftSideHoopLocation = new int[2];
    int[] rightSideHoopLocation = new int[2];
    private RelativeLayout mainLayout;
    private double radius;
    boolean doing = false;
    private View leftSideHoop;
    private View rightSideHoop;
    private TextView scoreView;
    int width;
    int height;
    double minAccuracy;
    Button stopButton;

    private int score = 0;
    private int rowHit = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        mainLayout = findViewById(R.id.main_activity_layout);
        ballView = findViewById(R.id.main_activity_ball);
        scoreView = findViewById(R.id.main_activity_score_text);
        scoreView.setText("0");
        leftSideHoop = findViewById(R.id.main_activity_left_hoop_view);
        rightSideHoop = findViewById(R.id.main_activity_right_hoop_view);
        stopButton = findViewById(R.id.main_activity_stop_button);
        mainLayout.post(() -> {
            leftSideHoop.getLocationOnScreen(leftSideHoopLocation);
            rightSideHoop.getLocationOnScreen(rightSideHoopLocation);
            ballView.getLocationOnScreen(ballStartLocation);
            ballView.setX(ballStartLocation[0]);
            ballView.setY(ballStartLocation[1]);
            ballView.setVisibility(View.VISIBLE);
            minAccuracy = Math.sqrt(Math.pow(ballStartLocation[0] - leftSideHoopLocation[0], 2)
                    + Math.pow(ballStartLocation[1] - leftSideHoopLocation[1], 2));

        });
        radius = ballView.getHeight() * 1.414213;
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });


        final GestureDetector gestureDetector = new GestureDetector(this, myGestureListener);


        mainLayout.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        stopButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            // добавляем фрагмент
            MenuFragment myFragment = new MenuFragment();
            fragmentTransaction.replace(R.id.main_activity_layout, myFragment).addToBackStack("MenuFragment");
            fragmentTransaction.commit();
        });

    }


    private GestureDetector.OnGestureListener myGestureListener = new GestureDetector.SimpleOnGestureListener() {

        ValueAnimator animator;

        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        private boolean isHit = false;


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!doing) {

                final double[] closer = {Double.MAX_VALUE, Double.MAX_VALUE};
                final double[] tmpAccuracy = {0};

                doing = true;
                animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(4000);
                final float dY = Math.abs((e2.getRawY() - e1.getRawY()) / (e2.getEventTime() - e1.getEventTime()));
                final float dX = (e2.getRawX() - e1.getRawX()) / (e2.getEventTime() - e1.getEventTime());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    private float speedX = dX / 3;
                    private float speedY = dY / 3;
                    private long lastCollisionYTime = 0;
                    private long lastCollisionXTime = 0;
                    private int collisionCounter = 1;
                    private int [] hitCoords = {ballStartLocation[0], ballStartLocation[1]};




                    {
                        Log.d(LOG_TAG, "start " + speedY + " ");
                    }


                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        long time = animation.getCurrentPlayTime();
                        speedY = speedY - 0.0001f * (time - lastCollisionYTime);
                        ballView.setY(hitCoords[1] - (speedY) * (time - lastCollisionYTime));
                        ballView.setX(hitCoords[0] + speedX * (time-lastCollisionXTime));

                        if (ballView.getX() + radius >= leftSideHoopLocation[0]
                                && ballView.getX() + radius <= rightSideHoopLocation[0]
                                && ballView.getY() + radius < leftSideHoopLocation[1]
                                && ballView.getY() + radius > leftSideHoopLocation[1] - 100
                                && speedY < 1) {
                            Log.d(LOG_TAG, "speedY = " + speedY);
                            if (!isHit) {
                                score++;
                                isHit = true;
                                Log.d(LOG_TAG, "Hit");
                            }
                        }
                        if (ballView.getY() + radius > ballStartLocation[1]) {
                            if (speedY < 0) {
                                Log.d(LOG_TAG, "direction change " + speedY + " " + ballView.getY());
                                speedY = (float) ((dY / 3) * Math.pow(0.75, collisionCounter));
                                lastCollisionYTime = time;
                                collisionCounter++;
                            }
                        }

                        if(ballView.getX() + radius >= rightSideHoopLocation[0]-30
                                && ballView.getX() + radius  < rightSideHoopLocation[0] + 30
                                && ballView.getY() > rightSideHoopLocation[1] - rightSideHoop.getHeight()/2-10
                                && ballView.getY() < rightSideHoopLocation[1] + rightSideHoop.getHeight()/2+10){
                            hitCoords[0] = (int)ballView.getX();
                            lastCollisionXTime = time;
                            speedX = -speedX;
                        }

                        //TODO max close
                        //if()

                        if(Math.sqrt(Math.pow(ballView.getX() - (leftSideHoopLocation[0]/2 + rightSideHoopLocation[0]/2 ), 2)
                                + Math.pow(ballView.getY() - leftSideHoopLocation[1], 2))
                                < Math.sqrt(Math.pow(closer[0] - leftSideHoopLocation[0], 2)
                                + Math.pow(closer[1] - leftSideHoopLocation[1], 2))){
                            closer[0] = ballView.getX();
                            closer[1] = ballView.getY();
                        }


                        if(ballView.getX() > width*1.5 || ballView.getX() < -width*0.5){
                            animator.cancel();
                        }


                    }


                });

                animator.addListener(new AnimatorListenerAdapter() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //ballView.setVisibility(View.INVISIBLE);

                        if (isHit) {
                            animFragment();
                            isHit = false;
                            scoreView.setText(score + "");
                            rowHit++;
                            if(rowHit > 0){
                                //TODO: reverse ball
                                //ballView.setMinimumHeight((int)(ballView.getHeight()*2));
                                //ballView.setImageResource(R.drawable.fire_ball);
                                tmpAccuracy[0] = 0.9999;
                            }
                        } else{
                            rowHit = 0;
                            ballView.setImageResource(R.drawable.new_ball);
                            tmpAccuracy[0] = Math.sqrt(Math.pow(closer[0] - leftSideHoopLocation[0], 2)
                                    + Math.pow(closer[1] - leftSideHoopLocation[1], 2));
                            Log.d(LOG_TAG, "accuracys " + tmpAccuracy[0] + " " + minAccuracy + " clos " + closer[0] + " " + closer[1]);
                            tmpAccuracy[0] = 1 - tmpAccuracy[0]/minAccuracy;
                            if(tmpAccuracy[0] <= 0){
                                tmpAccuracy[0] = 0.01;

                            }
                        }
                        ballView.setX(ballStartLocation[0]);
                        ballView.setY(ballStartLocation[1]);
                        doing = false;
                        AccuracyResource.addElement(tmpAccuracy[0]);
                        Log.d(LOG_TAG, "accuracy " + tmpAccuracy[0]);
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
            if(animator != null){
                animator.cancel();
            }
            return true;
        }

        void animFragment() {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            // добавляем фрагмент
            WinFragment myFragment = new WinFragment();
            fragmentTransaction.add(R.id.main_activity_layout, myFragment);
            fragmentTransaction.commit();
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
