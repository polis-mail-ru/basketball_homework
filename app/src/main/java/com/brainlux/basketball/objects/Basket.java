package com.brainlux.basketball.objects;

import android.view.View;

public class Basket {

    private final View object;

    public Basket(View object) {
        this.object = object;
    }

    float getX() {
        return object.getX();
    }

    float getY() {
        return object.getY();
    }

    float getRadius() {
        return object.getHeight() / 2f;
    }
}
