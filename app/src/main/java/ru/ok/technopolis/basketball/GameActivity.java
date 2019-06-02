package ru.ok.technopolis.basketball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static java.util.Locale.ENGLISH;

public class GameActivity extends AppCompatActivity {
    private TextView scoreView;
    private ImageView hoopView;
    private ImageView ballView;
    private BallsView ballsView;
    private int score = 0;
    private float dX = 0;
    private float dY = 0;
    private float prevX = 0;
    private float prevY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreView = findViewById(R.id.activity_game__score);
        hoopView = findViewById(R.id.activity_game__hoop);
        ballView = findViewById(R.id.activity_game__ball);
        ballsView = findViewById(R.id.activity_game__balls);
        ballView.setOnTouchListener(new BallOnTouchListener());
        updateScore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ballView.clearAnimation();
    }

    private void finishRound() {
        final String message;
        if (score == 0) {
            message = getString(R.string.result_bad);
        } else if (score < 4) {
            message = getString(R.string.result_good);
        } else {
            message = getString(R.string.result_great);
        }
        final String text = String.format(ENGLISH, "%d/%d. %s", score, BallsView.INIT_BALLS, message);
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        score = 0;
        updateScore();
        ballsView.refresh();
    }

    private void updateScore() {
        final String text = String.format(ENGLISH, "%d/%d", score, ballsView.thrown());
        scoreView.setText(text);
    }

    private class BallOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    onActionDown(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    onActionMove(event);
                    break;
                case MotionEvent.ACTION_UP:
                    onActionUp(event);
                    break;
                default:
                    return false;
            }
            return true;
        }

        private void onActionDown(MotionEvent event) {
            dX = ballView.getX() - event.getRawX();
            dY = ballView.getY() - event.getRawY();
            prevX = event.getRawX();
            prevY = event.getRawY();
        }

        private void onActionMove(MotionEvent event) {
            ballView.animate()
                    .x(event.getRawX() + dX)
                    .y(event.getRawY() + dY)
                    .setDuration(50)
                    .start();
        }

        private void onActionUp(MotionEvent event) {
            float destinationX = ballView.getX() + (event.getRawX() - prevX) * 4.5f;
            float destinationY = ballView.getY() + (event.getRawY() - prevY) * 3f;
            ballView.animate()
                    .x(destinationX)
                    .y(destinationY)
                    .setDuration(300)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            ballView.setY(prevY + dY);
                            ballView.setX(prevX + dX);
                        }
                    }).start();
            if (isScored(destinationX, destinationY)) {
                score++;
            }
            ballsView.decrease();
            updateScore();
            if (ballsView.isEmpty()) {
                finishRound();
            }
        }

        private boolean isScored(float x, float y) {
            return x > hoopView.getX() + hoopView.getWidth() / 4
                    && x < hoopView.getX() + hoopView.getWidth() / 2
                    && y < hoopView.getY() + hoopView.getHeight()
                    && y > hoopView.getY() + hoopView.getHeight() / 3;
        }
    }
}