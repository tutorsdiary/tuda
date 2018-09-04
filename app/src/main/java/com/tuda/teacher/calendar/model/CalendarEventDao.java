package com.tuda.teacher.calendar.model;

import android.content.AsyncQueryHandler;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Instances;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Colors;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.text.format.Time;

import com.tuda.teacher.calendar.callback.AsyncQueryCallback;
import com.tuda.teacher.calendar.model.entity.CalendarEventInstance;
import com.tuda.teacher.calendar.model.entity.CalendarEventItem;
import com.tuda.teacher.calendar.util.CalendarEventUtils;
import com.tuda.teacher.common.Preference;
import com.tuda.teacher.network.entity.CalendarEventItemForm;
import com.tuda.teacher.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class CalendarEventDao extends BaseDao {

    public static final String DISPLAY_ALL_DAY = "displayAllDay";
    public static final String[] INSTANCES_PROJECTION = new String[] {

            Instances.TITLE,                 // 0
            Instances.EVENT_LOCATION,        // 1
            Instances.ALL_DAY,               // 2
            Instances.DISPLAY_COLOR,         // 3 If SDK < 16, set to Instances.CALENDAR_COLOR.
            Instances.EVENT_TIMEZONE,        // 4
            Instances.EVENT_ID,              // 5
            Instances.BEGIN,                 // 6
            Instances.END,                   // 7
            Instances._ID,                   // 8
            Instances.START_DAY,             // 9
            Instances.END_DAY,               // 10
            Instances.START_MINUTE,          // 11
            Instances.END_MINUTE,            // 12
            Instances.HAS_ALARM,             // 13
            Instances.RRULE,                 // 14
            Instances.RDATE,                 // 15
            Instances.SELF_ATTENDEE_STATUS,  // 16
            Events.ORGANIZER,                // 17
            Events.GUESTS_CAN_MODIFY,        // 18
            Instances.ALL_DAY + "=1 OR (" + Instances.END + "-" + Instances.BEGIN + ")>="
                    + android.text.format.DateUtils.DAY_IN_MILLIS + " AS " + DISPLAY_ALL_DAY, // 19
            Instances.GUESTS_CAN_SEE_GUESTS, // 20
            Instances.GUESTS_CAN_INVITE_OTHERS, // 21
            Instances.CALENDAR_ID,              // 22
            Instances.STATUS,                   // 23
            Instances.CALENDAR_DISPLAY_NAME,    // 24
            Instances.HAS_ATTENDEE_DATA,        // 25
            Instances.ORIGINAL_INSTANCE_TIME

    };

    public static final int INSTANCES_INDEX_TITLE = 0;
    public static final int INSTANCES_INDEX_EVENT_LOCATION = 1;
    public static final int INSTANCES_INDEX_ALL_DAY = 2;
    public static final int INSTANCES_INDEX_DISPLAY_COLOR = 3;
    public static final int INSTANCES_INDEX_EVENT_TIMEZONE = 4;
    public static final int INSTANCES_INDEX_EVENT_ID = 5;
    public static final int INSTANCES_INDEX_BEGIN = 6;
    public static final int INSTANCES_INDEX_END = 7;
    public static final int INSTANCES_INDEX__ID = 8;
    public static final int INSTANCES_INDEX_START_DAY = 9;
    public static final int INSTANCES_INDEX_END_DAY = 10;
    public static final int INSTANCES_INDEX_START_MINUTE = 11;
    public static final int INSTANCES_INDEX_END_MINUTE = 12;
    public static final int INSTANCES_INDEX_HAS_ALARM = 13;
    public static final int INSTANCES_INDEX_RRULE = 14;
    public static final int INSTANCES_INDEX_RDATE = 15;
    public static final int INSTANCES_INDEX_SELF_ATTENDEE_STATUS = 16;
    public static final int INSTANCES_INDEX_ORGANIZER = 17;
    public static final int INSTANCES_INDEX_GUESTS_CAN_MODIFY = 18;
    public static final int INSTANCES_INDEX_DISPLAY_ALL_DAY = 19;
    public static final int INSTANCES_INDEX_GUESTS_CAN_SEE_GUESTS = 20;
    public static final int INSTANCES_INDEX_GUESTS_CAN_INVITE_OTHERS = 21;
    public static final int INSTANCES_INDEX_CALENDAR_ID = 22;
    public static final int INSTANCES_INDEX_STATUS = 23;
    public static final int INSTANCES_INDEX_CALENDAR_DISPLAY_NAME = 24;
    public static final int INSTANCES_INDEX_HAS_ATTENDEE_DATA = 25;
    public static final int INSTANCES_INDEX_ORIGINAL_INSTANCE_TIME = 26;

    static {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
            INSTANCES_PROJECTION[INSTANCES_INDEX_DISPLAY_COLOR] = Instances.CALENDAR_COLOR;
        }
    }

    public static final String[] EVENT_PROJECTION = new String[] {
            Events._ID, // 0
            Events.TITLE, // 1
            Events.DESCRIPTION, // 2
            Events.EVENT_LOCATION, // 3
            Events.ALL_DAY, // 4
            Events.HAS_ALARM, // 5
            Events.CALENDAR_ID, // 6
            Events.DTSTART, // 7
            Events.DTEND, // 8
            Events.DURATION, // 9
            Events.EVENT_TIMEZONE, // 10
            Events.RRULE, // 11
            Events._SYNC_ID, // 12
            Events.AVAILABILITY, // 13
            Events.ACCESS_LEVEL, // 14
            Events.OWNER_ACCOUNT, // 15
            Events.HAS_ATTENDEE_DATA, // 16
            Events.ORIGINAL_SYNC_ID, // 17
            Events.ORGANIZER, // 18
            Events.GUESTS_CAN_MODIFY, // 19
            Events.ORIGINAL_ID, // 20
            Events.STATUS, // 21
            Events.CALENDAR_COLOR, // 22
            Events.EVENT_COLOR, // 23
            Events.EVENT_COLOR_KEY, // 24
            Events.ORIGINAL_INSTANCE_TIME, // 25
            Calendars.MAX_REMINDERS, // 26
            Calendars.ALLOWED_REMINDERS // 27
    };

    public static final int EVENT_INDEX_ID = 0;
    public static final int EVENT_INDEX_TITLE = 1;
    public static final int EVENT_INDEX_DESCRIPTION = 2;
    public static final int EVENT_INDEX_EVENT_LOCATION = 3;
    public static final int EVENT_INDEX_ALL_DAY = 4;
    public static final int EVENT_INDEX_HAS_ALARM = 5;
    public static final int EVENT_INDEX_CALENDAR_ID = 6;
    public static final int EVENT_INDEX_DTSTART = 7;
    public static final int EVENT_INDEX_DTEND = 8;
    public static final int EVENT_INDEX_DURATION = 9;
    public static final int EVENT_INDEX_TIMEZONE = 10;
    public static final int EVENT_INDEX_RRULE = 11;
    public static final int EVENT_INDEX_SYNC_ID = 12;
    public static final int EVENT_INDEX_AVAILABILITY = 13;
    public static final int EVENT_INDEX_ACCESS_LEVEL = 14;
    public static final int EVENT_INDEX_OWNER_ACCOUNT = 15;
    public static final int EVENT_INDEX_HAS_ATTENDEE_DATA = 16;
    public static final int EVENT_INDEX_ORIGINAL_SYNC_ID = 17;
    public static final int EVENT_INDEX_ORGANIZER = 18;
    public static final int EVENT_INDEX_GUESTS_CAN_MODIFY = 19;
    public static final int EVENT_INDEX_ORIGINAL_ID = 20;
    public static final int EVENT_INDEX_EVENT_STATUS = 21;
    public static final int EVENT_INDEX_CALENDAR_COLOR = 22;
    public static final int EVENT_INDEX_EVENT_COLOR = 23;
    public static final int EVENT_INDEX_EVENT_COLOR_KEY = 24;
    public static final int EVENT_INDEX_ORIGINAL_INSTANCE_TIME = 25;
    public static final int EVENT_INDEX_MAX_REMINDERS = 26;
    public static final int EVENT_INDEX_ALLOWED_REMINDERS = 27;

    public static final String[] REMINDERS_PROJECTION = new String[] {
            Reminders._ID, // 0
            Reminders.EVENT_ID, // 1
            Reminders.MINUTES, // 2
            Reminders.METHOD, // 3
    };
    public static final int REMINDERS_INDEX_ID = 0;
    public static final int REMINDERS_INDEX_EVENT_ID = 1;
    public static final int REMINDERS_INDEX_MINUTES = 2;
    public static final int REMINDERS_INDEX_METHOD = 3;
    public static final String REMINDERS_WHERE = Reminders.EVENT_ID + "=?";

    public static final int ATTENDEE_ID_NONE = -1;
    public static final int[] ATTENDEE_VALUES = {
            Attendees.ATTENDEE_STATUS_NONE,
            Attendees.ATTENDEE_STATUS_ACCEPTED,
            Attendees.ATTENDEE_STATUS_TENTATIVE,
            Attendees.ATTENDEE_STATUS_DECLINED,
    };

    public static final String[] ATTENDEES_PROJECTION = new String[] {
            Attendees._ID, // 0
            Attendees.ATTENDEE_NAME, // 1
            Attendees.ATTENDEE_EMAIL, // 2
            Attendees.ATTENDEE_RELATIONSHIP, // 3
            Attendees.ATTENDEE_STATUS, // 4
            Attendees.ATTENDEE_TYPE, // 5
            Attendees.EVENT_ID, // 6
    };
    public static final int ATTENDEES_INDEX_ID = 0;
    public static final int ATTENDEES_INDEX_NAME = 1;
    public static final int ATTENDEES_INDEX_EMAIL = 2;
    public static final int ATTENDEES_INDEX_RELATIONSHIP = 3;
    public static final int ATTENDEES_INDEX_STATUS = 4;
    public static final int ATTENDEES_INDEX_TYPE = 5;
    public static final int ATTENDEES_INDEX_EVENT_ID = 6;
    public static final String ATTENDEES_WHERE = Attendees.EVENT_ID + "=? AND attendeeEmail IS NOT NULL";

    public static final String[] COLORS_PROJECTION = new String[] {
            Colors._ID, // 0
            Colors.ACCOUNT_NAME,
            Colors.ACCOUNT_TYPE,
            Colors.COLOR, // 1
            Colors.COLOR_KEY // 2
    };

    public static final String COLORS_ACCOUNT_WHERE = Colors.ACCOUNT_NAME + "=? AND " + Colors.ACCOUNT_TYPE +
            "=? AND " + Colors.COLOR_TYPE + "=" + Colors.TYPE_EVENT;
    public static final String COLORS_TYPE_WHERE = Colors.COLOR_TYPE + "=" + Colors.TYPE_EVENT;

    public static final int COLORS_INDEX_ACCOUNT_NAME = 1;
    public static final int COLORS_INDEX_ACCOUNT_TYPE = 2;
    public static final int COLORS_INDEX_COLOR = 3;
    public static final int COLORS_INDEX_COLOR_KEY = 4;


    private Context mContext;
    private ContentResolver mContentResolver;
    private CalendarDaoManager mManager;

    private static Map<String, CalendarEventItem> CalendarEventItems = new HashMap<>();

    Time mBaseDate;
    private Time mCurrentTime;

    private final Runnable mTZUpdater = new Runnable() {
        @Override
        public void run() {
            String tz = CalendarEventUtils.getTimeZone(mContext, this);
            mBaseDate.timezone = tz;
            mBaseDate.normalize(true);
            mCurrentTime.switchTimezone(tz);
        }
    };

    private class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            // If the query didn't return a cursor for some reason return
            AsyncQueryCallback callBack = (AsyncQueryCallback) cookie;
            List events = new ArrayList();

            if (cursor == null) {
                callBack.success(null);
                return;
            }

            // If the Activity is finishing, then close the cursor.
            // Otherwise, use the new cursor in the adapter.
            /*
            if (mContext instanceof Activity) {
                final Activity activity = ((Activity)mContext);
                if (activity == null || activity.isFinishing()) {
                    Log.d(TAG, "QueryHandler activity null");
                    cursor.close();
                    return;
                }
            }
            */

            if (cursor.getCount() == 0) {
                // The cursor is empty. This can happen if the event
                // was deleted.
                callBack.success(events);
                cursor.close();
                return;
            }

            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {

                events.add(new CalendarEventInstance(cursor));

            }

            callBack.success(events);

            cursor.close();
        }
    }

    public CalendarEventDao(Context context, CalendarDaoManager manager) {
        mContext = context;
        mManager = manager;
        mContentResolver = manager.getContentResolver();
        mCurrentTime = new Time(CalendarEventUtils.getTimeZone(context, mTZUpdater));
        mBaseDate = new Time(CalendarEventUtils.getTimeZone(context, mTZUpdater));
        mBaseDate.set(System.currentTimeMillis());
    }

    public void getCalendarEventInstances(DateTime start_date, int days, AsyncQueryCallback callback) {

        String DEFAULT_SORT_ORDER = Instances.ALL_DAY+" DESC, "+DISPLAY_ALL_DAY+" DESC, begin ASC, end DESC";

        long start_day = Time.getJulianDay(start_date.getMillis(), mCurrentTime.gmtoff);
        //DateTimeUtils.toJulianDayNumber(start_date.withTime(12, 0, 0, 0).getMillis());
        long end_day = start_day + days -1;

        Uri.Builder builder = Instances.CONTENT_BY_DAY_URI.buildUpon();
        ContentUris.appendId(builder, start_day);
        ContentUris.appendId(builder, end_day);

        HashMap<String, Boolean> visibleInfo = Preference.getCalendarVisibility();

        String selection = "";
        String[] selectionArgs = {};

        if (!visibleInfo.isEmpty()) {
            List<String> visibleList = new ArrayList<>();
            for (Map.Entry<String, Boolean> info : visibleInfo.entrySet()) {
                if (!info.getValue()) {
                    visibleList.add(info.getKey() +" != " + Instances.CALENDAR_ID);
                }
            }

            selection += TextUtils.join(" AND ", visibleList);
        }

        QueryHandler handler = new QueryHandler(mContentResolver);
        handler.startQuery(0, callback, builder.build(), INSTANCES_PROJECTION, selection, selectionArgs, DEFAULT_SORT_ORDER);

    }

    public Loader<Cursor> getEventLoader(Context context, Long eventId) {
        Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
        return new CursorLoader(context, uri, CalendarEventDao.EVENT_PROJECTION, null , null, null);
    }

    public Loader<Cursor> getAttendeeLoader(Context context, Long eventId) {

        Uri attUri = CalendarContract.Attendees.CONTENT_URI;
        String[] whereArgs = { Long.toString(eventId) };

        //Uri uri = ContentUris.withAppendedId(Attendees.CONTENT_URI, eventId);
        return new CursorLoader(context, attUri, CalendarEventDao.ATTENDEES_PROJECTION, ATTENDEES_WHERE , whereArgs, null);
    }

    public Loader<Cursor> getReminderLoader(Context context, Long eventId) {

        Uri remUri = CalendarContract.Reminders.CONTENT_URI;
        String[] whereArgs = { Long.toString(eventId) };

        return new CursorLoader(context, remUri, CalendarEventDao.REMINDERS_PROJECTION, REMINDERS_WHERE , whereArgs, null);
    }

    public Loader<Cursor> getColorLoader(Context context, String accountName) {

        Uri colorUri = CalendarContract.Colors.CONTENT_URI;
        String[] whereArgs = { null };

        String COLORS_WHERE;
        if (!TextUtils.isEmpty(accountName)) {
            COLORS_WHERE = COLORS_ACCOUNT_WHERE;
            whereArgs[0] = accountName;
        } else {
            COLORS_WHERE = COLORS_TYPE_WHERE;
            whereArgs = null;
        }

        return new CursorLoader(context, colorUri, CalendarEventDao.COLORS_PROJECTION, COLORS_WHERE, whereArgs, null);
    }

    public void save(CalendarEventItem item) {

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

    }
    public void createEventItemFrom(CalendarEventItemForm serverItem, CalendarEventItem item) {
        if (item == null) {
            Log.e(TAG, "empty event item.");
            return;
        }

        item.setId(serverItem.getId());
        item.setEventType(serverItem.getEventType());
        item.setTitle(serverItem.getTitle());
        item.setDescription(serverItem.getDescription());
        item.setEventLocation(serverItem.getEventLocation());
        item.setAllDay(serverItem.getAllDay()?1:0);
        item.setHasAlarm(serverItem.getHasAlarm());

        item.setOrganizer(serverItem.getOrganizer());

        if (!TextUtils.isEmpty(serverItem.getEventTimezone()))
            item.setEventTimezone(serverItem.getEventTimezone());

        item.setRRule(serverItem.getRRule());
        item.setSyncId(serverItem.getSyncId());
        item.setAvaliavility(serverItem.getAvailability());

        int accessLevel = serverItem.getAccessLevel();

//        item.setOwnerAccount(cursor.getString(EVENT_INDEX_OWNER_ACCOUNT));
        item.setHasAttendeeData(serverItem.getHasAttendeeData()?1:0);

        if (serverItem.getOriginalSyncId() != null)
            item.setOriginalSyncId(serverItem.getOriginalSyncId());

        if (serverItem.getOriginalId() != null)
            item.setOriginalId(serverItem.getOriginalId());

        if (serverItem.getGuestsCanModify() != null)
            item.setGuestsCanModify(serverItem.getGuestsCanModify());

        if (serverItem.getMaxReminders() != null)
            item.setMaxReminders(serverItem.getMaxReminders());

        if (serverItem.getAllowedReminders() != null)
            item.setAllowedReminders(serverItem.getAllowedReminders());

        Long startTime = serverItem.getStartDate();

        item.setDtStart(startTime);

        if (!TextUtils.isEmpty(item.getRRule())) {
            String duration = serverItem.getDuration();
            item.setDuration(duration);
        } else {
            Long endTime = serverItem.getEndDate();
            item.setDtEnd(endTime);
        }

        int eventColor = serverItem.getEventColor();

        item.setEventColor(CalendarEventUtils.getDisplayColorFromColor(eventColor));

        if (accessLevel > 0) {
            // For now the array contains the values 0, 2, and 3. We subtract
            // one to make it easier to handle in code as 0,1,2.
            // Default (0), Private (1), Public (2)
            accessLevel--;
        }

        item.setAccessLevel(accessLevel);
        item.setEventStatus(serverItem.getEventStatus());

    }

    public void createEventItemFrom(Cursor cursor, CalendarEventItem item) {

        if (cursor == null || cursor.getCount() != 1) {
            Log.e(TAG, "empty cursor.");
            return;
        }

        cursor.moveToFirst();

        item.setId(cursor.getLong(EVENT_INDEX_ID));
        item.setTitle(cursor.getString(EVENT_INDEX_TITLE));
        item.setDescription(cursor.getString(EVENT_INDEX_DESCRIPTION));
        item.setEventLocation(cursor.getString(EVENT_INDEX_EVENT_LOCATION));
        item.setAllDay(cursor.getInt(EVENT_INDEX_ALL_DAY));
        item.setHasAlarm(cursor.getInt(EVENT_INDEX_HAS_ALARM));
        item.setCalendarId(cursor.getLong(EVENT_INDEX_CALENDAR_ID));

        item.setOrganizer(cursor.getString(EVENT_INDEX_ORGANIZER));

        if (!TextUtils.isEmpty(cursor.getString(EVENT_INDEX_TIMEZONE)))
            item.setEventTimezone(cursor.getString(EVENT_INDEX_TIMEZONE));

        item.setRRule(cursor.getString(EVENT_INDEX_RRULE));
        item.setSyncId(cursor.getString(EVENT_INDEX_SYNC_ID));
        item.setAvaliavility(cursor.getInt(EVENT_INDEX_AVAILABILITY));

        int accessLevel = cursor.getInt(EVENT_INDEX_ACCESS_LEVEL);

        item.setOwnerAccount(cursor.getString(EVENT_INDEX_OWNER_ACCOUNT));
        item.setHasAttendeeData(cursor.getInt(EVENT_INDEX_HAS_ATTENDEE_DATA));

        item.setOriginalSyncId(cursor.getString(EVENT_INDEX_ORIGINAL_SYNC_ID));
        item.setOriginalId(cursor.getLong(EVENT_INDEX_ORIGINAL_ID));
        item.setOrganizer(cursor.getString(EVENT_INDEX_ORGANIZER));

        item.setGuestsCanModify(cursor.getInt(EVENT_INDEX_GUESTS_CAN_MODIFY));

        item.setMaxReminders(cursor.getInt(EVENT_INDEX_MAX_REMINDERS));
        item.setAllowedReminders(cursor.getString(EVENT_INDEX_ALLOWED_REMINDERS));

        String duration = cursor.getString(EVENT_INDEX_DURATION);

        Long startTime = cursor.getLong(EVENT_INDEX_DTSTART);

        item.setDtStart(startTime);

        if (!TextUtils.isEmpty(item.getRRule())) {
            item.setDuration(duration);
        } else {
            Long endTime = cursor.getLong(EVENT_INDEX_DTEND);
            item.setDtEnd(endTime);
        }

        int eventColor;
        if (cursor.isNull(EVENT_INDEX_EVENT_COLOR)) {
            eventColor = cursor.getInt(EVENT_INDEX_CALENDAR_COLOR);
        } else {
            eventColor = cursor.getInt(EVENT_INDEX_EVENT_COLOR);
        }

        item.setEventColor(CalendarEventUtils.getDisplayColorFromColor(eventColor));

        if (accessLevel > 0) {
            // For now the array contains the values 0, 2, and 3. We subtract
            // one to make it easier to handle in code as 0,1,2.
            // Default (0), Private (1), Public (2)
            accessLevel--;
        }

        item.setAccessLevel(accessLevel);
        item.setEventStatus(cursor.getInt(EVENT_INDEX_EVENT_STATUS));

    }


}
