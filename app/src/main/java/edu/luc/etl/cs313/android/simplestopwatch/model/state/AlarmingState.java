package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class AlarmingState implements StopwatchState {

    private final StopwatchSMStateView sm;

    public AlarmingState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    @Override
    public void onStartStop() {
        sm.toStoppedState();
        sm.actionStop();
    }

    @Override
    public void updateView() {}

    @Override
    public void onTick() {}

    @Override
    public void onTextViewClick(){

    }

    @Override
    public int getId() {
        return R.string.ALARMING;
    }
}
