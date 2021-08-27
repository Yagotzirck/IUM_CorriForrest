package org.altervista.yagotzirck.corriforrest;

import java.util.Date;

public interface DatePickersCallbacks {
    void dayCallback(Date date);
    void monthYearCallback(Date monthYearDate);
    void yearCallback(Date yearDate);
}
