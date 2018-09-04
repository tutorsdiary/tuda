package com.tuda.teacher.calendar.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by crazyluv on 2015. 12. 7..
 */
public class WeekItem {
    private static final String TAG = "WeekItem";
    private DateTime  week_start_date;
    private ArrayList<DayItem> dayItems = new ArrayList<DayItem>();

    public WeekItem(Calendar cal, int calendar_year, int calendar_month) {

        this.week_start_date = new DateTime(cal);

        for(int i=0; i<7; i++) {

            dayItems.add(new DayItem(cal, calendar_year, calendar_month));

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    public int getYear() {
        return week_start_date.getYear();
    }

    public int getMonthOfYear() {
        return week_start_date.getMonthOfYear();
    }

    public int getDayOfMonth() {
        return week_start_date.getDayOfMonth();
    }

    public DayItem getItem(int position) {
        return dayItems.get(position);
    }

    public List<DayItem> getItems() {
        return dayItems;
    }

    public int getCount() {
        if (dayItems == null)
            return 0;

        return dayItems.size();
    }

    public void addItem(DayItem item) {
        dayItems.add(item);
    }

    public void clearItem() {
        dayItems.clear();
    }

}
