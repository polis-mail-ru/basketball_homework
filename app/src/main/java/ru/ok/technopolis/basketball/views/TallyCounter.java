package ru.ok.technopolis.basketball.views;

public interface TallyCounter {
    void reset();
    void increment();
    int getCount();
    void setCount(int count);
}
