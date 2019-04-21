package ru.ok.technopolis.basketball.controllers;

import ru.ok.technopolis.basketball.StarView;

public class ScoreController {

    private final StarView view;

    public ScoreController(StarView view) {
        this.view = view;
    }

    public void score() {
        view.increase();
    }
}
