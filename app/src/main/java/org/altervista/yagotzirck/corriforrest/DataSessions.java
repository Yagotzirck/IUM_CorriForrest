package org.altervista.yagotzirck.corriforrest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DataSessions {

    private static DataSessions instance = null;

    private static ArrayList<DataSession> dataSessionsList;

    private DataSessions(){}

    public static DataSessions getInstance() {
        if (instance == null) {
            instance = new DataSessions();
            dataSessionsList = new ArrayList<>();

            dataSessionsList.add(new DataSession("a", buildDate(12, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(13, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(14, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(15, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(16, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(17, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(18, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(19, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("a", buildDate(20, 2, 2021), 1000, 4500));
        }
        return instance;
    }

    public void add(DataSession dataSession){ dataSessionsList.add(dataSession); }

    public boolean contains(DataSession dataSession){ return dataSessionsList.contains(dataSession); }


    public ArrayList<DataSession> getAllSessions(String user) {
        ArrayList<DataSession> allSessions = new ArrayList<>();

        for (DataSession ds : dataSessionsList)
            if (ds.getUser().equals(user))
                allSessions.add(ds);

        Collections.sort(allSessions, Collections.reverseOrder());
        return allSessions;

    }




    public ArrayList<DataSession> getSessionsByInterval(String user, Date date1, Date date2){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDateValue();
            if (ds.getUser().equals(user) &&
                (dsDate.compareTo(date1) >= 0 || isSameDay(dsDate, date1)) &&
                dsDate.compareTo(date2) <= 0)
                    intervalSessions.add(ds);
        }
        Collections.sort(intervalSessions, Collections.reverseOrder());
        return intervalSessions;
    }

    public ArrayList<DataSession> getSessionsByMonth(String user, Date date){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDateValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            if (ds.getUser().equals(user) &&
                sdf.format(dsDate).compareTo(sdf.format(date)) == 0)
                intervalSessions.add(ds);
        }

        return intervalSessions;
    }

    public ArrayList<DataSession> getSessionsByYear(String user, Date date){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDateValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

            if (ds.getUser().equals(user)&&
                sdf.format(dsDate).compareTo(sdf.format(date)) == 0)
                intervalSessions.add(ds);
        }

        Collections.sort(intervalSessions, Collections.reverseOrder());
        return intervalSessions;
    }

    private static Date buildDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    private static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

}
