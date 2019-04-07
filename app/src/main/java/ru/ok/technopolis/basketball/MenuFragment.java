package ru.ok.technopolis.basketball;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,
                container, false);
        /*Button stop = view.findViewById(R.id.main_activity_stop_button);
        stop.setOnClickListener(v -> close());*/
        Button playButton = view.findViewById(R.id.fragment_menu_play_button);
        playButton.setOnClickListener(v -> close());
        Button statsButton = view.findViewById(R.id.fragment_menu_stats_button);
        statsButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            // добавляем фрагмент
            StatisticFragment myFragment = new StatisticFragment();
            fragmentTransaction.replace(R.id.fragment_menu_layout, myFragment).addToBackStack("StatFr");
            fragmentTransaction.commit();
        });


        return view;
    }

    private void close(){
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
}
