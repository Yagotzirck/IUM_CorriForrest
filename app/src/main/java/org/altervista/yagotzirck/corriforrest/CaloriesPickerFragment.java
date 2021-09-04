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

import java.util.Calendar;

public class CaloriesPickerFragment extends DialogFragment {

    private NumberPicker caloriesPicker;

    private TargetValuePickersCallbacks listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.fragment_calories_picker, null);

        listener = (TargetValuePickersCallbacks)getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (TargetValuePickersCallbacks)getActivity();

        builder.setView(view)
                .setTitle("Scegli quante calorie bruciare")
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
        caloriesPicker = view.findViewById(R.id.pickerCalories);
    }

    private void setPickers(){
        final int minValue = 1;
        final int maxValue = 1_000_000;
        final int startingValue = 500;

        caloriesPicker.setMinValue(minValue);
        caloriesPicker.setMaxValue(maxValue);
        caloriesPicker.setValue(startingValue);
    }

    private void dialogOK(){
        listener.targetValueCallback(caloriesPicker.getValue());
    }
}
