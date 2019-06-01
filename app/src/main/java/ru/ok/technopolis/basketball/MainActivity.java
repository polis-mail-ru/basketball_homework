package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LifeView.GameEventListener {
    private LifeView lifeView;
    private TextView scoresView;
    private int score = 0;
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

        lifeView = findViewById(R.id.am_custv__lifes);
        ball = findViewById(R.id.am_im__ball);
        hoop = findViewById(R.id.am_im__hoop);
        scoresView = findViewById(R.id.am_tv__scores);

        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View ball, MotionEvent event) {
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
                                .setDuration(50)
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
                                .setDuration(600)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        ball.setY(prevY + dY);
                                        ball.setX(prevX + dX);
                                    }
                                }).start();
                        if (destinationX > hoop.getX() + hoop.getWidth() / 4
                                && destinationX < hoop.getX() + hoop.getWidth() / 2
                                && destinationY < hoop.getY() + hoop.getHeight()
                                && destinationY > hoop.getY() + hoop.getHeight() / 3) {
                            incScores();
                        } else {
                            lifeView.decLife();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void incScores(){
        score++;
        scoresView.setText(String.valueOf(score));
    }

    @Override
    public void endGame() {
        Toast.makeText(this, "Вы проиграли!", Toast.LENGTH_LONG).show();
        score = 0;
        scoresView.setText(String.valueOf(0));
        lifeView.restart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ball.clearAnimation();
    }
}