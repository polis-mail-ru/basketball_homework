package ru.ok.technopolis.basketball.CustomView;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class Utils {
    public static int pxFromDP(DisplayMetrics displayMetrics, int res){
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, res, displayMetrics) + 0.5f);
    }
}
