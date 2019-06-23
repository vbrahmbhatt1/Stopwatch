package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class IncrementState implements StopwatchState {

    private final StopwatchSMStateView sm;

    private int tickCounter = 0;

    public IncrementState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    @Override
    public void onStartStop() {
        resetTickCounter();
        sm.actionInc();
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public void onTick() {
        if (tickCounter >= 3){
            resetTickCounter();
            sm.toRunningState();
            sm.actionStart();
        }
        else {
            tickCounter++;
        }
    }

    @Override
    public void onTextViewClick () {

    }

    @Override
    public int getId() {
        return R.string.INCREMENT;
    }

    public void resetTickCounter() {
        tickCounter = 0;
    }
}