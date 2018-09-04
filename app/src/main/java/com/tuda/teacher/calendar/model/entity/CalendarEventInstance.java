package com.tuda.teacher.calendar.model.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.CalendarContract;

import com.tuda.teacher.calendar.model.CalendarBase;
import com.tuda.teacher.calendar.util.Utils;
import com.tuda.teacher.calendar.model.CalendarEventDao;
import com.tuda.teacher.network.entity.CalendarEventInstanceForServer;

import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazyluv on 2015. 11. 25..
 */
public class CalendarEventInstance extends CalendarBase {

    protected static final String TAG = "CalendarEventInstance";

    protected long _id;
    protected String eventLocation;
    protected boolean allDay;
    protected boolean displayAllDay;
    protected String eventTimezone;
    protected long event_id;
    protected long calendar_id;

    protected long begin;
    protected long end;

    protected int startDay;
    protected int endDay;
    protected int startMinute;
    protected int endMinute;

    protected boolean hasAlarm;
    protected String rrule;
    protected String rdate;

    protected int selfAttendeeStatus;
    protected String organizer;
    protected boolean guestsCanModify;
    protected int guestsCanSeeGuests;
    protected int guestsCanInviteOthers;

    protected String calendarDisplayName;

    protected long originalInstanceTime;

    protected List<CalendarAttendee> attendees = new ArrayList<>();
    protected List<CalendarReminder> reminders = new ArrayList<>();
    protected List<CalendarExtendedProp> extendedProperties = new ArrayList<>();

    private static final String NO_EVENT_COLOR = "";
    private boolean isServer = false;

