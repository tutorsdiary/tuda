package com.tuda.teacher.calendar.model;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.property.DateProperty;
import net.fortuna.ical4j.util.TimeZones;

import java.io.Serializable;

/**
 * Created by crazyluv on 2016. 2. 24..
 */
public class CalendarBase implements Serializable {
    private static final long serialVersionUID = 2251105679562709058L;

    protected String title;
    protected int displayColor;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDisplayColor() {
        return this.displayColor;
    }

    public void setDisplayColor(int color) {
        this.displayColor = color;
    }

    protected static String getTzId(DateProperty date) {
        if (isDateTime(date) && !date.isUtc() && date.getTimeZone() != null)
            return date.getTimeZone().getID();
        else
            return TimeZones.UTC_ID;
    }

    protected static boolean isDateTime(DateProperty date) {
        return date.getDate() instanceof DateTime;
    }

}
