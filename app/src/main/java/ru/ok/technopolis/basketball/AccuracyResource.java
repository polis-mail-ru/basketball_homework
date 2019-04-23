package ru.ok.technopolis.basketball;

import java.util.ArrayList;
import java.util.List;

class AccuracyResource {
    private double maxHeight = Double.MIN_VALUE;
    private List<Double> accuracy = new ArrayList<>();


    static class SingletonHolder {
        static final AccuracyResource HOLDER_INSTANCE = new AccuracyResource();
    }

    static AccuracyResource getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    void addElement(double value){
        accuracy.add(value);
        if(maxHeight < value){
            maxHeight = value;
        }
    }

    List<Double> getElements(){
        return accuracy;
    }

    double getMaxHeight(){
        return  maxHeight;
    }

    void deleteAll(){
        accuracy.clear();
    }

}
