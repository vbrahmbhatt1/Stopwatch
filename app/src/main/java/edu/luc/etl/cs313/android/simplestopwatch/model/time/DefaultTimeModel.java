package edu.luc.etl.cs313.android.simplestopwatch.model.time;

import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.*;

/**
 * An implementation of the stopwatch data model.
 */
public class DefaultTimeModel implements TimeModel {

    private int timer = 0;

    @Override
    public void resetTimer() {
        timer = 0;
    }

    @Override
    public void increment() {
        timer ++;
    }

    @Override
    public void decrement() {
        timer --;
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void setTimer(int timer){
        this.timer = timer;
    }

    @Override
    public boolean isZero() {
        return this.timer == 0;
    }

    @Override
    public boolean isFull() {
        return timer == Constants.MAX_CLICK_COUNT; // check for display value if it has reached the max value
    }
}