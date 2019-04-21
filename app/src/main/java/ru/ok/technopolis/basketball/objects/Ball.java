package ru.ok.technopolis.basketball.objects;

import android.view.View;

import ru.ok.technopolis.basketball.controllers.AnimationController;
import ru.ok.technopolis.basketball.BackView;

public class Ball {

    private Direction direction;
    private boolean isThrown;
    private float speedY;
    private float speedX;
    private final View object;
    private final float startPosX;
    private final float startPosY;
    private final float radius;
    private long lastCollisionXTime;
    private long lastCollisionYTime;
    private final AnimationController animationController;
    private boolean scored;

    public Ball(Direction direction, View object) {
        this.direction = direction;
        this.object = object;
        this.startPosX = object.getTranslationX();
        this.startPosY = object.getTranslationY();
        this.radius = object.getHeight() / 2f;
        this.animationController = new AnimationController();
    }

    public boolean isThrown() {
        return isThrown;
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

    public void resetBall() {
        object.setVisibility(View.INVISIBLE);
        isThrown = false;
        object.setTranslationX(startPosX);
        object.setTranslationY(startPosY);
        object.setAlpha(1f);
    }

    public void update(float dY, float dX) {
        object.setTranslationY(dY);
        object.setTranslationX(dX);
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

    public boolean hirLeftWall(long time) {
        return getX() <= 0
                && getDirection() == Ball.Direction.LEFT && getLastCollisionXTime() != time;
    }

    public void rotateRight() {
        animationController.setBallRotation(getObject(),-1);
        animationController.setBallRotation(getObject(),1);
    }

    public void rotateLeft() {
        animationController.setBallRotation(getObject(),1);
        animationController.setBallRotation(getObject(),-1);
    }

    public void fade() {
        animationController.fadeBall(getObject());
    }

    public boolean hitBasket(Basket basket) {
        return getX() > basket.getX() - basket.getRadius() && getX() < basket.getX() + basket.getRadius()
                && getY() > basket.getY() - basket.getRadius() && getY() < basket.getY() + basket.getRadius();
    }

    public void score() {
        scored = true;
    }

    public boolean isScored() {
        return scored;
    }

    public boolean hitLeftBasket(Basket basket, long time) {
        return getX() + getRadius() * 3 >= basket.getX()
                && getY() + getRadius() * 2 >= basket.getY()
                && getY() + getRadius() * 3 <= basket.getY() + basket.getRadius() * 4
                && getDirection() == Ball.Direction.RIGHT && lastCollisionXTime != time;
    }

    public enum Direction {
        RIGHT,
        LEFT
    }
}
