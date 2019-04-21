package ru.ok.technopolis.basketball.objects;

import android.widget.ImageView;

public class Basket {

    private final ImageView object;

    public Basket(ImageView object) {
        this.object = object;
    }

    public ImageView getObject() {
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
