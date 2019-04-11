package ru.ok.technopolis.basketball;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

public class SettingsFragment extends Fragment {

    private boolean level;
    private boolean vibrate;
    private int score;

    private static final String LEVEL_KEY = "level";
    private static final String VIBRO_KEY = "vibro";
    private static final String SCORE_KEY = "score";
    private static final String LOG_TAG = "SettingFragment";
    OnCloseSetListener closeListener;

    public void setCloseListener(OnCloseSetListener closeListener) {
        this.closeListener = closeListener;
    }

    public static SettingsFragment newInstance(boolean level, boolean vibrate, int score) {

        Bundle args = new Bundle();
        args.putBoolean(LEVEL_KEY, level);
        args.putBoolean(VIBRO_KEY, vibrate);
        args.putInt(SCORE_KEY, score);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException();
        }
        level = args.getBoolean(LEVEL_KEY);
        vibrate = args.getBoolean(VIBRO_KEY);
        score = args.getInt(SCORE_KEY);
        Log.d(LOG_TAG, "onCreate args " + Boolean.toString(vibrate));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,
                container, false);
        ToggleButton level = view.findViewById(R.id.fragment_settings_level_button);
        level.setChecked(this.level);
        level.setOnCheckedChangeListener((v, isChecked) -> {
            SettingsFragment.this.level = isChecked;
        });

        ToggleButton vibro = view.findViewById(R.id.fragment_settings_vibration_button);
        if(vibrate) {

            vibro.setBackgroundResource(R.drawable.bell);
        } else {

            vibro.setBackgroundResource(R.drawable.main_ball);
        }
        vibro.setOnCheckedChangeListener((v, isChecked) -> {
            if (isChecked) {
                SettingsFragment.this.vibrate = true;
                Log.d(LOG_TAG, "ClickCheck " + Boolean.toString(vibrate));
                v.setBackgroundResource(R.drawable.bell);
            } else {
                SettingsFragment.this.vibrate = false;
                v.setBackgroundResource(R.drawable.main_ball);
            }
        });

        Button close = view.findViewById(R.id.fragment_settings_close_button);
        close.setOnClickListener(v -> {
            if (closeListener != null) {
                closeListener.close(this.level, this.vibrate, this.score);
            }
        });

        Button reset = view.findViewById(R.id.fragment_settings_reset_button);
        reset.setOnClickListener(v -> {
            AccuracyResource.deleteAll();
            score = 0;
        });

        return view;
    }


    interface OnCloseSetListener {
        void close(boolean level, boolean vibro, int score);
    }

}
