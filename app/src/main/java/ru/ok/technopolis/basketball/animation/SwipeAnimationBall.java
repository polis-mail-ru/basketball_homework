package ru.ok.technopolis.basketball.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import ru.ok.technopolis.basketball.AnimationContext;
import ru.ok.technopolis.basketball.view.Counter;

public class SwipeAnimationBall implements SwipeAnimation {

    private View ballView;
    private View ballTarget;
    private Counter counter;
    private FlingAnimation animateBallOnX;
    private FlingAnimation animateBallOnY;
    private SpringAnimation animateRollOnX;
    private SpringAnimation animateRollOnY;
    private ViewGroup layout;
    private boolean scored;
    private AnimationContext eventContext;

    public void setEventContext(AnimationContext eventContext) {
        this.eventContext = eventContext;
    }

    @Override
    public void stopAnimation() {
        animateBallOnX.cancel();
        animateBallOnY.cancel();
        animateRollOnX.cancel();
        animateRollOnY.cancel();
    }

    public SwipeAnimationBall(View ballView, Counter counter, View ballTarget, ViewGroup layout) {
        this.ballTarget = ballTarget;
        this.ballView = ballView;
        this.counter = counter;
        this.layout = layout;
        animateBallOnX = new FlingAnimation(this.ballView, DynamicAnimation.X).setFriction(4.5f);
        animateBallOnY = new FlingAnimation(this.ballView, DynamicAnimation.Y).setFriction(8f);
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
        scored = false;
        animateBallOnX.setStartVelocity(velocityX);
        animateBallOnY.setStartVelocity(velocityY);
        animateBallOnX.setMaxValue(layout.getWidth());
        animateBallOnY.setMaxValue(layout.getHeight());
        animateBallOnX.addUpdateListener(updateListener);
        animateBallOnX.start();
        animateBallOnY.start();
        animateBallOnX.addEndListener(endListenerFlingAnim);
    }

    @Override
    public void rollback() {
        animateRollOnX.start();
        animateRollOnY.start();
    }

    private boolean isHitToBasket(float x, float y) {
        float targetLeftPosX = ballTarget.getX();
        float targetRightPosX = ballTarget.getX() + ballTarget.getWidth()*2;
        float targetTopPosY = ballTarget.getY() - ballTarget.getHeight() / 4f;
        float targetBottomPosY = ballTarget.getY() + ballTarget.getHeight()*2;
        Log.d("X", "isHitToBasket: " + x + " " + targetLeftPosX + " " + targetRightPosX);
        return (x >= targetLeftPosX && x <= targetRightPosX) && (y >= targetTopPosY && y <= targetBottomPosY);
    }

    private final DynamicAnimation.OnAnimationUpdateListener updateListener = new DynamicAnimation.OnAnimationUpdateListener() {
        @Override
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float v, float v1) {
            float ballCenterPosX = ballView.getX() + ballView.getWidth() / 2f;
            float ballCenterPosY = ballView.getY() + ballView.getHeight() / 2f;
            if (!scored && isHitToBasket(ballCenterPosX, ballCenterPosY)) {
                counter.increment();
                scored = true;
            }
        }
    };
    private final DynamicAnimation.OnAnimationEndListener endListenerFlingAnim = new DynamicAnimation.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
            rollback();
            eventContext.throwing(false);
        }
    };
}