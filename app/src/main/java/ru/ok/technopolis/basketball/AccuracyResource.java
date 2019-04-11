package ru.ok.technopolis.basketball;

import java.util.ArrayList;
import java.util.List;

class AccuracyResource {
    private static double maxHeight = Double.MIN_VALUE;
    private static List<Double> accuracy = new ArrayList<>();;

    private AccuracyResource(){
        accuracy = new ArrayList<>();
    }

    static void addElement(double value){
        accuracy.add(value);
        if(maxHeight < value){
            maxHeight = value;
        }
    }

    static List<Double> getElements(){
        return accuracy;
    }

    static double getMaxHeight(){
        return  maxHeight;
    }

    static void deleteAll(){
        accuracy.clear();
    }

}
