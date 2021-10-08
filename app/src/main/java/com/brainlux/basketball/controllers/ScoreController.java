package com.brainlux.basketball.controllers;


import com.brainlux.basketball.StarView;

public class ScoreController {

    private final StarView view;

    public ScoreController(StarView view) {
        this.view = view;
    }

    void score() {
        view.increase();

    }

    public StarView getView() {
        return view;
    }

    public int getCount(){
        return view.getCount();
    }

}
