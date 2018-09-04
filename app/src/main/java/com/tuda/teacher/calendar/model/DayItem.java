package com.tuda.teacher.calendar.model;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by crazyluv on 2015. 11. 24..
 */
public class DayItem {
    private static final String TAG = "DayItem";
    private int position;
    private DateTime date;
    private DateTime mCalendarMonth;
    private ArrayList<CalendarBase> calendar_event = new ArrayList();

    private boolean hasHoliday = false;

    public DayItem(Calendar cal, int calendar_year, int calendar_month) {
        setTime(new DateTime(cal));
        setCurMonth(calendar_year, calendar_month);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setTime(DateTime date) {
        this.date = date;
    }

    public int getYear() {
        return this.date.getYear();
    }

    public int getMonthOfYear() {
        return this.date.getMonthOfYear();
    }

    public int getDayOfMonth() {
        return this.date.getDayOfMonth();
    }

    public void setCurMonth(int year, int month) {
        this.mCalendarMonth = new DateTime(year, month, 1, 0, 0, 0);
    }

    public DateTime getDateTime() {
        return this.date;
    }

    public Boolean isToday() {
        LocalDate today = new LocalDate();

        return today.isEqual(date.toLocalDate());
    }

    public Boolean isCurMonth() {
        return getYear() == mCalendarMonth.getYear()
                && getMonthOfYear() == mCalendarMonth.getMonthOfYear();
    }

    public void clearCalendarEvent() {
        synchronized (this.calendar_event) {
            this.calendar_event.clear();
            this.hasHoliday = false;
        }
    }

    public boolean hasHoliday() {
        return this.hasHoliday;
    }

    public void setHasHoliday() {
        this.hasHoliday = true;
    }

    public void addCalendarEvent(CalendarBase calendar_event) {
        synchronized (this.calendar_event) {
            this.calendar_event.add(calendar_event);
        }
    }

    public List<CalendarBase> getCalendarEvent() {
        return this.calendar_event;
    }

    public boolean hasEvent() {
        return this.calendar_event.size() > 0 ? true : false;
    }

    public CalendarBase getCalendarEvent(int index) {
        if (index < 0 || this.calendar_event.size()-1 < index)
            throw new ArrayIndexOutOfBoundsException();

        return this.calendar_event.get(index);
    }

}
