package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MonthYearPickerFragment extends DialogFragment {

    private NumberPicker monthPicker;
    private NumberPicker yearPicker;

    private DatePickersCallbacks listener;

    private boolean blockPastDays;

    private int currMonth;
    private int currYear;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        blockPastDays = getArguments() != null && getArguments().getBoolean("blockPastDays", false);
        currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        currYear = Calendar.getInstance().get(Calendar.YEAR);

        LayoutInflater inflater;

        /*
        // called from within a fragment
        if(getParentFragment() != null)
            inflater = getParentFragment().getLayoutInflater();
        // called from within an activity
        else
        */
        inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view = inflater.inflate(R.layout.fragment_month_year_picker, null);

        listener = (DatePickersCallbacks)getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (DatePickersCallbacks)getActivity();

        builder.setView(view)
                .setTitle("Scegli mese ed anno")
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
        monthPicker = view.findViewById(R.id.pickerMonthYear_month);
        yearPicker = view.findViewById(R.id.pickerMonthYear_year);
    }

    private void setPickers(){
        monthPicker.setMinValue(blockPastDays ? currMonth : 1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(currMonth);

        yearPicker.setMinValue(currYear);
        yearPicker.setMaxValue(3000);
        yearPicker.setValue(currYear);

        if(blockPastDays)
            yearPicker.setOnValueChangedListener( (yearNumPicker, oldVal, newVal) -> {
                if(yearNumPicker.getValue() == currYear){
                    if(monthPicker.getValue() < currMonth)
                        monthPicker.setValue(currMonth);
                    monthPicker.setMinValue(currMonth);
                }
                else
                    monthPicker.setMinValue(1);
            });


    }

    private void dialogOK(){
        Calendar monthYear = Calendar.getInstance();

        // The day is set to 15 in order to avoid problems in case the current day of the month is e.g. 31
        // and the selected month has < 31 days (Feb/Apr/Jun/Sep/Nov)
        monthYear.set(Calendar.DAY_OF_MONTH, 15);
        monthYear.set(Calendar.MONTH, monthPicker.getValue() - 1);
        monthYear.set(Calendar.YEAR, yearPicker.getValue());


        listener.monthYearCallback(monthYear.getTime());
    }
}
