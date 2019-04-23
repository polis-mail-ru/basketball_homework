package ru.ok.technopolis.basketball;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment implements BackPressListener{

    private static final String LOG_TAG = "MenuFragmentLogs";
    OnMenuListener onMenuListener;

    public static MenuFragment newInstance() {

        Bundle args = new Bundle();
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,
                container, false);
        Button playButton = view.findViewById(R.id.fragment_menu_play_button);
        playButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "play click");

            if(onMenuListener != null){
                onMenuListener.play();
            }
        });
        Button statsButton = view.findViewById(R.id.fragment_menu_stats_button);
        statsButton.setOnClickListener(v -> {
            if(onMenuListener != null){
                Log.d(LOG_TAG, "stat click");
                onMenuListener.showStat();
            }
        });

        Button settingsButton = view.findViewById(R.id.fragment_menu_settings_button);
        settingsButton.setOnClickListener(v -> {
            if(onMenuListener != null){
                Log.d(LOG_TAG, "setting click");
                onMenuListener.showSettings();
            }
        });


        return view;
    }

    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }

    @Override
    public void onBackPressed() {
        if(getActivity() != null)
            getActivity().finish();

    }

    interface OnMenuListener {
        void play();
        void showStat();
        void showSettings();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "menu fragment onDestroy");
    }


}
