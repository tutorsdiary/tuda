package com.tuda.teacher.calendar.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;

import com.tuda.teacher.calendar.callback.AsyncQueryCallback;
import com.tuda.teacher.calendar.model.entity.CalendarEventInstance;
import com.tuda.teacher.calendar.model.entity.CalendarEventItem;
import com.tuda.teacher.network.entity.CalendarEventItemForm;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class CalendarDaoManager {
    public static final String TAG = "CalendarManager";
    public static final int TOKEN_EVENT = 1 << 0;
    public static final int TOKEN_ATTENDEES = 1 << 1;
    public static final int TOKEN_REMINDERS = 1 << 2;
    public static final int TOKEN_CALENDARS = 1 << 3;
    public static final int TOKEN_COLORS = 1 << 4;

    public static final int TOKEN_ALL = TOKEN_EVENT | TOKEN_ATTENDEES | TOKEN_REMINDERS | TOKEN_CALENDARS | TOKEN_COLORS;
    public static final int TOKEN_UNINITIALIZED =  1 << 31;

    private Context mContext;

    private CalendarEventDao mEventDao;
    private CalendarDao mCalendarDao;
    private ContentResolver mContentResolver;

    private static CalendarDaoManager instance;

    public static CalendarDaoManager getInstance(Context context) {

        if (instance != null)
            return instance;

        instance = new CalendarDaoManager(context);

        return instance;
    }

    public CalendarDaoManager(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
        mCalendarDao = new CalendarDao(context, this);
        mEventDao = new CalendarEventDao(context, this);
    }

    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public Loader<Cursor> getCalendarLoader(int access_level) {
        return mCalendarDao.getCalendarLoader(mContext, null, access_level);
    }

    public Loader<Cursor> getCalendarLoader(Long id) {
        return mCalendarDao.getCalendarLoader(mContext, id, 0);
    }

    public Loader<Cursor> getEventLoader(Long eventId) {
        return mEventDao.getEventLoader(mContext, eventId);
    }

    public Loader<Cursor> getAttendeeLoader(Long id) {
        return mEventDao.getAttendeeLoader(mContext, id);
    }

    public Loader<Cursor> getReminderLoader(Long id) {
        return mEventDao.getReminderLoader(mContext, id);
    }

    public Loader<Cursor> getColorLoader() {
        return getColorLoader(null);
    }

    public Loader<Cursor> getColorLoader(String accountName) {
        return mEventDao.getColorLoader(mContext, accountName);
    }

    public CalendarEventDao getEventDao() {
        return mEventDao;
    }

    public CalendarDao getCalendarDao() {
        return mCalendarDao;
    }

    public void getCalendarEventInstances(DateTime startDate, int days, AsyncQueryCallback<List<CalendarEventInstance>> callback) {
        mEventDao.getCalendarEventInstances(startDate, days, callback);
    }

    public void createEventItemFrom(Cursor cursor, CalendarEventItem item) {
        mEventDao.createEventItemFrom(cursor, item);
    }

    public void createEventItemFrom(CalendarEventItemForm form, CalendarEventItem item) {
        mEventDao.createEventItemFrom(form, item);
    }

}
