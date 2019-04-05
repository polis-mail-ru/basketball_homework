package ru.ok.technopolis.basketball.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import ru.ok.technopolis.basketball.EventContext;
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
    private boolean scored;
    private EventContext eventContext;

    public void setEventContext(EventContext eventContext) {
        this.eventContext = eventContext;
    }

    public SwipeAnimationBall(View ballView, Counter counter, View ball_target, ViewGroup mLayout) {
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
        scored = false;
        animateBallOnX.setStartVelocity(velocityX);
        animateBallOnY.setStartVelocity(velocityY);
        animateBallOnX.setMaxValue(mLayout.getWidth());
        animateRollOnY.setMaxValue(mLayout.getHeight());
        animateBallOnX.addUpdateListener(updateListener);
        animateBallOnX.start();
        animateBallOnY.start();
        animateBallOnX.addEndListener(endListener);
    }

    @Override
    public void rollback() {
        animateRollOnX.start();
        animateRollOnY.start();
    }

    private boolean isHitToBasket(float x, float y) {
        float targetLeftPosX = ball_target.getX() - ball_target.getWidth() / 2f;
        float targetRightPosX = ball_target.getX() + ball_target.getWidth() / 2f;
        float targetTopPosY = ball_target.getY() - ball_target.getHeight() / 2f;
        float targetBottomPosY = ball_target.getY() + ball_target.getHeight() / 2f;
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
    private final DynamicAnimation.OnAnimationEndListener endListener = new DynamicAnimation.OnAnimationEndListener() {
        @Override
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
            rollback();
            eventContext.throwing(false);
        }
    };
}