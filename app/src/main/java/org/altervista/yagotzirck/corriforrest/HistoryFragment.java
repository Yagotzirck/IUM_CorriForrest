package org.altervista.yagotzirck.corriforrest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HistoryFragment extends Fragment implements DatePickersCallbacks, HistoryOpenSessionDetails {

    @Override
    public void dayCallback(Date date) {
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        if (fromButtonClicked) {
            if(date.compareTo(dateTo) <= 0 || isSameDay(date, dateTo)) {
                dateFrom = date;
                fromButton.setText(dateFormatter.format(date));
            }
            else{
                makeToast("La data \"Dal\" dev'essere inferiore alla data \"al\"!").show();

            }
        }
        else {
            if (date.compareTo(dateFrom) >= 0) {
                dateTo = date;
                toButton.setText(dateFormatter.format(date));
            }
            else{
                makeToast("La data \"al\" dev'essere superiore alla data \"Dal\"!").show();
            }
        }

        displaySelectedSessions();
    }


    @Override
    public void monthYearCallback(Date monthYearDate) {
        DateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
        monthYear = monthYearDate;
        monthButton.setText(dateFormatter.format(monthYearDate));

        displaySelectedSessions();
    }

    @Override
    public void yearCallback(Date yearDate) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy");
        year = yearDate;
        yearButton.setText(dateFormatter.format(yearDate));

        displaySelectedSessions();
    }

    @Override
    public void openSessionDetailsCallback(DataSession ds) {
        return;
    }


    private enum ClickedRadioButton {
        CUSTOM,
        MONTH,
        YEAR,
        ALL
    };

    private RadioButton radioCustom;
    private RadioButton radioMonth;
    private RadioButton radioYear;
    private RadioButton radioAll;


    private LinearLayout customSelectionLayout;
    private LinearLayout monthSelectionLayout;
    private LinearLayout yearSelectionLayout;
    private LinearLayout allSelectionLayout;


    private Button fromButton;
    private Button toButton;

    private Button monthButton;

    private Button yearButton;


    private Date dateFrom, dateTo, monthYear, year;
    private boolean fromButtonClicked = false;   // true = user clicked "from" in interval selection, "to" otherwise

    private ClickedRadioButton selectedRadioButton;

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setListeners();
        setDefaultButtonFields();
    }



    private void findViews(View view){
        radioCustom = view.findViewById(R.id.history_radioCustom);
        radioMonth = view.findViewById(R.id.history_radioMonth);
        radioYear = view.findViewById(R.id.history_radioYear);
        radioAll = view.findViewById(R.id.history_radioAll);

        customSelectionLayout = view.findViewById(R.id.history_customSelection);
        monthSelectionLayout = view.findViewById(R.id.history_monthSelection);
        yearSelectionLayout = view.findViewById(R.id.history_yearSelection);
        allSelectionLayout = view.findViewById(R.id.history_allSelection);

        fromButton = view.findViewById(R.id.history_buttonFrom);
        toButton = view.findViewById(R.id.history_buttonTo);
        monthButton = view.findViewById(R.id.history_buttonMonth);
        yearButton = view.findViewById(R.id.history_buttonYear);

        listView = view.findViewById(R.id.history_listView);
    }

    private void setListeners(){
        radioCustom.setOnClickListener( v -> checkClickedRadioButton(ClickedRadioButton.CUSTOM));
        radioMonth.setOnClickListener( v -> checkClickedRadioButton(ClickedRadioButton.MONTH));
        radioYear.setOnClickListener( v -> checkClickedRadioButton(ClickedRadioButton.YEAR));
        radioAll.setOnClickListener( v -> checkClickedRadioButton(ClickedRadioButton.ALL));

        monthButton.setOnClickListener(v -> new MonthYearPickerFragment().show(getChildFragmentManager(), null) );
        yearButton.setOnClickListener(v -> new YearPickerFragment().show(getChildFragmentManager(), null));

        fromButton.setOnClickListener(v -> {
            fromButtonClicked = true;
            new DatePickerFragment().show(getChildFragmentManager(), "datePicker");
        });

        toButton.setOnClickListener(v -> {
            fromButtonClicked = false;
            new DatePickerFragment().show(getChildFragmentManager(), "datePicker");
        });
    }


    private void checkClickedRadioButton(ClickedRadioButton clickedRadioButton){
        customSelectionLayout.setVisibility(View.GONE);
        monthSelectionLayout.setVisibility(View.GONE);
        yearSelectionLayout.setVisibility(View.GONE);
        allSelectionLayout.setVisibility(View.GONE);

        switch(clickedRadioButton){
            case CUSTOM: customSelectionLayout.setVisibility(View.VISIBLE); break;
            case MONTH: monthSelectionLayout.setVisibility(View.VISIBLE); break;
            case YEAR: yearSelectionLayout.setVisibility(View.VISIBLE); break;
            case ALL: allSelectionLayout.setVisibility(View.VISIBLE); break;
        }

        selectedRadioButton = clickedRadioButton;

        displaySelectedSessions();

    }

    private boolean displaySelectedSessions(){
        DataSessions dataSessionsInstance = DataSessions.getInstance();
        ArrayList<DataSession> dataSessionsFiltered;
        String user = LoggedUser.getInstance().get();


        switch(selectedRadioButton){
            case CUSTOM:
                dataSessionsFiltered = dataSessionsInstance.getSessionsByInterval(user, dateFrom, dateTo);
                break;

            case MONTH:
                dataSessionsFiltered = dataSessionsInstance.getSessionsByMonth(user, monthYear);
                break;

            case YEAR:
                dataSessionsFiltered = dataSessionsInstance.getSessionsByYear(user, year);
                break;

            case ALL:
            default:
                dataSessionsFiltered = dataSessionsInstance.getAllSessions(user);

        }

        /*
        QUA BISOGNA AGGIUNGERE UNA ROBA CHE RENDE VISIBILE UNA TEXTVIEW CHE INFORMA L'UTENTE CON
        "Non sono presenti sessioni nell'intervallo temporale selezionato."
        
        if(dataSessionsFiltered.isEmpty())
            return false;

         */

        HistorySessionsAdapter adapter = new HistorySessionsAdapter(getContext(), dataSessionsFiltered, this);
        listView.setAdapter(adapter);


        return true;
    }


    private void setDefaultButtonFields(){
        Calendar currDateCal = Calendar.getInstance();

        Calendar prevWeekCal = Calendar.getInstance();
        prevWeekCal.add(Calendar.DATE, -7);

        Date currDate = currDateCal.getTime();
        Date prevWeek = prevWeekCal.getTime();

        // dummy initializations in order to avoid accessing null variables
        // on first dayCallback() call inside this method
        dateFrom = currDateCal.getTime();
        dateTo =prevWeekCal.getTime();

        selectedRadioButton = ClickedRadioButton.CUSTOM;


        fromButtonClicked = true;
        dayCallback(prevWeek);

        fromButtonClicked = false;
        dayCallback(currDate);

        monthYearCallback(currDate);
        yearCallback(currDate);



        displaySelectedSessions();
    }

    private Toast makeToast(String msg) {
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView toastMessage = (TextView) view.findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        view.setBackgroundColor(Color.RED);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    private static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

}
