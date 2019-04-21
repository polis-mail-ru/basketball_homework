package ru.ok.technopolis.basketball.objects;

import android.view.View;

public class Basket {

    private final View object;

    public Basket(View object) {
        this.object = object;
    }

    public View getObject() {
        return object;
    }

    public float getX() {
        return object.getX();
    }

    public float getY() {
        return object.getY();
    }

    public float getRadius() {
        return object.getHeight() / 2f;
    }
}
