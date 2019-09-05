package ru.ok.technopolis.basketball.controllers;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ru.ok.technopolis.basketball.R;
import ru.ok.technopolis.basketball.objects.Ball;
import ru.ok.technopolis.basketball.objects.Basket;
import ru.ok.technopolis.basketball.objects.Game;

public class BallController {

    private final Ball ball;
    private final ImageView nextBall;

    public BallController(Ball ball, ImageView nextBall) {
        this.ball = ball;
        this.nextBall = nextBall;
    }

    public void throwBall(final Context context, final float dY, final Basket basket, final RelativeLayout layout) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(Ball.AnimationController.BALL_DURATION);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private int collisionCounter = 1;
            private float[] hitCoords = {ball.getObject().getX(), ball.getObject().getTranslationY()};
            private float lastPosY = ball.getY();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d("", "onAnimationUpdate: "+hitCoords[0]);
                long time = animation.getCurrentPlayTime();
                ball.setSpeedY(ball.getSpeedY() - 0.000075f * (time - ball.getLastCollisionYTime()));
                ball.update(hitCoords[1] - (ball.getSpeedY()) * (time - ball.getLastCollisionYTime()),
                        hitCoords[0] + ball.getSpeedX() * (time - ball.getLastCollisionXTime()));
                if (ball.getY() + ball.getRadius() * 4 >= Game.getBackView().getHeight()) {
                    if (ball.getSpeedY() / Math.pow(0.75, collisionCounter) <= 0.01f || ball.getY() + ball.getRadius() * 2 >= Game.getBackView().getHeight()) {
                        ball.setSpeedY((float) ((dY / 3 * 2) * Math.pow(0.75, collisionCounter)));
                        ball.setLastCollisionYTime(time);
                        hitCoords[1] = ball.getObject().getTranslationY();
                        collisionCounter++;
                        if (collisionCounter == 2) { // ball hits ground
                            if (ball.getDirection() == Ball.Direction.RIGHT) {
                                ball.rotateRight();
                            } else {
                                ball.rotateLeft();
                            }
                        }
                    }
                    if (Math.abs(lastPosY - ball.getY()) < 3f && ball.getAnimationController().getState() == 0 && !ball.isFading()) { //fade ball
                       ball.fade(nextBall);
                    }
                    if (ball.getAnimationController().getState() == 2) {
                        ball.getAnimationController().setState(0);
                        animation.cancel();
                    }
                    lastPosY = ball.getY();
                }
                if (ball.hitBasket(basket) && !ball.isScored()) {
                    if (Game.hasVibrator() && Game.getVibrator().hasVibrator()) {
                        Game.getVibrator().vibrate(100L);
                    }
                    if (Game.isMusicOn()) {
                        Game.getMusicController().initMusic(context, R.raw.wow);
                        Game.getMusicController().playMusic();
                    }
                    Game.getScoreController().score();
                    ball.score();
                }
                if (ball.hitRightBasket(basket, time) || ball.hitRightWall(Game.getBackView(), time) || ball.hitLeftWall(time)
                        || ball.hitBottomBasket(basket, time)) {
                    hitCoords[0] = ball.getX();
                    ball.changeDirection(time);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ball.getAnimationController().setState(0);
               // ball.resetBall();
                ball.getAnimationController().getAnim().cancel();
                if (Game.getGameMode() == Game.Mode.UNLIMITED) {
                    layout.removeView(ball.getObject());
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }


}
