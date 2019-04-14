package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = "Main_Activity_logs";

    static MediaPlayer mediaPlayer;
    int width;
    int height;
    boolean isEasy;
    boolean isVibrationOn;
    boolean isMusicOn;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_LEVEL = "level";
    public static final String APP_PREFERENCES_VIBRATE = "vibrate";
    public static final String APP_PREFERENCES_MUSIC = "music";

    public static SharedPreferences preferences;

    private FragmentManager fragmentManager;

    private int score;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        isEasy = preferences.getBoolean(APP_PREFERENCES_LEVEL, false);
        isVibrationOn = preferences.getBoolean(APP_PREFERENCES_VIBRATE, true);
        isMusicOn = preferences.getBoolean(APP_PREFERENCES_MUSIC, true);

        fragmentManager = getSupportFragmentManager();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.backgroud_main_music);
        mediaPlayer.setVolume(0.2f, 0.4f);
        mediaPlayer.setLooping(true);
        if (isMusicOn) {
            mediaPlayer.start();

        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        score = 0;


        MenuFragment menuFragment = MenuFragment.newInstance();
        menuFragment.setOnMenuListener(new MenuFragment.OnMenuListener() {
            @Override
            public void play() {
                Log.d(LOG_TAG, "play callback");
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                GameFragment gameFragment = GameFragment.newInstance(width, height, score, isVibrationOn, isEasy, isMusicOn);
                MenuFragment.OnMenuListener listener = this;
                gameFragment.setOnPauseListener((score, isEasy, isVibrationOn) -> {
                    MainActivity.this.score = score;
                    MenuFragment menuFragment = MenuFragment.newInstance();
                    menuFragment.setOnMenuListener(listener);

                    if (mediaPlayer != null && isMusicOn) {

                        try {
                            mediaPlayer.prepare();
                        } catch (IOException ignore) {

                        }


                        mediaPlayer.start();
                    }
                    //fragmentManager.popBackStack();
                    changeFragment(menuFragment);
                });
                //fragmentManager.popBackStack();
                changeFragment(gameFragment);
            }

            @Override
            public void showStat() {
                Log.d(LOG_TAG, "stat callback");
                StatisticFragment statisticFragment = new StatisticFragment();
                MenuFragment.OnMenuListener listener = this;
                statisticFragment.setCloseListener(() -> {
                    MenuFragment menuFragment1 = MenuFragment.newInstance();
                    menuFragment1.setOnMenuListener(listener);
                    //fragmentManager.popBackStack();
                    changeFragment(menuFragment1);
                });
                //fragmentManager.popBackStack();
                changeFragment(statisticFragment);

            }

            @Override
            public void showSettings() {

                Log.d(LOG_TAG, "settings callback");
                SettingsFragment settingsFragment = SettingsFragment.newInstance(isEasy, isVibrationOn, isMusicOn, score);
                MenuFragment.OnMenuListener listener = this;
                settingsFragment.setCloseListener((isEasy, isMusicOn, isVibrationOn, score) -> {
                    MainActivity.this.isMusicOn = isMusicOn;
                    MainActivity.this.score = score;
                    MainActivity.this.isEasy = isEasy;
                    MainActivity.this.isVibrationOn = isVibrationOn;
                    MenuFragment menuFragment = MenuFragment.newInstance();
                    menuFragment.setOnMenuListener(listener);
                    //fragmentManager.popBackStack();
                    changeFragment(menuFragment);

                });
                //fragmentManager.popBackStack();
                changeFragment(settingsFragment);

            }
        });
        //fragmentManager.popBackStack();
        changeFragment(menuFragment);



        /*Button exit = findViewById(R.id.activity_main_exit_button);
        Button backToMenu = findViewById(R.id.activity_main_back_button);*/

        /*exit.setOnClickListener(v -> {
            MainActivity.this.finish();
        });

         fragmentManager.getBackStackEntryCount();

        backToMenu.setOnClickListener(v -> {
            changeFragment(menuFragment);
        });*/
    }


    @Override
    public void onBackPressed() {

        BackPressListener backPressListener = null;
        for (Fragment fr : fragmentManager.getFragments()) {
            if (fr instanceof BackPressListener) {
                backPressListener = (BackPressListener) fr;
                break;
            }
        }

        if (backPressListener != null) {
            backPressListener.onBackPressed();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMusicOn && !mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
        Log.d(LOG_TAG, "onResume");
    }

    private void changeFragment(Fragment f) {
        Log.d(LOG_TAG, "Change to " + f.getClass().getSimpleName());
        fragmentManager.beginTransaction().replace(R.id.activity_main_container, f).addToBackStack(f.getClass().getSimpleName()).commit();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_PREFERENCES_LEVEL, isEasy);
        editor.putBoolean(APP_PREFERENCES_VIBRATE, isVibrationOn);
        editor.putBoolean(APP_PREFERENCES_MUSIC, isMusicOn);
        editor.apply();
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }
}
