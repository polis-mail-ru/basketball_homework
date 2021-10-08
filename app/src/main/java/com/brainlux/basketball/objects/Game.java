package com.brainlux.basketball.objects;

import android.os.Vibrator;

import com.brainlux.basketball.BackView;
import com.brainlux.basketball.controllers.MusicController;
import com.brainlux.basketball.controllers.ScoreController;

public class Game {
    private static BackView backView;
    private static MusicController musicController;
    private static ScoreController scoreController;
    private static Mode gameMode;
    private static int throwsCount;
    private static int ballsCount;
    private static int coinsCount;
    private static int highScore;

    private static Vibrator vibrator;

    public Game() {
    }

    public static void setBackView(BackView backView) {
        Game.backView = backView;
    }

    public static BackView getBackView() {
        return backView;
    }

    public static Vibrator getVibrator() {
        return vibrator;
    }

    public static boolean hasVibrator() {
        return vibrator != null;
    }

    public static void setVibrator(Vibrator vibrator) {
        Game.vibrator = vibrator;
    }

    public static MusicController getMusicController() {
        return musicController;
    }

    public static void setMusicController(MusicController musicController) {
        Game.musicController = musicController;
    }

    public static ScoreController getScoreController() {
        return scoreController;
    }

    public static void setScoreController(ScoreController scoreController) {
        Game.scoreController = scoreController;
    }

    public static Mode getGameMode() {
        return gameMode;
    }

    public static void setGameMode(Mode gameMode) {
        Game.gameMode = gameMode;
    }

    public static boolean isMusicOn() {
        return musicController != null;
    }

    static int increaseCoins() {
        return ++coinsCount;
    }

    static int getCoinsCount() {
        return coinsCount;
    }

    public enum Mode {
        DEFAULT,
        UNLIMITED
    }

    public static int getThrowsCount() {
        return throwsCount;
    }

    public static void throwIncrease() {
        throwsCount++;
    }

    public static int getBallsCount() {
        return ballsCount;
    }

    public static void setBallsCount(int ballsCount) {
        Game.ballsCount = ballsCount;
    }

    static int getHighScore() {
        return highScore;
    }

    public static void updateHighScore(){
        highScore = Math.max(scoreController.getCount(), highScore);
    }
}
