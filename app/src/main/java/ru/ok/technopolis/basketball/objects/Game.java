package ru.ok.technopolis.basketball.objects;

import android.os.Vibrator;

import ru.ok.technopolis.basketball.BackView;
import ru.ok.technopolis.basketball.controllers.MusicController;
import ru.ok.technopolis.basketball.controllers.ScoreController;

public class Game {
    private static BackView backView;
    private static MusicController musicController;
    private static ScoreController scoreController;
    private static Mode gameMode;
    private static int throwsCount;

    private static Vibrator vibrator;

    public Game(Vibrator vibrator) {
        Game.vibrator = vibrator;
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
}
