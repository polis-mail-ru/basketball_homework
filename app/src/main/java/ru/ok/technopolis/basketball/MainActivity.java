package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private StarScoreView scoreView;
    private ImageView hoop;
    private ImageView ball;
    private float dX = 0;
    private float dY = 0;
    private float prevX = 0;
    private float prevY = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreView = findViewById(R.id.activity_main__scores);
        ball = findViewById(R.id.ball);
        hoop = findViewById(R.id.hoop);

        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View ball, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        dX = ball.getX() - event.getRawX();
                        dY = ball.getY() - event.getRawY();
                        prevX = event.getRawX();
                        prevY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ball.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        float destinationX = ball.getX() + (event.getRawX() - prevX) * 4.5f;
                        float destinationY = ball.getY() + (event.getRawY() - prevY) * 3f;
                        ball.setY(prevY + dY);
                        ball.setX(prevX + dX);
                        ball.animate()
                                .x(destinationX)
                                .y(destinationY)
                                .setDuration(500).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        reload();
                                    }
                                }).start();
                        if (destinationX > hoop.getX() + hoop.getWidth() / 4
                                && destinationX < hoop.getX() + hoop.getWidth() / 2
                                && destinationY < hoop.getY() + hoop.getHeight()
                                && destinationY > hoop.getY() + hoop.getHeight() / 3) {
                            scoreView.incrementScore();
                        } else {
                            scoreView.decrementScore();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void reload() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ball.setX(120 * metrics.density);
        ball.setY((metrics.heightPixels - 80) / metrics.density);
    }
}
