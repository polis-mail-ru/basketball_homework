package com.brainlux.basketball.objects;

import android.widget.TextView;

public class Record {
    private final TextView balls;
    private final TextView coinCount;
    private final TextView high;

    public Record(TextView balls, TextView coinCount, TextView high) {
        this.balls = balls;
        this.coinCount = coinCount;
        this.high = high;
        this.coinCount.setText(String.valueOf(Game.getCoinsCount()));
        this.high.setText(String.valueOf(Game.getHighScore()));
    }

    public void updateBallCount() {
        balls.setText(String.valueOf(Game.getBallsCount()));
    }

    public void collect() {
        coinCount.setText(String.valueOf(Game.increaseCoins()));
    }

    public void updateHighScore() {
        this.high.setText(String.valueOf(Game.getHighScore()));
    }
}
