package ru.ok.technopolis.basketball;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StatisticFragment extends Fragment {

    OnCloseListener closeListener;

    public void setCloseListener(OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat,
                container, false);
        Button close = view.findViewById(R.id.fragment_stat_close_button);
        close.setOnClickListener(v -> {
            if(closeListener != null){
                closeListener.close();
            }
        });

        return view;
    }

    interface OnCloseListener{
        void close();
    }

}
