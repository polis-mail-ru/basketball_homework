package ru.ok.technopolis.basketball.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import ru.ok.technopolis.basketball.view.Counter;

public class SwipeAnimationBall implements SwipeAnimation {

    private View ballView;
    private View ball_target;
    private Counter counter;
    private FlingAnimation animateBallOnX;
    private FlingAnimation animateBallOnY;
    private SpringAnimation animateRollOnX;
    private SpringAnimation animateRollOnY;
    private ViewGroup mLayout;

    public SwipeAnimationBall (View ballView, Counter counter, View ball_target, ViewGroup mLayout) {
        this.ball_target = ball_target;
        this.ballView = ballView;
        this.counter = counter;
        this.mLayout = mLayout;
        animateBallOnX = new FlingAnimation(this.ballView, DynamicAnimation.X).setFriction(2.5f);
        animateBallOnY = new FlingAnimation(this.ballView, DynamicAnimation.Y).setFriction(5f);
        animateRollOnX = new SpringAnimation(this.ballView, DynamicAnimation.TRANSLATION_X);
        animateRollOnY = new SpringAnimation(this.ballView, DynamicAnimation.TRANSLATION_Y);
        SpringForce springForce = new SpringForce(0);
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);
        animateRollOnX.setSpring(springForce);
        animateRollOnY.setSpring(springForce);
    }

    @Override
    public void throwing(float velocityX, float velocityY) {
        animateBallOnX.setStartVelocity(velocityX);
        animateBallOnY.setStartVelocity(velocityY);
        animateBallOnX.setMaxValue(mLayout.getWidth());
        animateRollOnY.setMaxValue(mLayout.getHeight());
        animateBallOnX.start();
        animateBallOnY.start();
        animateBallOnX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean cancelled, float value, float velocity) {
                float ballCenterPosX = ballView.getX() + ballView.getWidth() / 2f;
                float ballCenterPosY = ballView.getY() + ballView.getHeight() / 2f;
                Log.d("x", String.valueOf(ballCenterPosX));
                Log.d("y", String.valueOf(ballCenterPosY));
                if(isHitToBasket(ballCenterPosX, ballCenterPosY)) {
                    counter.increment();
                }
                rollback();
            }
        });
    }

    @Override
    public void rollback() {
        animateRollOnX.start();
        animateRollOnY.start();
    }

    private boolean isHitToBasket(float x, float y) {
        
        /*int upperLeftPosOfTarget [] = new int[2];
        ball_target.getLocationOnScreen(upperLeftPosOfTarget);
        //get upper right pos
        int upperRightPosOfTarget [] = {upperLeftPosOfTarget[0] + ball_target.getWidth(), upperLeftPosOfTarget[1]};
        //get lower left pos
        int lowerLeftPosOfTarget [] = {upperLeftPosOfTarget[0], upperLeftPosOfTarget[1] - ball_target.getHeight()};
        // get lower right pos
        int lowerRightPosOfTarget [] = {lowerLeftPosOfTarget[0] + ball_target.getWidth(), upperRightPosOfTarget[1] - ball_target.getHeight()};

        return x >= upperLeftPosOfTarget[0] && x <= upperRightPosOfTarget[0] && y >= lowerRightPosOfTarget[1] && y <= upperRightPosOfTarget[1];*/

        float targetCenterPosX = ball_target.getX() + ball_target.getWidth() / 2f;
        float targetCenterPosY = ball_target.getY() + ball_target.getHeight() / 2f;

        return x == targetCenterPosX && y == targetCenterPosY;
    }
}
