package org.altervista.yagotzirck.corriforrest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        }
        return instance;
    }

    public void add(DataSession dataSession){ dataSessionsList.add(dataSession); }

    public boolean contains(DataSession dataSession){ return dataSessionsList.contains(dataSession); }


    public ArrayList<DataSession> getAllSessions(){ return dataSessionsList; }

    public ArrayList<DataSession> getSessionsByInterval(String user, Date date1, Date date2){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDateValue();
            if (dsDate.compareTo(date1) >= 0 &&
                dsDate.compareTo(date2) <= 0)
                    intervalSessions.add(ds);
        }

        return intervalSessions;
    }

    public ArrayList<DataSession> getSessionsByMonth(String user, Date date){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDateValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            if (sdf.format(dsDate).compareTo(sdf.format(date)) == 0)
                intervalSessions.add(ds);
        }

        return intervalSessions;
    }

    public ArrayList<DataSession> getSessionsByYear(String user, Date date){
        ArrayList<DataSession> intervalSessions = new ArrayList<>();

        for(DataSession ds: dataSessionsList) {
            Date dsDate = ds.getDateValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

            if (sdf.format(dsDate).compareTo(sdf.format(date)) == 0)
                intervalSessions.add(ds);
        }

        return intervalSessions;
    }





}
