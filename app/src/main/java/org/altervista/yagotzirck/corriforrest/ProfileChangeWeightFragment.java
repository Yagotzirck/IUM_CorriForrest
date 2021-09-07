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

public class ProfileChangeWeightFragment extends DialogFragment {

    private NumberPicker weightPicker;

    private ProfileChangeField listener;

    private int currentWeight;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        currentWeight = getArguments() != null ? getArguments().getInt("weight", 75) : 75;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.fragment_weight_picker, null);

        listener = (ProfileChangeField) getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (ProfileChangeField) getActivity();

        builder.setView(view)
                .setTitle("Aggiorna il tuo peso")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
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
        weightPicker = view.findViewById(R.id.profile_picker_weight);
    }

    private void setPickers(){


        weightPicker.setMinValue(20);
        weightPicker.setMaxValue(300);
        weightPicker.setValue(currentWeight);
    }

    private void dialogOK(){

        listener.updateField(Integer.toString(weightPicker.getValue()));
    }
}
