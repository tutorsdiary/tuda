package com.tuda.teacher.calendar.model.entity;

import android.content.ContentValues;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import android.text.format.Time;

import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.calendar.model.CalendarBase;
import com.tuda.teacher.calendar.util.Utils;
import com.tuda.teacher.calendar.EventColorCache;
import com.tuda.teacher.calendar.EventRecurrence;
import com.tuda.teacher.util.Log;

import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by crazyluv on 2015. 11. 25..
 */
public class CalendarEventItem extends CalendarBase implements Cloneable {

    private static final long serialVersionUID = -7959324871188127229L;
    public static final int ACCESS_LEVEL_NONE = 0;
    public static final int ACCESS_LEVEL_DELETE = 1;
    public static final int ACCESS_LEVEL_EDIT = 2;

    public final static String EVENT_TYPE_LESSON = "L";
    public final static String EVENT_TYPE_NORMAL = "N";

    protected static final String TAG = "CalendarEventItem";

    protected long calendar_id = -1L;

    protected long _id = -1L;
    protected long instance_id = -1L;
    protected long original_id = -1L;
    protected long original_instance_time;
    protected String sync_id;
    protected String original_sync_id;
    protected String organizer;
    protected String ownerAccount;
    protected String title;
    protected long dtstart;
    protected long dtend;
    protected long originalDtstart;
    protected long originalDtend;
    protected String eventLocation;
    protected String description;
    protected int eventColor = -1;
    protected int calendarColor = -1;
    protected EventColorCache mEventColorCache;
    protected boolean initEventColor = false;
    protected boolean initCalenarColor = false;
    protected String eventTimezone;
    protected boolean original_allDay;
    protected boolean allDay;
    protected int availability = Events.AVAILABILITY_BUSY;
    protected int accessLevel = 0;

    protected int eventStatus = Events.STATUS_CONFIRMED;

    protected boolean guestsCanModify = false;
    protected boolean guestsCanInviteOthers = false;
    protected boolean guestsCanSeeGuests = false;

    protected String rrule;
    protected String rdate;
    protected String exdate;
    protected String duration;

    protected String eventType;

    protected String mUri;

    protected int maxReminders;
    protected String allowedReminders;

    protected boolean hasAttendeeData = true;
    protected boolean isOrganizer = true;

    protected boolean isFirstEventInSeries = true;

    protected ArrayList<CalendarAttendee> attendees = new ArrayList<>();
    protected ArrayList<CalendarReminder> reminders = new ArrayList<>();
    protected ArrayList<CalendarExtendedProp> extendedProperties = new ArrayList<>();

