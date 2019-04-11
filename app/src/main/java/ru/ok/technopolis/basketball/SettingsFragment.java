package ru.ok.technopolis.basketball;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

public class SettingsFragment extends Fragment {

    private boolean level;
    private boolean vibrate;

    OnCloseSetListener closeListener;

    public void setCloseListener(OnCloseSetListener closeListener) {
        this.closeListener = closeListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.preferences.contains(MainActivity.APP_PREFERNCES_LEVEL)) {
            MainActivity.preferences.getBoolean(MainActivity.APP_PREFERNCES_LEVEL, level);
        } else {
            level = false;
        }
        if (MainActivity.preferences.contains(MainActivity.APP_PREFERNCES_VIBRATE)) {
            MainActivity.preferences.getBoolean(MainActivity.APP_PREFERNCES_VIBRATE, vibrate);
        } else {
            vibrate = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.preferences.contains(MainActivity.APP_PREFERNCES_LEVEL)) {
            MainActivity.preferences.getBoolean(MainActivity.APP_PREFERNCES_LEVEL, level);
        } else {
            level = false;
        }
        if (MainActivity.preferences.contains(MainActivity.APP_PREFERNCES_VIBRATE)) {
            MainActivity.preferences.getBoolean(MainActivity.APP_PREFERNCES_VIBRATE, vibrate);
        } else {
            vibrate = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,
                container, false);
        ToggleButton level = view.findViewById(R.id.fragment_settings_level_button);
        level.setChecked(this.level);
        level.setOnCheckedChangeListener((v, isChecked) -> {
            //v.setButtonDrawable(R.drawable.ball_main);
//v.setButtonDrawable(R.drawable.bell);
            SettingsFragment.this.level = isChecked;
        });

        ToggleButton vibro = view.findViewById(R.id.fragment_settings_vibration_button);
        vibro.setChecked(this.vibrate);
        vibro.setOnCheckedChangeListener((v, isChecked) -> {
            if (isChecked) {
                SettingsFragment.this.vibrate = true;
                v.setBackgroundResource(R.drawable.ball_main);
                v.setBackgroundResource(R.drawable.bell);
            } else {
                SettingsFragment.this.vibrate = false;
                v.setBackgroundResource(R.drawable.main_ball);
            }
        });

        Button close = view.findViewById(R.id.fragment_settings_close_button);
        close.setOnClickListener(v -> {
            if (closeListener != null) {
                closeListener.close();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        editor.putBoolean(MainActivity.APP_PREFERNCES_LEVEL, level);
        editor.putBoolean(MainActivity.APP_PREFERNCES_VIBRATE, vibrate);
        editor.apply();
    }

    interface OnCloseSetListener {
        void close();
    }

}
