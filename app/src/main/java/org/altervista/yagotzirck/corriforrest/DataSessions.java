package org.altervista.yagotzirck.corriforrest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class DataSessions {

    private static DataSessions instance = null;

    private static ArrayList<DataSession> dataSessionsList;

    private DataSessions(){}

    public static DataSessions getInstance() {
        if (instance == null) {
            instance = new DataSessions();
            dataSessionsList = new ArrayList<>();

            dataSessionsList.add(new DataSession("john", buildDate(12, 2, 2021), 1000, 2000));
            dataSessionsList.add(new DataSession("john", buildDate(13, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("john", buildDate(14, 2, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("john", buildDate(24, 8, 2021), 1000, 4500));
            dataSessionsList.add(new DataSession("john", buildDate(25, 8, 2021), 2000, 3500));
            dataSessionsList.add(new DataSession("john", buildDate(26, 8, 2021), 1500, 2800));
            dataSessionsList.add(new DataSession("john", buildDate(28, 8, 2021), 800,  1500));
            dataSessionsList.add(new DataSession("john", buildDate(1, 9, 2021), 2300, 4500));
            dataSessionsList.add(new DataSession("john", buildDate(2, 9, 2021), 1800, 2900));
        }
        return instance;
    }

    public void add(DataSession dataSession){ dataSessionsList.add(dataSession); }
    public void remove(DataSession dataSession){ dataSessionsList.remove(dataSession); }

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
            Date dsDate = ds.getDate();
            if (ds.getUser().equals(user) &&
                (dsDate.compareTo(date1) >= 0 || isSameDay(dsDate, date1)) &&
                dsDate.compareTo(date2) <= 0)
                    intervalSessions.add(ds);
        }
        Collections.sort(intervalSessions, Collections.reverseOrder());
        return intervalSessions;
    }

    public ArrayList<DataSession> getSessionsByDay(String user, Date date){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDate();

            if (ds.getUser().equals(user) &&
                    isSameDay(dsDate, date))
                intervalSessions.add(ds);
        }

        Collections.sort(intervalSessions, Collections.reverseOrder());
        return intervalSessions;
    }

    public ArrayList<DataSession> getSessionsByMonth(String user, Date date){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            if (ds.getUser().equals(user) &&
                sdf.format(dsDate).compareTo(sdf.format(date)) == 0)
                intervalSessions.add(ds);
        }

        Collections.sort(intervalSessions, Collections.reverseOrder());
        return intervalSessions;
    }

    public ArrayList<DataSession> getSessionsByYear(String user, Date date){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDate();
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
