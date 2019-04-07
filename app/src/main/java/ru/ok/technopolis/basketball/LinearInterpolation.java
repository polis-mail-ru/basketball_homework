package ru.ok.technopolis.basketball;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinearInterpolation {
    public static Double[] interpolateArray(Double[] source, int destinationLength) {
        Double[] destination = new Double [destinationLength];
        destination[0] = source[0];
        int jPrevious = 0;
        for (int i = 1; i < source.length; i++) {
            int j = i * (destination.length - 1) / (source.length - 1);
            interpolate(destination, jPrevious, j, source[i - 1], source[i]);
            jPrevious = j;
        }
        return destination;
    }

    private static void interpolate(Double [] destination, int destFrom, int destTo, double valueFrom, double valueTo) {
        int destLength = destTo - destFrom;
        double valueLength = valueTo - valueFrom;
        for (int i = 0; i <= destLength; i++) {
            destination[destFrom + i] = (valueFrom + (valueLength * i) / destLength);
        }
    }


    static List interpolateList(List<Double> originalData, int itemCount) {
        Double [] finalArray = new Double[originalData.size()];
        for(int  i = 0; i < itemCount; i++){
            if(i < originalData.size())
            finalArray[i] = originalData.get(i);
        }
        finalArray = interpolateArray(finalArray, itemCount);
        return new ArrayList<>(Arrays.asList(finalArray));
    }
}
