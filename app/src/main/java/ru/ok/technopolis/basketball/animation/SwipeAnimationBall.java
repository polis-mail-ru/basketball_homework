package ru.ok.technopolis.basketball.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;

public class SwipeAnimationBall implements SwipeAnimation {

    private View ballView, counter;
    private FlingAnimation animateBallOnX;
    private FlingAnimation animateBallOnY;
    private SpringAnimation animateRollOnX;
    private SpringAnimation animateRollOnY;

    public SwipeAnimationBall (View ballView, View counter) {
        this.ballView = ballView;
        this.counter = counter;
        animateBallOnX = new FlingAnimation(this.ballView, DynamicAnimation.X).setFriction(1.5f);
        animateBallOnY = new FlingAnimation(this.ballView, DynamicAnimation.Y).setFriction(3f);
        animateRollOnX = new SpringAnimation(this.ballView, DynamicAnimation.TRANSLATION_X);
        animateRollOnY = new SpringAnimation(this.ballView, DynamicAnimation.TRANSLATION_Y);
    }

    @Override
    public void throwing(float velocityX, float velocityY) {
        animateBallOnX.setStartVelocity(velocityX);
        animateBallOnY.setStartVelocity(velocityY);
        animateBallOnX.start();
        animateBallOnY.start();
        animateBallOnX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean cancelled, float value, float velocity) {
                rollback();
            }
        });
    }

    @Override
    public void rollback() {
        SpringForce springForce = new SpringForce(0);
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);
        animateRollOnX.setSpring(springForce);
        animateRollOnY.setSpring(springForce);
        animateRollOnX.start();
        animateRollOnY.start();
    }
}
