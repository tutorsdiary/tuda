package com.tuda.teacher.calendar.model.entity;

import android.content.ContentValues;
import android.provider.CalendarContract;

import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.util.Log;

import java.io.Serializable;

/**
 * Created by crazyluv on 2015. 11. 25..
 */
public class CalendarReminder implements Serializable, Cloneable, Comparable<CalendarReminder> {

    private static final String TAG = "CalendarReminder";
    private static final long serialVersionUID = -2602827696079689480L;

    protected int srl;
    protected long event_id;
    protected int minutes;
    protected int method;

    public CalendarReminder() {}
    public CalendarReminder(int minutes) {
        this(minutes, CalendarContract.Reminders.METHOD_ALERT);
    }
    public CalendarReminder(int minutes, int method) {
        this.minutes = minutes;
        if ( method >= CalendarContract.Reminders.METHOD_DEFAULT && method <= CalendarContract.Reminders.METHOD_ALARM)
            this.method = method;
        else
            this.method = CalendarContract.Reminders.METHOD_ALERT;
    }
    public CalendarReminder(ContentValues values) {
        this.srl = values.getAsInteger(CalendarContract.Reminders._ID);
        this.event_id = values.getAsLong(CalendarContract.Reminders.EVENT_ID);
        this.minutes = values.getAsInteger(CalendarContract.Reminders.MINUTES);
        this.method = values.getAsInteger(CalendarContract.Reminders.METHOD);
    }

    public void setSrl(int srl) {
        this.srl = srl;
    }

    public int getSrl() {
        return this.srl;
    }

    public void setEventId(long eventId) {
        this.event_id = eventId;
    }

    public long getEventId() {
        return this.event_id;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Integer getMinutes() {
        return this.minutes;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public Integer getMethod() {
        return this.method;
    }

    public ContentValues toContentValues(long eventID) {

        ContentValues values = new ContentValues();

        values.put(CalendarContract.Reminders._ID, getSrl());
        if (eventID != -1)
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        values.put(CalendarContract.Reminders.MINUTES, getMinutes());
        values.put(CalendarContract.Reminders.METHOD, getMethod());

        if (BuildConfig.DEBUG)
            for(String key: values.keySet())
                Log.d(TAG, key + ": " + values.get(key));

        return values;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        CalendarReminder obj = (CalendarReminder) super.clone();
        obj.srl = srl;
        obj.event_id = event_id;
        obj.minutes = minutes;
        obj.method = method;
        return obj;
    }

    @Override
    public int compareTo(CalendarReminder another) {
        if (another.getMinutes() != getMinutes())
            return getMinutes() - another.getMinutes();

        return 0;
    }
}