package com.brainlux.basketball.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Stack;

import com.brainlux.basketball.BackView;
import com.brainlux.basketball.Tools.RandomGenerator;

public class Ball {

    private Direction direction;
    private boolean isThrown;
    private float speedY;
    private float speedX;
    private final View object;
    private final float radius;
    private long lastCollisionXTime;
    private long lastCollisionYTime;
    private final AnimationController animationController;
    private boolean scored;
    private boolean fading;
    private ImageView nextBall;
    private boolean collected;

    public Ball(Direction direction, View object) {
        this.direction = direction;
        this.object = object;
        this.radius = object.getHeight() / 2f;
        this.animationController = new AnimationController();
    }

    public boolean isNotThrown() {
        return !isThrown;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public View getObject() {
        return object;
    }

    public Direction getDirection() {
        return direction;
    }

    public long getLastCollisionXTime() {
        return lastCollisionXTime;
    }

    private void setLastCollisionXTime(long lastCollisionXTime) {
        this.lastCollisionXTime = lastCollisionXTime;
    }

    public long getLastCollisionYTime() {
        return lastCollisionYTime;
    }

    public void setLastCollisionYTime(long lastCollisionYTime) {
        this.lastCollisionYTime = lastCollisionYTime;
    }

    public void throwBall(float dY, float dX) {
        isThrown = true;
        speedY = dY;
        speedX = dX;
        object.setVisibility(View.VISIBLE);
        animationController.startBallRotation(getObject());
    }

    private void resetBall() {
        object.setVisibility(View.VISIBLE);
        isThrown = false;
        nextBall.setVisibility(View.VISIBLE);
        nextBall.setX(RandomGenerator.ballPosX());
        nextBall.setTranslationY(RandomGenerator.ballPosY());
    }

    public void update(float dY, float dX) {
        object.setTranslationY(dY);
        object.setX(dX);
    }

    public float getX() {
        return object.getX();
    }

    public float getY() {
        return object.getY();
    }

    public float getRadius() {
        return radius;
    }

    public void changeDirection(long time) {
        speedX = -speedX / 2;
        direction = direction == Direction.RIGHT ? Direction.LEFT : Direction.RIGHT;
        setLastCollisionXTime(time);
        if (direction == Direction.RIGHT) {
            rotateRight();
        } else {
            rotateLeft();
        }
    }

    public boolean hitRightWall(BackView backView, long time) {
        return getX() + getRadius() * 2 >= backView.getWidth()
                && getDirection() == Ball.Direction.RIGHT && lastCollisionXTime != time;
    }

    public boolean hitLeftWall(long time) {
        return getX() <= 0
                && getDirection() == Ball.Direction.LEFT && getLastCollisionXTime() != time;
    }

    public void rotateRight() {
        animationController.setBallRotation(getObject(), -1);
        animationController.setBallRotation(getObject(), 1);
    }

    public void rotateLeft() {
        animationController.setBallRotation(getObject(), 1);
        animationController.setBallRotation(getObject(), -1);
    }

    public void fade(ImageView nextBall) {
        this.nextBall = nextBall;
        animationController.fadeBall(getObject());
        fading = true;
    }

    public boolean hitBasket(Basket basket) {
        return getX() > basket.getX() - basket.getRadius() && getX() < basket.getX() + basket.getRadius()
                && getY() > basket.getY() - basket.getRadius() && getY() < basket.getY() + basket.getRadius();
    }

    public boolean isFading() {
        return fading;
    }

    public void score() {
        scored = true;
    }

    public boolean isScored() {
        return scored;
    }

    public boolean hitBottomBasket(Basket basket, long time) {
        return getX() <= basket.getX()
                && getY() >= basket.getY() -  basket.getRadius() * 3
                && getDirection() == Direction.LEFT && lastCollisionXTime != time;
    }

    public boolean hitTopBasket(Basket basket, long time) {
        return getX() <= basket.getX() - basket.getRadius() * 2
                && getY() <= basket.getY() -  basket.getRadius() * 3
                && getY() > basket.getRadius() * 2
                && getDirection() == Direction.LEFT && lastCollisionXTime != time;
    }

    public boolean hitRightBasket(Basket basket, long time) {
        return getX() - getRadius() * 3 <= basket.getX()
                && getY() + getRadius() * 1.5f >= basket.getY()
                && getY() + getRadius() * 3 <= basket.getY() + basket.getRadius() * 3.5f
                && getDirection() == Direction.LEFT && lastCollisionXTime != time;
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    public boolean hitCoin(Coin coin) {
        return getX() > coin.getX() - coin.getRadius() && getX() < coin.getX() + coin.getRadius()
                && getY() > coin.getY() - coin.getRadius() && getY() < coin.getY() + coin.getRadius();
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public boolean hitWall(Stack<Wall> walls, long time) {
        for (Wall wall : walls) {
            if(getX() <= wall.getX() + wall.getRadius()
                    && getX() >= wall.getX()
                    && getY() <= wall.getY() + wall.getRadius()
                    && getY() >= wall.getY() - wall.getRadius()
                    && getDirection() == Direction.LEFT && lastCollisionXTime != time
                    || getX() >= wall.getX() - wall.getRadius()
                    && getX() <= wall.getX()
                    && getY() <= wall.getY() + wall.getRadius()
                    && getY() >= wall.getY() - wall.getRadius()
                    && getDirection() == Direction.RIGHT && lastCollisionXTime != time)
                return true;

        }
        return false;
    }

    public enum Direction {
        RIGHT,
        LEFT
    }

    public class AnimationController {
        ObjectAnimator anim;
        private int state;
        public static final int BALL_DURATION = 8000;

        AnimationController() {
        }

        public ObjectAnimator getAnim() {
            return anim;
        }

        void setBallRotation(View ball, int i) {
            anim.end();
            anim.removeAllListeners();
            anim = ObjectAnimator.ofFloat(ball, "rotation", i * 360);
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setDuration(1250);
            anim.setInterpolator(new LinearInterpolator());
            anim.start();
        }

        void startBallRotation(View ball) {
            anim = ObjectAnimator.ofFloat(ball, "rotation", 360);
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setDuration(2000);
            anim.setInterpolator(new LinearInterpolator());
            anim.start();
        }

        void fadeBall(final View ball) {
            getAnimationController().setState(1);
            ObjectAnimator anim = ObjectAnimator.ofFloat(ball, "alpha", 0);
            anim.setDuration(1000);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    resetBall();
                    getAnimationController().setState(2);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.start();

        }

        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
