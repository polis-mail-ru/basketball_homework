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
        animateRollOnY = new SpringAnimation(this.ballView, DynamicAnimation.Y);
        animateRollOnX.setSpring(new SpringForce(0).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW));
        animateRollOnY.setSpring(new SpringForce(0).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW));
    }

    @Override
    public void throwing(float velocityX, float velocityY) {
        animateBallOnX.setStartVelocity(velocityX);
        animateBallOnY.setStartVelocity(velocityY);
        animateBallOnX.start();
        animateBallOnY.start();
        animateBallOnX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
                //rollback();
            }
        });
    }

    @Override
    public void rollback() {
        animateRollOnX.start();
        animateRollOnY.start();
    }
}
