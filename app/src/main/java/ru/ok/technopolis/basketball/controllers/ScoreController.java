package ru.ok.technopolis.basketball.controllers;

import android.view.View;
import android.widget.TextView;

import ru.ok.technopolis.basketball.StarView;

public class ScoreController {

    private final StarView view;
    private final TextView coinCount;
    private int counter;

    public ScoreController(StarView view, View coinCount) {
        this.view = view;
        this.coinCount = (TextView) coinCount;
    }

    void score() {
        view.increase();
    }

    public StarView getView() {
        return view;
    }

    public void collect() {
        coinCount.setText(String.valueOf(++counter));
    }
}
