package ru.ok.technopolis.basketball.controllers;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicController {
    private MediaPlayer soundsPlayer;

    public MusicController() {
    }



    void initMusic(Context context, int res) {
        soundsPlayer = MediaPlayer.create(context, res);
    }

    void playMusic() {
        if (canPlay()) {
            soundsPlayer.start();
        }
    }

    private boolean canPlay() {
        return soundsPlayer != null;
    }
}
