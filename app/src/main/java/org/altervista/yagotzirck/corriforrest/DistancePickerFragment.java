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

public class DistancePickerFragment extends DialogFragment {

    private NumberPicker distancePicker;

    private TargetValuePickersCallbacks listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.fragment_distance_picker, null);

        listener = (TargetValuePickersCallbacks)getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (TargetValuePickersCallbacks)getActivity();

        builder.setView(view)
                .setTitle("Scegli la distanza da percorrere")
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
        distancePicker = view.findViewById(R.id.pickerDistance);
    }

    private void setPickers(){
        final int minValue = 1;
        final int maxValue = 100_000;
        final int startingValue = 1;



        distancePicker.setMinValue(minValue);
        distancePicker.setMaxValue(maxValue);
        distancePicker.setValue(startingValue);
    }

    private void dialogOK(){
        final int METERS_IN_KM = 1000;
        listener.targetValueCallback(METERS_IN_KM * distancePicker.getValue());
    }
}
