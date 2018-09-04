package com.tuda.teacher.calendar.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.EntityIterator;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.tuda.teacher.util.Log;
import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.calendar.AsyncQueryService;
import com.tuda.teacher.calendar.model.entity.CalendarItem;
import com.tuda.teacher.common.Preference;

/**
 * Created by crazyluv on 2015. 12. 14.
 */
public class CalendarDao extends BaseDao {
    public static final String TUDA_ACCOUNT_NAME = "TUDA Calendar";

    public static final String[] CALENDAR_PROJECTION = new String[]{
            Calendars._ID,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.OWNER_ACCOUNT,
            Calendars.CALENDAR_COLOR,
            Calendars.CAN_ORGANIZER_RESPOND,
            Calendars.CALENDAR_ACCESS_LEVEL,
            Calendars.VISIBLE,
            Calendars.MAX_REMINDERS,
            Calendars.ALLOWED_REMINDERS,
            Calendars.ALLOWED_ATTENDEE_TYPES,
            Calendars.ALLOWED_AVAILABILITY,
            Calendars.ACCOUNT_NAME,
            Calendars.ACCOUNT_TYPE,

            Calendars.NAME,
            Calendars.CALENDAR_LOCATION,
            Calendars.DEFAULT_SORT_ORDER,
            Calendars.CALENDAR_TIME_ZONE,
            Calendars.SYNC_EVENTS,
            Calendars.DELETED,
    };

    private Context mContext;
    private CalendarDaoManager mManager;
    private ContentResolver mContentResolver;

    public static final String SELECTION = CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.DELETED + " = 0 ";
    public static final String SELECTION2 = CalendarContract.Calendars.DELETED + " = 0 ";

    public CalendarDao(Context context, CalendarDaoManager manager) {
        mContext = context;
        mManager = manager;
        mContentResolver = manager.getContentResolver();

        getCalendars();
    }

    public Loader<Cursor> getCalendarLoader(Context context) {
        return getCalendarLoader(context, null, 0);
    }

    public Loader<Cursor> getCalendarLoader(Context context, Long id, int access_level) {
        String selectionWhere = "";
        if (id != null && id > 0)
            selectionWhere = Calendars._ID + " = " + id + " AND " + SELECTION2
                    + " AND " + Calendars.CALENDAR_ACCESS_LEVEL + " >= " + access_level;
        else
            selectionWhere = "( " + SELECTION + " OR " + Calendars.ACCOUNT_NAME + " = \"" + TUDA_ACCOUNT_NAME + "\"  ) "
                    + " AND " + Calendars.CALENDAR_ACCESS_LEVEL + " >= " + access_level;


        return new CursorLoader(context, CalendarContract.CalendarEntity.CONTENT_URI, CalendarDao.CALENDAR_PROJECTION, selectionWhere, null, null);
    }

    public void getCalendars() {

        @SuppressLint("MissingPermission")
        Cursor calCursor = mContentResolver.query(CalendarContract.CalendarEntity.CONTENT_URI, CALENDAR_PROJECTION,
                "(" + SELECTION + " OR " + Calendars.ACCOUNT_NAME + " = \"" + TUDA_ACCOUNT_NAME + "\" ) ", null, null);

        EntityIterator ei = android.provider.CalendarContract.CalendarEntity.newEntityIterator(calCursor);

        try {
            while (ei.hasNext()) {

                ContentValues values = ei.next().getEntityValues();
                CalendarItem calendarItem = new CalendarItem(values);

                long calendarId = values.getAsLong(Calendars._ID);

                if (BuildConfig.DEBUG) {
                    Log.d(TAG + "_" + calendarId, "================================");
                    for (String key : values.keySet()) {
                        Log.d(TAG + "_" + calendarId, key + ": " + values.get(key));
                    }
                }

                if (calendarItem.getAccountName().equals(TUDA_ACCOUNT_NAME)) {
                    if (Preference.getDefaultSyncCalendarId() == -999L)
                        Preference.setDefaultSyncCalendarId(calendarId);

                    if (calendarItem.getCalendarColor() > 0)
                        setChangeCalendarColor(calendarId);
                }

            }

        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "캘린더 계정이 존재하지 않아요.");
        }
    }

    private void setChangeCalendarColor(Long calendarId) {
        AsyncQueryService mService = new AsyncQueryService(mContext);
        int mUpdateToken = mService.getNextToken();
        Uri uri = ContentUris.withAppendedId(Calendars.CONTENT_URI, calendarId);
        ContentValues values = new ContentValues();
        // Toggle the current setting
        values.put(Calendars.CALENDAR_COLOR, Color.parseColor("#4b77be"));
        mService.startUpdate(mUpdateToken, null, uri, values, null, null, 0);
    }

}
