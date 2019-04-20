package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView ballImageView;
    private ImageView hoopImageView;
    private ImageView playerImageView;
    private CustomCounterView counterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ballImageView = findViewById(R.id.activity_main_ball);
        hoopImageView = findViewById(R.id.activity_main_hoop);
        playerImageView = findViewById(R.id.activity_main_player);
        counterView = findViewById(R.id.activity_main_counter);
        ImageView reloadImageView = findViewById(R.id.activity_main_reload);
        reloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterView.resetScore();
            }
        });

        ballImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float dx = event.getX();
                    float dy = event.getY();
                    Log.d("dx,dy: ", dx + "----" + dy);
                    startAnimation2(dx, dy);
                }
                return true;
            }
        });
    }

    private void startAnimation2(final float dx, final float dy) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(ballImageView, View.X,
                ballImageView.getX(), ballImageView.getX() - dx * 3);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(ballImageView, View.Y,
                ballImageView.getY(), ballImageView.getY() - dy * 3);
        ObjectAnimator animatorRotation = ObjectAnimator.ofFloat(ballImageView, View.ROTATION, 360);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(1000);
        animatorSet.playTogether(animatorX, animatorY, animatorRotation);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                Log.d("finalX", Float.toString(ballImageView.getX()));
                Log.d("finalY", Float.toString(ballImageView.getY()));
                if (checkHit()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.goal), Toast.LENGTH_SHORT).show();
                    Log.d(getString(R.string.goal), getString(R.string.goal));
                    counterView.incrementScore();
                }
                ballImageView.setX(playerImageView.getX() + 0.6f * (playerImageView.getWidth() - ballImageView.getWidth()));
                ballImageView.setY(playerImageView.getY() - ballImageView.getHeight());

            }
        });
        animatorSet.start();
    }

    private boolean checkHit() {
        float ballX = ballImageView.getX() + ballImageView.getWidth() * 0.5f;
        float ballY = ballImageView.getY() + ballImageView.getHeight() * 0.5f;
        float hoopX1 = hoopImageView.getX() + hoopImageView.getWidth() * 0.4f;
        float hoopX2 = hoopImageView.getX() + hoopImageView.getWidth() * 0.7f;
        float hoopY1 = hoopImageView.getY() + hoopImageView.getHeight() * 0.4f;
        float hoopY2 = hoopImageView.getY() + hoopImageView.getHeight();
        return (ballX > hoopX1 & ballX < hoopX2) & (ballY > hoopY1 & ballY < hoopY2);
    }
}



