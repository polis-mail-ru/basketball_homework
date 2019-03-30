package ru.ok.technopolis.basketball.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;

public class SwipeAnimationBall implements SwipeAnimation {

    private View view;
    private FlingAnimation animateBallOnX;
    private FlingAnimation animateBallOnY;

    public SwipeAnimationBall (View view) {
        this.view = view;
        animateBallOnX = new FlingAnimation(view, DynamicAnimation.X).setFriction(1.5f);
        animateBallOnY = new FlingAnimation(view, DynamicAnimation.Y).setFriction(3f);
    }

    @Override
    public void throwing(float velocityX, float velocityY) {
        animateBallOnX.setStartVelocity(velocityX);
        animateBallOnY.setStartVelocity(velocityY);
    }

    @Override
    public void rollback() {
        SpringAnimation animateRollOnX = new SpringAnimation(view, DynamicAnimation.TRANSLATION_X);
        SpringAnimation animateRollOnY = new SpringAnimation(view, DynamicAnimation.Y);
        animateRollOnX.setSpring(new SpringForce(0).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW));
        animateRollOnY.setSpring(new SpringForce(0).setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW));
        animateRollOnX.start();
        animateRollOnY.start();
    }
}
