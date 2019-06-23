package edu.luc.etl.cs313.android.simplestopwatch.android;


import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIUpdateListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.ConcreteStopwatchModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.StopwatchModelFacade;

/**
 * A thin adapter component for the stopwatch.
 *
 * @author laufer
 */
public class StopwatchAdapter extends Activity implements StopwatchUIUpdateListener {

    private static String TAG = "stopwatch-android-activity";

    /**
     * The state-based dynamic model.
     */
    private StopwatchModelFacade model;
    private Ringtone r;

    protected void setModel(final StopwatchModelFacade model) {
        this.model = model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject dependency on view so this adapter receives UI events
        setContentView(R.layout.activity_main);
        // inject dependency on model into this so model receives UI events
        this.setModel(new ConcreteStopwatchModelFacade());
        // inject dependency on this into model to register for UI updates
        model.setUIUpdateListener(this);

        TextView textView = (TextView) findViewById(R.id.seconds);

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction (TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    try {
                        final int newTimerValue = Integer.parseInt(editText.getText().toString());
                        model.onEditEnd(newTimerValue);
                    } catch (NumberFormatException nfe) {

                    }
                    hideEdit();
                }
                return  false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        model.onStart();
    }

    // TODO remaining lifecycle methods

    /**
     * Updates the seconds and minutes in the UI.
     * @param time
     */
    public void updateTime(final int time) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView tvS = (TextView) findViewById(R.id.seconds);
            tvS.setText(Integer.toString(time / 10) + Integer.toString(time % 10));
        });
    }

    /**
     * Updates the state name in the UI.
     * @param stateId
     */
    public void updateState(final int stateId) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView stateName = (TextView) findViewById(R.id.stateName);
            stateName.setText(getString(stateId));
        });
    }

    // forward event listener methods to the model
    public void onStartStop(final View view) {model.onStartStop();}

    public void onTextViewClick(final View view) {
        model.onTextViewClick();
    }

    public void playSound(int type){
        try {
            Uri notification = RingtoneManager.getDefaultUri(type);
            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBeep() {
        playSound(RingtoneManager.TYPE_NOTIFICATION);
    }

    public void playAlarm() {
        playSound(RingtoneManager.TYPE_ALARM);
    }

    public void stopAlarm() {
        r.stop();
    }

    public void displayEdit() {
        final EditText et = (EditText) findViewById(R.id.editText);
        final TextView tvS = (TextView) findViewById(R.id.seconds);
        et.setVisibility(View.VISIBLE);
        tvS.setVisibility(View.INVISIBLE);
    }

    public void hideEdit() {
        final EditText et = (EditText) findViewById(R.id.editText);
        final TextView tvS = (TextView) findViewById(R.id.seconds);
        et.setVisibility(View.INVISIBLE);
        tvS.setVisibility(View.VISIBLE);
    }
}
