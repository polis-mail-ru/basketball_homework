package ru.ok.technopolis.basketball;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import ru.ok.technopolis.basketball.CustomView.FieldView;
import ru.ok.technopolis.basketball.CustomView.ScoreView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private View ball;
    private FieldView fieldView;
    private ScoreView scoreView;
    private View hoop;
    private View player;
    private VelocityTracker velocityTracker;
    private Point startPoint;
    private MotionOfProjectile motionOfProjectile;
    private float currentX;
    private float currentY;
    private Context context;
    private float G = 9800;
    private float maxDistance;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fieldView = findViewById(R.id.field_view_basketball_field);
        fieldView.setOnTouchListener(this);
        ball = findViewById(R.id.image_ball);
        float density = getResources().getDisplayMetrics().density;
        hoop = findViewById(R.id.image_hoop);
        player = findViewById(R.id.image_player);
        scoreView = findViewById(R.id.score_view_score);
        context = this;
        G = G / density;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                velocityTracker = VelocityTracker.obtain();
                break;
            case MotionEvent.ACTION_UP:
                fieldView.clearField();
                scoreView.resetMark();
                velocityTracker.computeCurrentVelocity(1000);
                calculateInitialCondition(event.getX(), event.getY(), velocityTracker.getXVelocity(), velocityTracker.getYVelocity());
                initialiseAnimation();
                velocityTracker.recycle();
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        ball.setTranslationX(event.getX() - ball.getLeft() - ball.getWidth() / 2);
        ball.setTranslationY(event.getY() - ball.getTop() - ball.getHeight() / 2);
        return true;
    }

    void calculateInitialCondition(float x0, float y0, float vx0, float vy0) {
        startPoint = new Point(x0, y0, vx0, vy0);
        startPoint.changeVerticalAxisTo(fieldView.getHeight());
        motionOfProjectile = new MotionOfProjectile(startPoint, G);
    }

    void initialiseAnimation() {
        float timeStart = 0;
        ValueAnimator animator = ValueAnimator.ofFloat(timeStart, motionOfProjectile.getTimeFlight());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentX = motionOfProjectile.getXFromTime((float) animation.getAnimatedValue());
                currentY = fieldView.getHeight() - motionOfProjectile.getYFromTime((float) animation.getAnimatedValue());
                ball.setX(currentX - ball.getWidth() / 2);
                ball.setY(currentY - ball.getHeight() / 2);
                if (isScored(currentX, currentY)) {
                    Toast.makeText(context, R.string.scored, Toast.LENGTH_SHORT).show();
                    maxDistance = Point.getDistance(player.getWidth(), fieldView.getHeight() - player.getHeight(), currentX, currentY);
                    startPoint.changeVerticalAxisTo(fieldView.getHeight());
                    scoreView.setMark(calculateScore(maxDistance, startPoint.getDistance(currentX, currentY)));

                }
                fieldView.drawDot(currentX, currentY);
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.start();
    }

    private boolean isScored(float x, float y) {
        if (x > (hoop.getLeft() + hoop.getWidth() / 2 - hoop.getWidth() / 6) && x < (hoop.getLeft() + hoop.getWidth() / 2 + hoop.getWidth() / 6)) {
            if (y > (hoop.getTop() + hoop.getHeight() / 2) && y < (hoop.getTop() + hoop.getHeight() / 2 + hoop.getHeight() / 3)) {
                return true;
            }
        }
        return false;
    }

    private int calculateScore(float maxDist, float dist) {
        int score = (int) (dist / (maxDist / 4)) + 1;
        return score > 5 ? 5 : score;
    }
}
