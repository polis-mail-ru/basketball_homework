package ru.ok.technopolis.basketball;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WinFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal,
                container, false);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        final ImageView view1  = view.findViewById(R.id.fragment_goal_whistle);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private float x = 10;
            private float bias = x;
            private float startX = view1.getX();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view1.setTranslationX(bias);
                bias += x;
                if (Math.abs(bias) > 100) {
                    x = -x;
                }
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                close();
            }

        });

        animator.start();
        view1.animate().start();
        return view;
    }

    private void close(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
