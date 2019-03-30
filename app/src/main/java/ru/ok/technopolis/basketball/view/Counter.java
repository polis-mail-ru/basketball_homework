package ru.ok.technopolis.basketball.view;

public interface Counter {
    void reset();
    void increment();
    int getCount();
    void setCount(int count);
}