    private static final String NO_EVENT_COLOR = "";
    private boolean hasAlarm = false;
    private String organizerDisplayName;
    private int ownerAttendeeId = -1;
    private int selfAttendeeStatus = -1;
    private String accountType;
    private boolean hasExtendedProperties;

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return this.accountType;
    }

    private CalendarItem calendarItem = new CalendarItem();

    public CalendarEventItem() { }

    public long getId() {
        return this._id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public long getInstanceId() {
        return this.instance_id;
    }

    public void setInstnaceId(long id) {
        this.instance_id = id;
    }

    public String getUri() {
        return this.mUri;
    }

    public void setUri(String uri) {
        this.mUri = uri;
    }

    public long getCalendarId() {
        return this.calendar_id;
    }

    public void setCalendarId(long calendar_id) {
        this.calendar_id = calendar_id;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setIsOrganizer(boolean isOrganizer) {
        this.isOrganizer = isOrganizer;
    }

    public boolean getIsOrganizer() {
        return this.isOrganizer;
    }

    public String getOwnerAccount() {
        return this.ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventLocation() {
        return this.eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public int getCalendarColor() {
        if (calendarItem == null)
            return -1;
        return calendarItem.getCalendarColor();
    }

    public boolean isInitCalendarColor() {
        if (calendarItem == null)
            return false;
        return calendarItem.getInitCalendarColor();
    }

    public int getEventColor() {
        return this.eventColor;
    }

    public int[] getCalendarEventColors() {
        if (mEventColorCache != null)
            return mEventColorCache.getColorArray(getCalendarItem().getAccountName(), getCalendarItem().getAccountType());
        return null;
    }

    public void setCalendarItem(CalendarItem item) {
        this.calendarItem = item;
    }

    public CalendarItem getCalendarItem() {
        return this.calendarItem;
    }

    public void setEventColor(int color) {
        this.eventColor = color;
        this.initEventColor = true;
    }

    public int getEventColorKey() {
        if (mEventColorCache != null) {
            return mEventColorCache.getColorKey(getCalendarItem().getAccountName(), getCalendarItem().getAccountType(), getEventColor());
        }
        return -1;
    }

    public boolean isInitEventColor() {
        return this.initEventColor;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDtStart() {
        return this.dtstart;
    }

    public void setDtStart(long start_date) {
        this.dtstart = start_date;
    }

    public long getOriginalDtstart() {
        return this.originalDtstart;
    }

    public void setOriginalDtStart(long start_date) {
        this.originalDtstart = start_date;
    }

    public long getDtEnd() {
        return this.dtend;
    }

    public void setDtEnd(long end_date) {
        this.dtend = end_date;
    }

    public long getOriginalDtEnd() {
        return this.originalDtend;
    }

    public void setOriginalDtEnd(long end_date) {
        this.originalDtend = end_date;
    }

    public String getEventTimezone() {
        return this.eventTimezone;
    }

    public void setEventTimezone(String timezone) {
        this.eventTimezone = timezone;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String dur) {
        this.duration = dur;
    }

    public boolean getAllDay() {
        return this.allDay;
    }

    public void setAllDay(Integer allday) {
        this.allDay = allday != 0;
    }

    public boolean getHasAttendeeData() {
        return this.hasAttendeeData;
    }

    public void setHasAttendeeData(Integer hasAttendeeData) {
        this.hasAttendeeData = hasAttendeeData != 0;
    }

    public Integer getAvailability() {
        return this.availability;
    }

    public void setAvaliavility(int availability) {
        this.availability = availability;
    }

    public Integer getAccessLevel() {
        return this.accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Integer getEventStatus() {
        return this.eventStatus;
    }

    public void setEventStatus(int eventStatus) {
        this.eventStatus = eventStatus;
    }

    public boolean getGuestsCanModify() {
        return this.guestsCanModify;
    }

    public boolean getGuestsCanInviteOthers() {
        return this.guestsCanInviteOthers;
    }

    public boolean getGuestsCanSeeGuests() {
        return this.guestsCanSeeGuests;
    }

    public void setGuestsCanModify(boolean guestsCanModify) {
        this.guestsCanModify = guestsCanModify;
    }

    public void setGuestsCanModify(int guestsCanModify) {
        setGuestsCanModify(guestsCanModify != 0);
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

    public String getExDate() {
        return this.exdate;
    }

    public void setExDate(String exdate) {
        this.exdate = exdate;
    }

    public ArrayList<CalendarAttendee> getAttendee() {
        return this.attendees;
    }

    public void addAttendee(CalendarAttendee attendee) {
        this.attendees.add(attendee);
    }

    public ArrayList<CalendarReminder> getReminder() {
        return this.reminders;
    }

    public void addReminder(CalendarReminder attendee) {
        this.reminders.add(attendee);
    }

    public ArrayList<CalendarExtendedProp> getExtendedProps() {
        return this.extendedProperties;
    }

    public void addExtendedProperties(CalendarExtendedProp extendedPropertie) {
        for (CalendarExtendedProp prop: this.extendedProperties) {
            if (extendedPropertie.getName().equals(prop.getName())) {
                prop.setValue(extendedPropertie.getValue());
                return;
            }
        }
        this.extendedProperties.add(extendedPropertie);
    }

    public void setHasExtendedProperties(boolean hasExtendedProperties) {
        this.hasExtendedProperties = hasExtendedProperties;
    }
    public boolean hasExtendedProperties() {
        return this.hasExtendedProperties || (extendedProperties != null && extendedProperties.size() > 0);
    }

    public boolean getHasAlarm() {
        return this.hasAlarm || (reminders != null && reminders.size() > 0);
    }

    public void setHasAlarm(int hasAlarm) {
        setHasAlarm(hasAlarm != 0);
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public long getOriginalId() {
        return this.original_id;
    }

    public void setOriginalId(long original_id) {
        this.original_id = original_id;
    }

    public String getSyncId() {
        return this.sync_id;
    }

    public void setSyncId(String sync_id) {
        this.sync_id = sync_id;
    }

    public String getOriginalSyncId() {
        return this.original_sync_id;
    }

    public void setOriginalSyncId(String original_sync_id) {
        this.original_sync_id = original_sync_id;
    }

    public boolean getOriginalAllDay() {
        return this.original_allDay;
    }

    public void setOriginalAllDay(Integer original_allDay) {
        if (original_allDay == null)
            return;
        this.original_allDay = original_allDay == 1 ? true : false;
    }

    public long getOriginalInstanceTime() {
        return this.original_instance_time;
    }

    public void setOriginalInstanceTime(long original_instance_time) {
        this.original_instance_time = original_instance_time;
    }

    public String toString() {
        return super.toString();
    }

    public ContentValues toContentValues() {

        String title = getTitle();
        String rrule = getRRule();
        String timezone = getEventTimezone();
        if (timezone == null) {
            timezone = TimeZone.getDefault().getID();
        }
        Time startTime = new Time(timezone);
        Time endTime = new Time(timezone);

        startTime.set(getDtStart());
        endTime.set(getDtEnd());
        offsetStartTimeIfNecessary(startTime, endTime, rrule);

        ContentValues values = new ContentValues();

        long startMillis;
        long endMillis;
        long calendarId = getCalendarId();
        if (getAllDay()) {
            // Reset start and end time, ensure at least 1 day duration, and set
            // the timezone to UTC, as required for all-day events.
            timezone = Time.TIMEZONE_UTC;
            startTime.hour = 0;
            startTime.minute = 0;
            startTime.second = 0;
            startTime.timezone = timezone;
            startMillis = startTime.normalize(true);

            endTime.hour = 0;
            endTime.minute = 0;
            endTime.second = 0;
            endTime.timezone = timezone;
            endMillis = endTime.normalize(true);
            if (endMillis < startMillis + Utils.DAY_IN_MILLIS) {
                // EditEventView#fillModelFromUI() should treat this case, but we want to ensure
                // the condition anyway.
                endMillis = startMillis + Utils.DAY_IN_MILLIS;
            }
        } else {
            startMillis = startTime.toMillis(true);
            endMillis = endTime.toMillis(true);

        }

        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.EVENT_TIMEZONE, timezone);
        values.put(Events.TITLE, title);
        values.put(Events.ALL_DAY, getAllDay() ? 1 : 0);
        values.put(Events.DTSTART, startMillis);
        values.put(Events.RRULE, rrule);
        if (!TextUtils.isEmpty(rrule)) {
            addRecurrenceRule(values);
        } else {
            values.put(Events.DURATION, (String) null);
            values.put(Events.DTEND, endMillis);
        }

        if (getDescription() != null) {
            values.put(Events.DESCRIPTION, getDescription().trim());
        } else {
            values.put(Events.DESCRIPTION, (String) null);
        }

        if (getEventLocation() != null) {
            values.put(Events.EVENT_LOCATION, getEventLocation().trim());
        } else {
            values.put(Events.EVENT_LOCATION, (String) null);
        }

        values.put(Events.AVAILABILITY, getAvailability());
        values.put(Events.HAS_ATTENDEE_DATA, getHasAttendeeData());

        int accessLevel = getAccessLevel();
        if (accessLevel > 0) {
            // For now the array contains the values 0, 2, and 3. We add one to match.
            // Default (0), Private (2), Public (3)
            accessLevel++;
        }

        values.put(Events.ACCESS_LEVEL, accessLevel);
        values.put(Events.STATUS, getEventStatus());

        if (isInitEventColor()) {
            if (getEventColor() == getCalendarColor()) {
                values.put(Events.EVENT_COLOR_KEY, NO_EVENT_COLOR);
            } else {
                values.put(Events.EVENT_COLOR_KEY, getEventColorKey());
            }
        }

        values.put(Events.HAS_EXTENDED_PROPERTIES, hasExtendedProperties());

        if (BuildConfig.DEBUG) {
            for (String key : values.keySet())
                Log.d(TAG, key + ": " + values.get(key));
        }

        return values;

    }

    public boolean isValid() {
        if (getCalendarId() == -1)
            return false;

        return true;
    }

    public boolean isSameEvent(CalendarEventItem origin) {
        if (origin == null)
            return false;

        if (getCalendarId() != origin.getCalendarId())
            return false;

        if (getId() != origin.getId())
            return false;

        return true;
    }

    public boolean isModifyData(CalendarEventItem originItem) {

        if (getAllDay() != originItem.getAllDay())
            return false;

        if (getOriginalAllDay() != originItem.getOriginalAllDay())
            return false;

        if (getAttendee().size() == 0) {
            if (originItem.getAttendee().size() != 0)
                return false;
        } else if (!getAttendee().equals(originItem.getAttendee()))
            return false;

        if (getExtendedProps().size() == 0) {
            if (originItem.getExtendedProps().size() != 0)
                return false;
        } else if (!getExtendedProps().equals(originItem.getExtendedProps()))
            return false;

        if (getReminder().size() == 0) {
            if (originItem.getReminder().size() != 0)
                return false;
        } else if (!getReminder().equals(originItem.getReminder()))
            return false;

        if (getCalendarId() != originItem.getCalendarId())
            return false;

        if (getEventColor() != originItem.getEventColor())
            return false;

        if (TextUtils.isEmpty(getRRule())) {
            if (!TextUtils.isEmpty(originItem.getRRule()))
                return false;
        } else if (!getRRule().equals(originItem.getRRule()))
            return false;

        if (getGuestsCanInviteOthers() != originItem.getGuestsCanInviteOthers())
            return false;

        if (getGuestsCanModify() != originItem.getGuestsCanModify())
            return false;

        if (getGuestsCanSeeGuests() != originItem.getGuestsCanSeeGuests())
            return false;

        if (getAccessLevel() != originItem.getAccessLevel())
            return false;

        if (getOrganizer() == null) {
            if (originItem.getOrganizer() != null)
                return false;
        } else if (!getOrganizer().equals(originItem.getOrganizer()))
            return false;

        if (getId() != originItem.getId())
            return  false;

        if (getOriginalInstanceTime() != originItem.getOriginalInstanceTime())
            return false;

        return true;

    }

    public String getAttendeesString() {
        StringBuilder b = new StringBuilder();
        for (CalendarAttendee attendee : getAttendee()) {
            String name = attendee.getAttendeeName();
            String email = attendee.getAttendeeEmail();
            String status = Integer.toString(attendee.getAttendeeStatus());
            b.append("name:").append(name);
            b.append(" email:").append(email);
            b.append(" status:").append(status);
        }
        return b.toString();
    }

    public int getEventAccessLevel() {

        CalendarItem calendarItem = getCalendarItem();

        if (calendarItem == null || calendarItem.getCalenarId() == -1)
            return ACCESS_LEVEL_NONE;

        int accessLevel = CalendarContract.Calendars.CAL_ACCESS_NONE;
        String calendarOwnerAccount = null;

        if (calendarItem != null) {
            accessLevel = calendarItem.getAccessLevel();
            calendarOwnerAccount = calendarItem.getOwnerAccount();
        }

        if (accessLevel < CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR)
            return ACCESS_LEVEL_NONE;

        if (getGuestsCanModify())
            return ACCESS_LEVEL_EDIT;

        if (!TextUtils.isEmpty(calendarOwnerAccount)
                && calendarOwnerAccount.equalsIgnoreCase(getOrganizer()))
            return ACCESS_LEVEL_EDIT;

        return ACCESS_LEVEL_DELETE;
    }

    /**
     * If the recurrence rule is such that the event start date doesn't actually fall in one of the
     * recurrences, then push the start date up to the first actual instance of the event.
     */
    private void offsetStartTimeIfNecessary(Time startTime, Time endTime, String rrule) {
        if (rrule == null || TextUtils.isEmpty(rrule)) {
            // No need to waste any time with the parsing if the rule is empty.
            return;
        }

        EventRecurrence mEventRecurrence = new EventRecurrence();
        mEventRecurrence.parse(rrule);
        // Check if we meet the specific special case. It has to:
        //  * be weekly
        //  * not recur on the same day of the week that the startTime falls on
        // In this case, we'll need to push the start time to fall on the first day of the week
        // that is part of the recurrence.
        if (mEventRecurrence.freq != EventRecurrence.WEEKLY) {
            // Not weekly so nothing to worry about.
            return;
        }
        if (mEventRecurrence.byday == null ||
                mEventRecurrence.byday.length > mEventRecurrence.bydayCount) {
            // This shouldn't happen, but just in case something is weird about the recurrence.
            return;
        }

        // Start to figure out what the nearest weekday is.
        int closestWeekday = Integer.MAX_VALUE;
        int weekstart = EventRecurrence.day2TimeDay(mEventRecurrence.wkst);
        int startDay = startTime.weekDay;
        for (int i = 0; i < mEventRecurrence.bydayCount; i++) {
            int day = EventRecurrence.day2TimeDay(mEventRecurrence.byday[i]);
            if (day == startDay) {
                // Our start day is one of the recurring days, so we're good.
                return;
            }

            if (day < weekstart) {
                // Let's not make any assumptions about what weekstart can be.
                day += 7;
            }
            // We either want the earliest day that is later in the week than startDay ...
            if (day > startDay && (day < closestWeekday || closestWeekday < startDay)) {
                closestWeekday = day;
            }
            // ... or if there are no days later than startDay, we want the earliest day that is
            // earlier in the week than startDay.
            if (closestWeekday == Integer.MAX_VALUE || closestWeekday < startDay) {
                // We haven't found a day that's later in the week than startDay yet.
                if (day < closestWeekday) {
                    closestWeekday = day;
                }
            }
        }

        // We're here, so unfortunately our event's start day is not included in the days of
        // the week of the recurrence. To save this event correctly we'll need to push the start
        // date to the closest weekday that *is* part of the recurrence.
        if (closestWeekday < startDay) {
            closestWeekday += 7;
        }
        int daysOffset = closestWeekday - startDay;
        startTime.monthDay += daysOffset;
        endTime.monthDay += daysOffset;
        long newStartTime = startTime.normalize(true);
        long newEndTime = endTime.normalize(true);

        // Later we'll actually be using the values from the model rather than the startTime
        // and endTime themselves, so we need to make these changes to the model as well.
        setDtStart(newStartTime);
        setDtEnd(newEndTime);
    }

    // Adds an rRule and duration to a set of content values
    void addRecurrenceRule(ContentValues values) {
        String rrule = getRRule();

        values.put(Events.RRULE, rrule);
        long end = getDtEnd();
        long start = getDtStart();
        String duration = null;
        if (getDuration() != null)
            duration = getDuration();

        boolean isAllDay = getAllDay();
        if (end >= start) {
            if (isAllDay) {
                // if it's all day compute the duration in days
                long days = (end - start + Utils.DAY_IN_MILLIS - 1) / Utils.DAY_IN_MILLIS;
                duration = "P" + days + "D";
            } else {
                // otherwise compute the duration in seconds
                long seconds = (end - start) / Utils.SECOND_IN_MILLIS;
                duration = "P" + seconds + "S";
            }
        } else if (TextUtils.isEmpty(duration)) {
            // If no good duration info exists assume the default
            if (isAllDay) {
                duration = "P1D";
            } else {
                duration = "P3600S";
            }
        }
        // recurring events should have a duration and dtend set to null
        values.put(Events.DURATION, duration);
        values.put(Events.DTEND, (Long) null);
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        CalendarEventItem item = (CalendarEventItem) super.clone();

        if (getHasAttendeeData()) {
            item.attendees = (ArrayList) attendees.clone();
        }

        if (getHasAlarm()) {
            item.reminders = (ArrayList) reminders.clone();
        }

        return item;
    }

    public void setOrganizerDisplayName(String organizerDisplayName) {
        this.organizerDisplayName = organizerDisplayName;
    }

    public String getOrganizerDisplayName() {
        return this.organizerDisplayName;
    }

    public void setOwnerAttendeeId(int ownerAttendeeId) {
        this.ownerAttendeeId = ownerAttendeeId;
    }

    public int getOwnerAttendeeId() {
        return this.ownerAttendeeId;
    }

    public void setSelfAttendeeStatus(int selfAttendeeStatus) {
        this.selfAttendeeStatus = selfAttendeeStatus;
    }

    public int getSelfAttendeeStatus() {
        return this.selfAttendeeStatus;
    }

    public void setEventColorCache(EventColorCache eventColorCache) {
        this.mEventColorCache = eventColorCache;
    }

    public boolean getIsFirstEventInSeries() {
        return isFirstEventInSeries;
    }

    public void setIsFirstEventInSeries(boolean firstEventInSeries) {
        isFirstEventInSeries = firstEventInSeries;
    }

    public int getMaxReminders() {
        return maxReminders;
    }

    public void setMaxReminders(int maxReminders) {
        this.maxReminders = maxReminders;
    }

    public String getAllowedReminders() {
        return allowedReminders;
    }

    public void setAllowedReminders(String allowedReminders) {
        this.allowedReminders = allowedReminders;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String type) {
         eventType = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getAllDay() ? 1231 : 1237);
        result = prime * result + ((getAttendee() == null) ? 0 : getAttendeesString().hashCode());
        result = prime * result + (int) (getCalendarId() ^ (getCalendarId() >>> 32));
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + (int) (getDtEnd() ^ (getDtEnd() >>> 32));
        result = prime * result + (getGuestsCanInviteOthers() ? 1231 : 1237);
        result = prime * result + (getGuestsCanModify() ? 1231 : 1237);
        result = prime * result + (getGuestsCanSeeGuests() ? 1231 : 1237);
        result = prime * result + (getCalendarItem().getCanOrganizerRespond() ? 1231 : 1237);
//        result = prime * result + (mModelUpdatedWithEventCursor ? 1231 : 1237);
        result = prime * result + getCalendarItem().getAccessLevel();
        result = prime * result + (getHasAlarm() ? 1231 : 1237);
        result = prime * result + (getHasAttendeeData() ? 1231 : 1237);
        result = prime * result + (int) (getId() ^ (getId() >>> 32));
        result = prime * result + (getIsFirstEventInSeries() ? 1231 : 1237);
        result = prime * result + (getIsOrganizer() ? 1231 : 1237);
        result = prime * result + ((getEventLocation() == null) ? 0 : getEventLocation().hashCode());
        result = prime * result + ((getOrganizer() == null) ? 0 : getOrganizer().hashCode());
        result = prime * result + (getOriginalAllDay() ? 1231 : 1237);
//        result = prime * result + (int) (mOriginalEnd ^ (mOriginalEnd >>> 32));
        result = prime * result + ((getOriginalSyncId() == null) ? 0 : getOriginalSyncId().hashCode());
//        result = prime * result + (int) (mOriginalId ^ (mOriginalEnd >>> 32));
//        result = prime * result + (int) (mOriginalStart ^ (mOriginalStart >>> 32));
//        result = prime * result + ((mOriginalTime == null) ? 0 : mOriginalTime.hashCode());
        result = prime * result + ((getOwnerAccount() == null) ? 0 : getOwnerAccount().hashCode());
        result = prime * result + ((getReminder() == null) ? 0 : getReminder().hashCode());
        result = prime * result + ((TextUtils.isEmpty(getRRule())) ? 0 : getRRule().hashCode());
        result = prime * result + getSelfAttendeeStatus();
        result = prime * result + getOwnerAttendeeId();
        result = prime * result + (int) (getDtStart() ^ (getDtStart() >>> 32));
//        result = prime * result + ((mSyncAccount == null) ? 0 : mSyncAccount.hashCode());
//        result = prime * result + ((mSyncAccountType == null) ? 0 : mSyncAccountType.hashCode());
        result = prime * result + ((getSyncId() == null) ? 0 : getSyncId().hashCode());
        result = prime * result + ((getEventTimezone() == null) ? 0 : getEventTimezone().hashCode());
//        result = prime * result + ((mTimezone2 == null) ? 0 : mTimezone2.hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + (getAvailability());
        result = prime * result + ((mUri == null) ? 0 : mUri.hashCode());
        result = prime * result + getAccessLevel();
        result = prime * result + getEventStatus();
        return result;
    }

}
