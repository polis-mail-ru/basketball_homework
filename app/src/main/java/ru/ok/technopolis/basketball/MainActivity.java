package ru.ok.technopolis.basketball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView ball;
    private StarView stars;
    private ImageView hoop;
    private float dx = 0;
    private float dy = 0;
    private float x = 0;
    private float y = 0;
    private int starsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ball = findViewById(R.id.ball);
        ball.setOnTouchListener(new BallOnTouchListener());
        stars = findViewById(R.id.stars);
        hoop = findViewById(R.id.hoop);
        starsCount = 0;
        stars.setCount(starsCount);

    }

    private class BallOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dx = view.getX() - motionEvent.getRawX();
                    dy = view.getY() - motionEvent.getRawY();
                    x = motionEvent.getRawX();
                    y = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(motionEvent.getRawX() + dx)
                            .y(motionEvent.getRawY() + dy)
                            .setDuration(0)
                            .start();
                    break;
                case MotionEvent.ACTION_UP:
                    float finalX = view.getX() - (motionEvent.getRawX() - x) * 12f;
                    float finalY = view.getY() - (motionEvent.getRawY() - y) * 8f;
                    view.animate()
                            .x(finalX)
                            .y(finalY)
                            .setDuration(400)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    ball.setY(y + dy);
                                    ball.setX(x + dx);
                                }
                            }).start();
                    if (finalX > hoop.getX() && finalY < hoop.getY() + hoop.getHeight()
                            && finalX < hoop.getX() + hoop.getWidth() && finalY > 0) {
                        starsCount += 1;
                        stars.setCount(starsCount);
                        if (starsCount > StarView.MAX_STAR_COUNT) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    getString(R.string.win), Toast.LENGTH_SHORT);
                            toast.show();
                            restart();
                        }
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    private void restart() {
        starsCount = 0;
        stars.setCount(starsCount);
    }
}