    public CalendarEventInstance() { }
    public CalendarEventInstance(CalendarEventInstanceForServer instance) {
        _id = instance.getId();
        title = instance.getTitle();
        eventLocation = instance.getEventLocation();
        allDay = instance.getAllDay();
        eventTimezone = instance.getEventTimezone();
//        displayAllDay = instance.getDiaplayAllDay();
//        displayColor = instance.getDisplayColor();
        event_id = instance.getEventId();
        begin = instance.getBegin();
        end = instance.getEnd();

//        startDay = instance.getStartDate();
//        endDay = instance.getEndDate();

        startMinute = instance.getStartMinute();
        endMinute = instance.getEndMinute();

//        hasAlarm = instance.getHasAlarm();
//        rrule = instance.getRrule();
        rdate = instance.getRdate();

//        selfAttendeeStatus = instance.getSelfAttendeeStatus();
        organizer = instance.getOrganizer();
        guestsCanModify = instance.getGuestsCanModify();
        guestsCanSeeGuests = instance.getGuestsCanSeeGuests()?1:0;
        guestsCanInviteOthers = instance.getGuestsCanInviteOthers()?1:0;
//        calendar_id =
//        calendarDisplayName =
//        originalInstanceTime = instance.getOriginalInstanceTIme();

        isServer = true;

    }
    public CalendarEventInstance(Cursor cursor) {
        _id = cursor.getLong(CalendarEventDao.INSTANCES_INDEX__ID);
        title = cursor.getString(CalendarEventDao.INSTANCES_INDEX_TITLE);
        eventLocation = cursor.getString(CalendarEventDao.INSTANCES_INDEX_EVENT_LOCATION);
        allDay = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_ALL_DAY) != 0;
        eventTimezone = cursor.getString(CalendarEventDao.INSTANCES_INDEX_EVENT_TIMEZONE);
        displayAllDay = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_DISPLAY_ALL_DAY) != 0;
        displayColor = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_DISPLAY_COLOR);
        event_id = cursor.getLong(CalendarEventDao.INSTANCES_INDEX_EVENT_ID);
        begin = cursor.getLong(CalendarEventDao.INSTANCES_INDEX_BEGIN);
        end = cursor.getLong(CalendarEventDao.INSTANCES_INDEX_END);

        startDay = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_START_DAY);
        endDay = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_END_DAY);

        startMinute = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_START_MINUTE);
        endMinute = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_END_MINUTE);

        hasAlarm = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_HAS_ALARM) != 0;
        rrule = cursor.getString(CalendarEventDao.INSTANCES_INDEX_RRULE);
        rdate = cursor.getString(CalendarEventDao.INSTANCES_INDEX_RDATE);

        selfAttendeeStatus = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_SELF_ATTENDEE_STATUS);
        organizer = cursor.getString(CalendarEventDao.INSTANCES_INDEX_ORGANIZER);
        guestsCanModify = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_GUESTS_CAN_MODIFY) != 0;
        guestsCanSeeGuests = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_GUESTS_CAN_SEE_GUESTS);
        guestsCanInviteOthers = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_GUESTS_CAN_INVITE_OTHERS);
        calendar_id = cursor.getInt(CalendarEventDao.INSTANCES_INDEX_CALENDAR_ID);
        calendarDisplayName = cursor.getString(CalendarEventDao.INSTANCES_INDEX_CALENDAR_DISPLAY_NAME);
        originalInstanceTime = cursor.getLong(CalendarEventDao.INSTANCES_INDEX_ORIGINAL_INSTANCE_TIME);

    }

    public CalendarEventInstance(ContentValues eventValues) {
        _id = eventValues.getAsLong(CalendarContract.Instances._ID);
        event_id = eventValues.getAsLong(CalendarContract.Instances.EVENT_ID);
        calendar_id = eventValues.getAsLong(CalendarContract.Instances.CALENDAR_ID);
        title = eventValues.getAsString(CalendarContract.Instances.TITLE);
        eventLocation = eventValues.getAsString(CalendarContract.Instances.EVENT_LOCATION);
        allDay = eventValues.getAsInteger(CalendarContract.Instances.ALL_DAY) != 0;
        displayAllDay = eventValues.getAsInteger(CalendarEventDao.DISPLAY_ALL_DAY) != 0;
        eventTimezone = eventValues.getAsString(CalendarContract.Instances.EVENT_TIMEZONE);
        displayColor = eventValues.getAsInteger(CalendarContract.Instances.DISPLAY_COLOR);
        event_id = eventValues.getAsLong(CalendarContract.Instances.EVENT_ID);
        begin = eventValues.getAsLong(CalendarContract.Instances.BEGIN);
        end = eventValues.getAsLong(CalendarContract.Instances.END);

        startDay = eventValues.getAsInteger(CalendarContract.Instances.START_DAY);
        endDay = eventValues.getAsInteger(CalendarContract.Instances.END_DAY);

        startMinute = eventValues.getAsInteger(CalendarContract.Instances.START_MINUTE);
        endMinute = eventValues.getAsInteger(CalendarContract.Instances.END_MINUTE);

        hasAlarm = eventValues.getAsInteger(CalendarContract.Instances.HAS_ALARM) != 0;
        rrule = eventValues.getAsString(CalendarContract.Instances.RRULE);
        rdate = eventValues.getAsString(CalendarContract.Instances.RDATE);

        selfAttendeeStatus = eventValues.getAsInteger(CalendarContract.Instances.SELF_ATTENDEE_STATUS);
        organizer = eventValues.getAsString(CalendarContract.Instances.ORGANIZER);
        guestsCanModify = eventValues.getAsInteger(CalendarContract.Instances.GUESTS_CAN_MODIFY) != 0;
        guestsCanSeeGuests = eventValues.getAsInteger(CalendarContract.Instances.GUESTS_CAN_SEE_GUESTS);
        guestsCanInviteOthers = eventValues.getAsInteger(CalendarContract.Instances.GUESTS_CAN_INVITE_OTHERS);

        calendarDisplayName = eventValues.getAsString(CalendarContract.Instances.CALENDAR_DISPLAY_NAME);
        originalInstanceTime = eventValues.getAsLong(CalendarContract.Instances.ORIGINAL_INSTANCE_TIME);

    }

    public Long getId() {
        return this._id;
    }

    public void setId(Long id) {
        this._id = id;
    }

    public long getEventId() {
        return this.event_id;
    }

    public void setEventId(long id) {
        this.event_id = id;
    }

    public Long getCalendarId() {
        return this.calendar_id;
    }

    public void setCalendarId(Long id) {
        this.calendar_id = id;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getEventLocation() {
        return this.eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Date convertTimeToDateTime(long time, String tzId) {
        String tId = getEventTimezone();
        if (tzId == null) {
            Date datetime = new Date(time);
            return datetime;
        } else {
            DateTime datetime = new DateTime(time);
            datetime.setTimeZone(Utils.tzRegistry.getTimeZone(tId));
            return datetime;
        }
    }

    public String getEventTimezone() {
        return this.eventTimezone;
    }

    public void setEventTimezone(String timezone) {
        this.eventTimezone = timezone;
    }

    public boolean getAllDay() {
        return this.allDay;
    }

    public boolean getDisplayAllDay() {
        return this.displayAllDay;
    }

    public void setAllDay(Integer allday) {
        this.allDay = allday != 0;
    }

    public boolean getGuestsCanModify() {
        return this.guestsCanModify;
    }

    public String getRRule() {
        return this.rrule;
    }

    public void setRRule(String rrule) {
        this.rrule = rrule;
    }

    public String getRdate() {
        return this.rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public List<CalendarAttendee> getAttendee() {
        return this.attendees;
    }

    public void addAttendee(CalendarAttendee attendee) {
        this.attendees.add(attendee);
    }

    public List<CalendarReminder> getReminder() {
        return this.reminders;
    }

    public void addReminder(CalendarReminder attendee) {
        this.reminders.add(attendee);
    }

    public List<CalendarExtendedProp> getExtendedProps() {
        return this.extendedProperties;
    }

    public void addExtendedProperties(CalendarExtendedProp extendedProperties) {
        this.extendedProperties.add(extendedProperties);
    }

    public LocalDate getStartDay() {
        return new LocalDate(startDay);
    }

    public int getIntStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public LocalDate getEndDay() {
        return new LocalDate(endDay);
    }

    public int getIntEndDay() {
        return endDay;
    }

    public LocalTime getStartTime() {
        return new LocalTime(0, 0).plusMinutes(this.startMinute);
    }

    public void setStartTime(int startMinute) {
        this.startMinute = startMinute;
    }

    public LocalTime getEndTime() {
        return new LocalTime(0, 0).plusMinutes(this.endMinute);
    }

    public long getBegin() {
        return this.begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getCalendarDisplayName() {
        return this.calendarDisplayName;
    }

    public String toString() {
        return super.toString();
    }

    public boolean isServer() {
        return this.isServer;
    }
}
