package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class YearPickerFragment extends DialogFragment {

    private NumberPicker yearPicker;

    private DatePickersCallbacks listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getParentFragment().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_year_picker, null);

        listener = (DatePickersCallbacks)getParentFragment();

        builder.setView(view)
                .setTitle("Scegli anno")
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
        yearPicker = view.findViewById(R.id.pickerYear_year);
    }

    private void setPickers(){

        yearPicker.setMinValue(0);
        yearPicker.setMaxValue(3000);
        yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void dialogOK(){
        Calendar year = Calendar.getInstance();
        year.set(Calendar.YEAR, yearPicker.getValue());

        listener.yearCallback(year.getTime());
    }
}
