package ru.ok.technopolis.basketball.objects;

import android.view.View;

import ru.ok.technopolis.basketball.controllers.AnimationController;

public class Player {

    private final View object;
    private final AnimationController animationController;

    public Player(View object) {
        this.object = object;
        animationController = new AnimationController();
    }

    public void jump(float y) {
        animationController.jumpPlayer(object, y);
    }

}
