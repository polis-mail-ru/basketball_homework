package ru.ok.technopolis.basketball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = "Main_Activity_logs";

    int width;
    int height;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERNCES_SCORE = "score";
    public static final String APP_PREFERNCES_LEVEL = "level";
    public static final String APP_PREFERNCES_VIBRATE = "vibrate";

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
        if(preferences.contains(APP_PREFERNCES_SCORE)){
            preferences.getInt(APP_PREFERNCES_SCORE, score);
        }
        fragmentManager = getSupportFragmentManager();


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
                GameFragment gameFragment = GameFragment.newInstance(width, height, score);
                MenuFragment.OnMenuListener listener = this;
                gameFragment.setOnPauseListener(score -> {
                    MainActivity.this.score = score;
                    MenuFragment menuFragment = MenuFragment.newInstance();
                    menuFragment.setOnMenuListener(listener);
                    changeFragment(menuFragment);
                });
                changeFragment(gameFragment);
            }

            @Override
            public void showStat() {
                Log.d(LOG_TAG, "stat callback");
                StatisticFragment statisticFragment= new StatisticFragment();
                MenuFragment.OnMenuListener listener = this;
               statisticFragment.setCloseListener(() -> {
                   MenuFragment menuFragment1 = MenuFragment.newInstance();
                   menuFragment1.setOnMenuListener(listener);
                   changeFragment(menuFragment1);
               });
                changeFragment(statisticFragment);

            }

            @Override
            public void showSettings() {

                Log.d(LOG_TAG, "settings callback");
                SettingsFragment settingsFragment= new SettingsFragment();
                MenuFragment.OnMenuListener listener = this;
                settingsFragment.setCloseListener(() -> {
                        MenuFragment menuFragment = MenuFragment.newInstance();
                        menuFragment.setOnMenuListener(listener);
                        changeFragment(menuFragment);

                });
                changeFragment(settingsFragment);

            }
        });
        changeFragment(menuFragment);
    }

    @Override
    protected void onResume() {
        if(preferences.contains(APP_PREFERNCES_SCORE)){
            preferences.getInt(APP_PREFERNCES_SCORE, score);
        }
        super.onResume();
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
    protected void onStop() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(APP_PREFERNCES_SCORE, score);
        editor.apply();
        super.onStop();
    }
}
