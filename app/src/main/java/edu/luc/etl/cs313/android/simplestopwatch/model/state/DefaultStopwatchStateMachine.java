package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIUpdateListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

    private final TimeModel timeModel;
    private final ClockModel clockModel;
    private StopwatchState state;
    private StopwatchUIUpdateListener uiUpdateListener;


    public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
    }

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */

    protected void setState(final StopwatchState state) {
        this.state = state;
        uiUpdateListener.updateState(state.getId());
    }

    @Override
    public void setUIUpdateListener(final StopwatchUIUpdateListener uiUpdateListener) {
        this.uiUpdateListener = uiUpdateListener;
    }

    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override public synchronized void onStartStop() { state.onStartStop(); }

    @Override public synchronized void onTextViewClick() {state.onTextViewClick();}

    @Override public synchronized void onTick()      { state.onTick(); }

    @Override public void updateUIRuntime() { uiUpdateListener.updateTime(timeModel.getTimer()); }

    // known states
    private final StopwatchState STOPPED     = new StoppedState(this);
    private final StopwatchState RUNNING     = new RunningState(this);
    private final StopwatchState INCREMENT = new IncrementState(this);
    private final StopwatchState ALARMING = new AlarmingState(this);

    // transitions
    @Override public void toRunningState()    { setState(RUNNING); }
    @Override public void toStoppedState()    { setState(STOPPED); }
    @Override public void toIncrementState() { setState(INCREMENT); }
    @Override public void toAlarmingState() { setState(ALARMING); }

    // actions
    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetTimer(); actionUpdateView(); }
    @Override
    public void actionStart() {
        actionBeep();
        clockModel.stop();
        clockModel.start();
    }

    @Override
    public void actionStop() {
        stopAlarm();
        clockModel.stop();
        toStoppedState();
        actionReset();
    }

    @Override
    public void actionInc() {
        if (timeModel.isFull()){
            toRunningState();
            actionStart();
        }
        else {
            timeModel.increment();
            clockModel.stop();
            clockModel.start();
        }
        actionUpdateView();
    }
    @Override public void actionUpdateView() { state.updateView(); }

    @Override public void actionDisplayEdit() { uiUpdateListener.displayEdit(); }

    @Override
    public void actionDec(){
        timeModel.decrement();
        actionUpdateView();
        if (timeModel.isZero()){
            toAlarmingState();
            actionAlarm();
        }
    }

    @Override
    public void actionBeep() {
        uiUpdateListener.playBeep();
    }

    public void actionAlarm() { uiUpdateListener.playAlarm(); }
    public void stopAlarm() { uiUpdateListener.stopAlarm(); }

    @Override
    public void setTimerValue ( int value ) {
        timeModel.setTimer(value);
        toRunningState();
        actionStart();
        actionUpdateView();
    }
}
