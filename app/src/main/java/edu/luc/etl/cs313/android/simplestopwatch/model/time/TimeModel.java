package edu.luc.etl.cs313.android.simplestopwatch.model.time;

/**
 * The passive data model of the stopwatch.
 * It does not emit any events.
 *
 * @author laufer
 */
public interface TimeModel {
    void resetTimer();

    void increment();

    void decrement();

    int getTimer();

    void setTimer(int timer);

    boolean isZero();

    boolean isFull();
}