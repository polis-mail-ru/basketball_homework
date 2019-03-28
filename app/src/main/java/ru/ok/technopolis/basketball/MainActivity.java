package ru.ok.technopolis.basketball;

import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ball = findViewById(R.id.ball);
    }


    public void flingIt(View view) {
        FlingAnimation flingAnimation_x = new FlingAnimation(ball, DynamicAnimation.X);
        FlingAnimation flingAnimation_y = new FlingAnimation(ball, DynamicAnimation.Y);
        flingAnimation_x.setStartVelocity(500f);
        flingAnimation_y.setStartVelocity(-500f);
        flingAnimation_x.setFriction(0.5f);
        flingAnimation_y.setFriction(0.5f);
        flingAnimation_x.start();
        flingAnimation_y.start();
    }
}
