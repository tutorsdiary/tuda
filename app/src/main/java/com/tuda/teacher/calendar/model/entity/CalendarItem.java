package com.tuda.teacher.calendar.model.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.CalendarContract.Calendars;

import com.tuda.teacher.calendar.util.CalendarEventUtils;
import com.tuda.teacher.network.entity.CalendarItemForm;

import net.fortuna.ical4j.model.TimeZone;

import java.io.Serializable;

/**
 * Created by crazyluv on 2015. 11. 25..
 *
 * ContentValues의 값을 Class로 변환하여 사용.
 *
 */
public class CalendarItem implements Serializable {

    private static final long serialVersionUID = 6677134715287561915L;

    private long _id = -1;
    private String name;
    private String accountName;
    private String accountType;

    private String calendarDisplayName;
    private String calendarTimezone;
    private int calendarColor;
    private boolean initCalendarColor = false;
    private boolean visibility = false;

    private String ownerAccount;

    private int calendarAccessLevel = Calendars.CAL_ACCESS_CONTRIBUTOR;
    private boolean canOrganizerRespond = false;
    private String allowedAttendeesType;
    private String allowedReminders;
    private int maxReminders;
    private String allowedAvailability;

    public CalendarItem() {
        this.name = "TUDA CALENDAR";
        this.calendarDisplayName = this.name;
        this.calendarTimezone = TimeZone.getDefault().getID();
        this.calendarColor = -382764;
        this.accountType = null;
        this.accountName = null;
        this.ownerAccount = null;
    }

    public CalendarItem(CalendarItemForm item) {
        this._id = item.getId();
        this.name = item.getName();
        this.calendarDisplayName = item.getName();
        this.calendarTimezone = item.getTimezone();
        this.calendarColor = item.getColor();
        this.calendarAccessLevel = item.getAccessLevel();
        this.accountType = item.getAccountType();
        this.accountName = item.getName();
        this.ownerAccount = item.getOwnerAccount();

        this.maxReminders = item.getMaxReminders();
        this.allowedReminders = item.getAllowedReminders();
        this.allowedAttendeesType = item.getAllowedAttendeesType();
        this.allowedAvailability = item.getAllowedAvailability();
        this.canOrganizerRespond = item.canOrganizerRespond();
    }

    public CalendarItem(ContentValues values) {
        _id = values.getAsLong(Calendars._ID);
        name = values.getAsString(Calendars.NAME);
        calendarTimezone = values.getAsString(Calendars.CALENDAR_TIME_ZONE);
        ownerAccount = values.getAsString(Calendars.OWNER_ACCOUNT);

        canOrganizerRespond = values.getAsBoolean(Calendars.CAN_ORGANIZER_RESPOND);
        calendarAccessLevel = values.getAsInteger(Calendars.CALENDAR_ACCESS_LEVEL);
        calendarDisplayName = values.getAsString(Calendars.CALENDAR_DISPLAY_NAME);
        setCalendarColor(CalendarEventUtils.getDisplayColorFromColor(values.getAsInteger(Calendars.CALENDAR_COLOR)));
        accountName = values.getAsString(Calendars.ACCOUNT_NAME) ;
        accountType = values.getAsString(Calendars.ACCOUNT_TYPE);

        maxReminders = values.getAsInteger(Calendars.MAX_REMINDERS);
        allowedReminders = values.getAsString(Calendars.ALLOWED_REMINDERS);
        allowedAttendeesType = values.getAsString(Calendars.ALLOWED_ATTENDEE_TYPES);
        allowedAvailability = values.getAsString(Calendars.ALLOWED_AVAILABILITY);
    }

    public CalendarItem(Cursor cursor) {

        int mIdColumn = cursor.getColumnIndexOrThrow(Calendars._ID);
        int mNameColumn = cursor.getColumnIndexOrThrow(Calendars.CALENDAR_DISPLAY_NAME);
        int mColorColumn = cursor.getColumnIndexOrThrow(Calendars.CALENDAR_COLOR);
        int mVisibleColumn = cursor.getColumnIndexOrThrow(Calendars.VISIBLE);
        int mOwnerAccountColumn = cursor.getColumnIndexOrThrow(Calendars.OWNER_ACCOUNT);
        int mAccountNameColumn = cursor.getColumnIndexOrThrow(Calendars.ACCOUNT_NAME);
        int mAccountTypeColumn = cursor.getColumnIndexOrThrow(Calendars.ACCOUNT_TYPE);

        _id = cursor.getLong(mIdColumn);
        calendarDisplayName = cursor.getString(mNameColumn);
        ownerAccount = cursor.getString(mOwnerAccountColumn);
        setCalendarColor(CalendarEventUtils.getDisplayColorFromColor(cursor.getInt(mColorColumn)));

        visibility = cursor.getInt(mVisibleColumn) != 1 ? false : true;

        accountName = cursor.getString(mAccountNameColumn) ;
        accountType = cursor.getString(mAccountTypeColumn);

    }

    public Long getCalenarId() {
        return _id;
    }
    public String getCalenarName() {
        return name;
    }
    public String getCalenarDisplayName() {
        return calendarDisplayName;
    }
    public String getAccountName() {
        return accountName;
    }
    public int getCalendarColor() {
        return calendarColor;
    }
    public void setCalendarColor(int color) {
        this.calendarColor = color;
        this.initCalendarColor = true;
    }

    public boolean getInitCalendarColor() {
        return initCalendarColor;
    }
    public String getOwnerAccount() {
        return ownerAccount;
    }
    public String getAccountType() {
        return accountType;
    }
    public int getAccessLevel() {
        return calendarAccessLevel;
    }

    public boolean getCanOrganizerRespond() {
        return canOrganizerRespond;
    }

    public String getAllowedAttendeesType() {
        return allowedAttendeesType;
    }

    public String getAllowedReminders() {
        return allowedReminders;
    }

    public int getMaxReminders() {
        return maxReminders;
    }

    public String getAllowedAvailability() {
        return allowedAvailability;
    }

    public boolean isLegalHoliday() {

        if ("legalHoliday".equalsIgnoreCase(getCalenarName()) ||
                "legalSubstHoliday".equalsIgnoreCase(getCalenarName()))
            return true;

        return false;

    }

    public boolean getVisibility() {
        return this.visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility != 1 ? false : true;
    }
}
