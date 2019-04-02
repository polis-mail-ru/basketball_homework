package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sPref;
    private ScoreComboView scoreComboView;
    private TextView maxComboView;
    private ImageView ballView;
    private ImageView hoopView;

    private int prevMaxCombo;
    private String maxComboStr;

    private boolean throwed = false;
    private boolean scoredThis = false;

    private float x = Float.POSITIVE_INFINITY;
    private float y;
    private float startPositionX;
    private float startPositionY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreComboView = findViewById(R.id.activity_main__score_combo);
        maxComboView = findViewById(R.id.activity_main__max_combo);
        ballView = findViewById(R.id.activity_main__ball);
        hoopView = findViewById(R.id.activity_main__hoop);
        final ConstraintLayout mMainLayout = findViewById(R.id.activity_main);

        scoreComboView.setIsIndicator(true);

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
        loadMaxScore();
        scoreComboView.resetScore();
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent arg0) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
            if (!throwed) {
                startPositionX = ballView.getX();
                startPositionY = ballView.getY();
                throwed = true;
                final float distanceInX = moveEvent.getRawX() - downEvent.getRawX();
                final float distanceInY = Math.abs(moveEvent.getRawY() - downEvent.getRawY());

                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    float w = (float) ballView.getWidth() / 2;
                    float h = (float) ballView.getHeight() / 2;
                    float hoopWidth = (float) hoopView.getWidth();
                    float hoopHeight = (float) hoopView.getHeight();

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (x == Float.POSITIVE_INFINITY) {
                            x = ballView.getX() + w;
                            y = ballView.getY() + h;
                        }
                        float value = (Float) (animation.getAnimatedValue());
                        ballView.setTranslationX((float) (distanceInX * -Math.cos(value * Math.PI) + distanceInX));
                        ballView.setTranslationY((float) (distanceInY * -Math.sin(value * Math.PI)));
                        x = ballView.getX() + w;
                        y = ballView.getY() + h;
                        if ((y >= hoopView.getY() + 0.37f * hoopHeight && y <= hoopView.getY() + 0.8f * hoopHeight) &&
                                (x >= hoopView.getX() + 0.3f * hoopWidth && x <= hoopView.getX() + 0.7f * hoopWidth) && !scoredThis) {
                            scoreComboView.incrementScore();
                            if (prevMaxCombo < scoreComboView.getMaxScoreCombo()) {
                                prevMaxCombo = scoreComboView.getMaxScoreCombo();
                                maxComboStr = getString(R.string.max_combo) + " " + prevMaxCombo;
                                maxComboView.setText(maxComboStr);
                            }
                            scoredThis = true;
                        }
                    }
                });

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ballView.setX(startPositionX);
                        ballView.setY(startPositionY);
                        if (!scoredThis) {
                            scoreComboView.resetScore();
                        }
                        scoredThis = false;
                        throwed = false;
                    }
                });
                animator.start();
                ballView.animate().start();
            }
            return true;
        }
    };

    private void loadMaxScore() {
        sPref = getSharedPreferences("MaxScorePref",MODE_PRIVATE);
        String savedMaxScore = sPref.getString("MAX_SCORE", "0");
        prevMaxCombo = Integer.valueOf(savedMaxScore);
        scoreComboView.setMaxScoreCombo(prevMaxCombo);
        maxComboStr = getString(R.string.max_combo)+" "+savedMaxScore;
        maxComboView.setText(maxComboStr);
    }

    private void saveMaxScore() {
        sPref = getSharedPreferences("MaxScorePref", MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString("MAX_SCORE", String.valueOf(scoreComboView.getMaxScoreCombo()));
        ed.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveMaxScore();
    }
}
