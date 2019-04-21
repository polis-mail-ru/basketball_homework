package ru.ok.technopolis.basketball.controllers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimationController {
    public static byte state;
    public static ObjectAnimator anim;
    public static final int BALL_DURATION = 8000;

    public AnimationController() {
    }

    public void setBallRotation(View ball, int i) {
        anim.end();
        anim.removeAllListeners();
        anim = ObjectAnimator.ofFloat(ball, "rotation", i * 360);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setDuration(1250);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    public void startBallRotation(View ball) {
        anim = ObjectAnimator.ofFloat(ball, "rotation", 360);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setDuration(2000);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    public void fadeBall(View ball) {
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

    public void jumpPlayer(final View player, final float y) {
        player.animate().setDuration(350).translationYBy(-y).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                player.animate().setDuration(350).translationYBy(y).setListener(null).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).start();

    }
}
