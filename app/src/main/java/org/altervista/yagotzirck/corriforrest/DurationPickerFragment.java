package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DurationPickerFragment extends DialogFragment {

    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;

    private TargetValuePickersCallbacks listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.fragment_duration_picker, null);

        listener = (TargetValuePickersCallbacks)getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (TargetValuePickersCallbacks)getActivity();

        builder.setView(view)
                .setTitle("Scegli per quanto tempo correre")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogOK();
                    }
                });

        findViews(view);

        setPickers();

        return builder.create();
    }

    private void findViews(View view){
        hoursPicker = view.findViewById(R.id.pickerDuration_hours);
        minutesPicker = view.findViewById(R.id.pickerDuration_minutes);
    }

    private void setPickers(){
        final int hoursMinValue = 0;
        final int hoursMaxValue = 3_000;
        final int hoursStartingValue = 1;

        final int minutesMinValue = 0;
        final int minutesMaxValue = 59;
        final int minutesStartingValue = 0;

        hoursPicker.setMinValue(hoursMinValue);
        hoursPicker.setMaxValue(hoursMaxValue);
        hoursPicker.setValue(hoursStartingValue);

        minutesPicker.setMinValue(minutesMinValue);
        minutesPicker.setMaxValue(minutesMaxValue);
        minutesPicker.setValue(minutesStartingValue);

        hoursPicker.setOnValueChangedListener( (hoursNumPicker, oldVal, newVal) -> {
            if(newVal == 0)
                minutesPicker.setMinValue(1);
            else
                minutesPicker.setMinValue(0);
        });
    }

    private void dialogOK(){
        final int SECONDS_IN_HOUR = 3600;
        final int SECONDS_IN_MINUTE = 60;
        listener.targetValueCallback(SECONDS_IN_HOUR * hoursPicker.getValue() + SECONDS_IN_MINUTE * minutesPicker.getValue());
    }
}
