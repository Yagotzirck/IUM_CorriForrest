package org.altervista.yagotzirck.corriforrest;

import android.app.Dialog;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getParentFragment().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_month_year_picker, null);

        listener = (DatePickersCallbacks)getParentFragment();

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
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1);

        yearPicker.setMinValue(0);
        yearPicker.setMaxValue(3000);
        yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void dialogOK(){
        Calendar monthYear = Calendar.getInstance();
        monthYear.set(Calendar.MONTH, monthPicker.getValue() - 1);
        monthYear.set(Calendar.YEAR, yearPicker.getValue());

        listener.monthYearCallback(monthYear.getTime());
    }
}
