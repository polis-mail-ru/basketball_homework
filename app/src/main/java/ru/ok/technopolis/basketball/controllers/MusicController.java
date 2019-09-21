package ru.ok.technopolis.basketball.controllers;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicController {
    private MediaPlayer soundsPlayer;
    private boolean canPlay;

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

    public boolean canPlay() {
        return canPlay && soundsPlayer != null;
    }
    public void setMode(boolean mode){
        canPlay = mode;
    }

    public void release(){
        soundsPlayer.release();
    }
}
