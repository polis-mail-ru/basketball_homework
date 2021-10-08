package com.brainlux.basketball.objects;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.brainlux.basketball.Tools.RandomGenerator;

public class Coin {
    private final ImageView coinView;
    private final AnimationDrawable anim;

    public Coin(View coinView) {
        this.coinView = (ImageView) coinView;
        anim = (AnimationDrawable) coinView.getBackground();
    }

    private void resetPosition() {
        coinView.setX(RandomGenerator.coinPosX());
        coinView.setY(RandomGenerator.coinPosY());
    }

    private void start() {
        anim.start();
    }

    private void stop() {
        anim.stop();
    }

    public void collect() {
        resetPosition();
        hide();
    }

    float getX() {
        return coinView.getX();
    }

    float getY() {
        return coinView.getY();
    }

    float getRadius() {
        return coinView.getHeight() / 2f;
    }

    public void show() {
        start();
        resetPosition();
        coinView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        stop();
        coinView.setVisibility(View.INVISIBLE);
    }

    public boolean isVisible() {
        return coinView.getVisibility() == View.VISIBLE;
    }
}
