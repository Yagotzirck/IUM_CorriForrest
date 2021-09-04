package org.altervista.yagotzirck.corriforrest;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.widget.DatePicker;
import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;



public class DatePickerFragment extends DialogFragment{
    Calendar date;
    private DatePickersCallbacks listener;

    private int year,month, dayOfMonth;

    private boolean blockPastDays;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        date = Calendar.getInstance();

        listener = (DatePickersCallbacks) getParentFragment();
        if(listener == null)    // Dialog used inside an activity
            listener = (DatePickersCallbacks) getActivity();

        blockPastDays = getArguments() != null && getArguments().getBoolean("blockPastDays", false);

        int currYear = date.get(Calendar.YEAR);
        int currMonth = date.get(Calendar.MONTH);
        int currDay = date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.MyDatePickerDialogStyle, (DatePickerDialog.OnDateSetListener)
                null, currYear, currMonth, currDay);

        if(blockPastDays)
            dpd.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());

        dpd.show();

        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", (dialog, whichButton) -> {
            DatePicker dp = dpd.getDatePicker();
            date.set(Calendar.YEAR, dp.getYear());
            date.set(Calendar.MONTH, dp.getMonth());
            date.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());

            listener.dayCallback(date.getTime());
        });

        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Annulla", (dialog, whichButton) -> dismiss());

        dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

        return dpd;
    }



}
