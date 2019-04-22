package ru.ok.technopolis.basketball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView ball;
    private ImageView player;
    private ImageView basket;
    private StarsView score;
    private float dX = 0;
    private float dY = 0;
    private float prevX = 0;
    private float prevY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = findViewById(R.id.player);
        ball = findViewById(R.id.ball);
        ball.setOnTouchListener(new BallOnTouchListener());
        basket = findViewById(R.id.basket);
        score = findViewById(R.id.score);
    }

    public void reload(View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        score.setStarsAmount(0);
        ball.setX(120 * metrics.density);
        ball.setY((metrics.heightPixels - 80) / metrics.density);
        player.setRotation(0);
    }

    private class BallOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - motionEvent.getRawX();
                    dY = view.getY() - motionEvent.getRawY();
                    prevX = motionEvent.getRawX();
                    prevY = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.animate()
                            .x(motionEvent.getRawX() + dX)
                            .y(motionEvent.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    player.animate()
                            .rotationBy(-(float) Math.atan2(motionEvent.getRawY() - prevY, motionEvent.getRawX() - prevX))
                            .setDuration(0)
                            .start();
                    break;
                case MotionEvent.ACTION_UP:
                    float destinationX = view.getX() - (motionEvent.getRawX() - prevX) * 4.5f;
                    float destinationY = view.getY() - (motionEvent.getRawY() - prevY) * 3f;
                    view.animate()
                            .x(destinationX)
                            .y(destinationY)
                            .setDuration(300)
                            .start();
                    player.animate()
                            .rotationBy(60)
                            .setDuration(10)
                            .start();
                    if (destinationX > basket.getX() + basket.getWidth() / 4
                            && destinationX < basket.getX() + basket.getWidth() / 4 * 2
                            && destinationY < basket.getY() + basket.getHeight()
                            && destinationY > basket.getY() + basket.getHeight() / 3) {
                        score.setStarsAmount(5);
                    } else if (destinationX > basket.getX() && destinationY < basket.getY() + basket.getHeight()) {
                        score.setStarsAmount(3);
                    } else {
                        score.setStarsAmount(1);
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
    }
}
