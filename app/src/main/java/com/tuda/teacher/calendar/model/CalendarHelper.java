package com.tuda.teacher.calendar.model;

import android.content.Context;
import android.provider.CalendarContract.Calendars;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class CalendarHelper extends EventSaveHelper {

    private static final String TAG = "Calendar";
    private static final String IS_PRIMARY = "\"primary\"";
    private static final String SELECTION = Calendars.SYNC_EVENTS + "=?";
    private static final String[] SELECTION_ARGS = new String[] {"1"};

    private static final String[] PROJECTION = new String[] {
            Calendars._ID,
            Calendars.ACCOUNT_NAME,
            Calendars.ACCOUNT_TYPE,
            Calendars.OWNER_ACCOUNT,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.CALENDAR_COLOR,
            Calendars.VISIBLE,
            Calendars.SYNC_EVENTS,
            "(" + Calendars.ACCOUNT_NAME + "=" + Calendars.OWNER_ACCOUNT + ") AS " + IS_PRIMARY,
    };


    public CalendarHelper(Context context) {
        super(context);
    }


}
