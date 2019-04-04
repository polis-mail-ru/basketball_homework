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
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sPref;
    private ScoreComboView scoreComboView;
    private TextView maxComboView;
    private ImageView ballView;
    private ImageView hoopView;

    private int prevMaxCombo;
    private static final String sharedPrefName = "MaxScorePref";
    private static final String maxScore = "MAX_SCORE";

    private boolean throwed = false;
    private boolean scored = false;

    private float x = Float.POSITIVE_INFINITY;
    private float y;
    private float startPositionX;
    private float startPositionY;

    private ValueAnimator animator;
    private ViewPropertyAnimator ball;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreComboView = findViewById(R.id.activity_main__score_combo);
        maxComboView = findViewById(R.id.activity_main__max_combo);
        ballView = findViewById(R.id.activity_main__ball);
        hoopView = findViewById(R.id.activity_main__hoop);
        final ConstraintLayout mainLayout = findViewById(R.id.activity_main__root);

        scoreComboView.setIsIndicator(true);

        final GestureDetector gestureDetector = new GestureDetector(this, gestureListener);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        loadMaxScore();
        scoreComboView.resetScore();
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
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

                animator = ValueAnimator.ofFloat(0, 1);
                animator.setDuration(1000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    final float w = (float) ballView.getWidth() / 2;
                    final float h = (float) ballView.getHeight() / 2;
                    final float hoopWidth = (float) hoopView.getWidth();
                    final float hoopHeight = (float) hoopView.getHeight();

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
                                (x >= hoopView.getX() + 0.3f * hoopWidth && x <= hoopView.getX() + 0.7f * hoopWidth) && !scored) {
                            scoreComboView.incrementScore();
                            if (prevMaxCombo < scoreComboView.getMaxScoreCombo()) {
                                prevMaxCombo = scoreComboView.getMaxScoreCombo();
                                maxComboView.setText(getString(R.string.max_combo, prevMaxCombo));
                            }
                            scored = true;
                        }
                    }
                });

                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ballView.setX(startPositionX);
                        ballView.setY(startPositionY);
                        if (!scored) {
                            scoreComboView.resetScore();
                        }
                        scored = false;
                        throwed = false;
                    }
                });
                ball = ballView.animate();
                animator.start();
                ball.start();
            }
            return true;
        }
    };

    private void loadMaxScore() {
        sPref = getSharedPreferences(sharedPrefName, MODE_PRIVATE);
        prevMaxCombo = sPref.getInt(maxScore, 0);
        scoreComboView.setMaxScoreCombo(prevMaxCombo);
        maxComboView.setText(getString(R.string.max_combo, prevMaxCombo));
    }

    private void saveMaxScore() {
        sPref = getSharedPreferences(sharedPrefName, MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putInt(maxScore, scoreComboView.getMaxScoreCombo());
        ed.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveMaxScore();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animator != null) {
            animator.cancel();
        }
        if (ball != null) {
            ball.cancel();
        }
    }
}
