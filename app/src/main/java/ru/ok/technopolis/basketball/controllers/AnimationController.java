package ru.ok.technopolis.basketball.controllers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class AnimationController {
    public static byte state;
    public static ObjectAnimator anim;
    private final ImageView ball;
    public static final int BALL_DURATION = 4000;

    public AnimationController(ImageView ball) {
        this.ball = ball;
    }

    public void setBallRotation(int i) {
        anim.end();
        anim.removeAllListeners();
        anim = ObjectAnimator.ofFloat(ball, "rotation", i * 360);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setDuration(1250);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    public void startBallRotation() {
        anim = ObjectAnimator.ofFloat(ball, "rotation", 360);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setDuration(2000);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    public void fadeBall() {
        AnimationController.state = 1;
        ObjectAnimator anim = ObjectAnimator.ofFloat(ball, "alpha", 0);
        anim.setDuration(1000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimationController.state = 2;
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

}
