package com.tuda.teacher.calendar.model;

import android.content.Context;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.text.TextUtils;
import android.text.format.Time;

import com.tuda.teacher.calendar.DateException;
import com.tuda.teacher.calendar.EventRecurrence;
import com.tuda.teacher.calendar.RecurrenceProcessor;
import com.tuda.teacher.calendar.RecurrenceSet;
import com.tuda.teacher.calendar.model.entity.CalendarEventInstance;
import com.tuda.teacher.calendar.model.entity.CalendarEventItem;
import com.tuda.teacher.calendar.model.entity.CalendarReminder;

import java.util.ArrayList;

/**
 * Created by crazyluv on 2016. 1. 25..
 */
public class EventSaveHelper extends BaseHelper {
    public static final int CALENDAR_LOAD = 1;
    public static final int CALENDAR_DIALOG = 1 << 1;

    public static final int CREATE_NEW_EVENT = 1;
    public static final int MODIFY_ALL = 1 << 1;
    public static final int MODIFY_SELECTED = 1 << 2;
    public static final int MODIFY_SELECTED_FOLLOWING = 1 << 3;
    public static final int MODIFY_NON_REPEAT_TO_REPEAT = 1 << 4;
    public static final int SIMPLE_MODIFY = 1 << 5;

    public EventSaveHelper(Context context) {
        super(context);
    }

    /**
     * Prepares an update to the original event so it stops where the new series
     * begins. When we update 'this and all following' events we need to change
     * the original event to end before a new series starts. This creates an
     * update to the old event's rrule to do that.
     *<p>
     * If the event's recurrence rule has a COUNT, we also need to reduce the count in the
     * RRULE for the exception event.
     *
     * @param ops The list of operations to add the update to
     * @param originalModel The original event that we're updating
     * @param endTimeMillis The time before which the event must end (i.e. the start time of the
     *        exception event instance).
     * @return A replacement exception recurrence rule.
     */
    public String updatePastEvents(ArrayList<ContentProviderOperation> ops,
                                   CalendarEventItem originalModel, long endTimeMillis) {
        boolean origAllDay = originalModel.getAllDay();
        String origRrule = originalModel.getRRule();
        String newRrule = origRrule;

        EventRecurrence origRecurrence = new EventRecurrence();
        origRecurrence.parse(origRrule);

        // Get the start time of the first instance in the original recurrence.
        long startTimeMillis = originalModel.getDtStart();
        Time dtstart = new Time();
        dtstart.timezone = originalModel.getEventTimezone();
        dtstart.set(startTimeMillis);

        ContentValues updateValues = new ContentValues();

        if (origRecurrence.count > 0) {
            /*
             * Generate the full set of instances for this recurrence, from the first to the
             * one just before endTimeMillis.  The list should never be empty, because this method
             * should not be called for the first instance.  All we're really interested in is
             * the *number* of instances found.
             *
             * TODO: the model assumes RRULE and ignores RDATE, EXRULE, and EXDATE.  For the
             * current environment this is reasonable, but that may not hold in the future.
             *
             * TODO: if COUNT is 1, should we convert the event to non-recurring?  e.g. we
             * do an "edit this and all future events" on the 2nd instances.
             */
            RecurrenceSet recurSet = new RecurrenceSet(originalModel.getRRule(), null, null, null);
            RecurrenceProcessor recurProc = new RecurrenceProcessor();
            long[] recurrences;
            try {
                recurrences = recurProc.expand(dtstart, recurSet, startTimeMillis, endTimeMillis);
            } catch (DateException de) {
                throw new RuntimeException(de);
            }

            if (recurrences.length == 0) {
                throw new RuntimeException("can't use this method on first instance");
            }

            EventRecurrence excepRecurrence = new EventRecurrence();
            excepRecurrence.parse(origRrule);  // TODO: add+use a copy constructor instead
            excepRecurrence.count -= recurrences.length;
            newRrule = excepRecurrence.toString();


            origRecurrence.count = recurrences.length;

        } else {
            // The "until" time must be in UTC time in order for Google calendar
            // to display it properly. For all-day events, the "until" time string
            // must include just the date field, and not the time field. The
            // repeating events repeat up to and including the "until" time.
            Time untilTime = new Time();
            untilTime.timezone = Time.TIMEZONE_UTC;

            // Subtract one second from the old begin time to get the new
            // "until" time.
            untilTime.set(endTimeMillis - 1000); // subtract one second (1000 millis)
            if (origAllDay) {
                untilTime.hour = 0;
                untilTime.minute = 0;
                untilTime.second = 0;
                untilTime.allDay = true;
                untilTime.normalize(false);

                // This should no longer be necessary -- DTSTART should already be in the correct
                // format for an all-day event.
                dtstart.hour = 0;
                dtstart.minute = 0;
                dtstart.second = 0;
                dtstart.allDay = true;
                dtstart.timezone = Time.TIMEZONE_UTC;
            }
            origRecurrence.until = untilTime.format2445();
        }

        updateValues.put(Events.RRULE, origRecurrence.toString());
        updateValues.put(Events.DTSTART, dtstart.normalize(true));
        ContentProviderOperation.Builder b =
                ContentProviderOperation.newUpdate(Uri.parse(originalModel.getUri()))
                        .withValues(updateValues);
        ops.add(b.build());

        return newRrule;
    }

    void checkTimeDependentFields(CalendarEventItem originalModel, CalendarEventItem model, CalendarEventInstance originalInstance, ContentValues values, int modifyWhich) {

        long oldBegin = originalInstance.getBegin();
        long oldEnd = originalInstance.getEnd();
        boolean oldAllDay = originalModel.getAllDay();
        String oldRrule = !TextUtils.isEmpty(originalModel.getRRule()) ? originalModel.getRRule() : null;
        String oldTimezone = originalModel.getEventTimezone();

        long newBegin = model.getDtStart();
        long newEnd = model.getDtEnd();
        boolean newAllDay = model.getAllDay();
        String newRrule = !TextUtils.isEmpty(model.getRRule()) ? model.getRRule() : null;
        String newTimezone = model.getEventTimezone();

        // If none of the time-dependent fields changed, then remove them.
        if (oldBegin == newBegin && oldEnd == newEnd && oldAllDay == newAllDay
                && TextUtils.equals(oldRrule, newRrule)
                && TextUtils.equals(oldTimezone, newTimezone)) {
            values.remove(Events.DTSTART);
            values.remove(Events.DTEND);
            values.remove(Events.DURATION);
            values.remove(Events.ALL_DAY);
            values.remove(Events.RRULE);
            values.remove(Events.EVENT_TIMEZONE);
            return;
        }

        if (TextUtils.isEmpty(oldRrule) || TextUtils.isEmpty(newRrule)) {
            return;
        }

        // If we are modifying all events then we need to set DTSTART to the
        // start time of the first event in the series, not the current
        // date and time. If the start time of the event was changed
        // (from, say, 3pm to 4pm), then we want to add the time difference
        // to the start time of the first event in the series (the DTSTART
        // value). If we are modifying one instance or all following instances,
        // then we leave the DTSTART field alone.
        if (modifyWhich == MODIFY_ALL) {
            long oldStartMillis = originalModel.getDtStart();
            if (oldBegin != newBegin) {
                // The user changed the start time of this event
                long offset = newBegin - oldBegin;
                oldStartMillis += offset;
            }
            if (newAllDay) {
                Time time = new Time(Time.TIMEZONE_UTC);
                time.set(oldStartMillis);
                time.hour = 0;
                time.minute = 0;
                time.second = 0;
                oldStartMillis = time.toMillis(false);
            }
            values.put(Events.DTSTART, oldStartMillis);
        }
    }

    /**
     * Saves the reminders, if they changed. Returns true if operations to
     * update the database were added. Uses a reference id since an id isn't
     * created until the row is added.
     *
     * @param ops the array of ContentProviderOperations
     * @param eventIdIndex the id of the event whose reminders are being updated
     * @param reminders the array of reminders set by the user
     * @param originalReminders the original array of reminders
     * @param forceSave if true, then save the reminders even if they didn't change
     * @return true if operations to update the database were added
     */
    public static boolean saveRemindersWithBackRef(ArrayList<ContentProviderOperation> ops,
                                                   int eventIdIndex, ArrayList<CalendarReminder> reminders,
                                                   ArrayList<CalendarReminder> originalReminders, boolean forceSave) {
        // If the reminders have not changed, then don't update the database
        if (reminders.equals(originalReminders) && !forceSave) {
            return false;
        }

        // Delete all the existing reminders for this event
        ContentProviderOperation.Builder b = ContentProviderOperation
                .newDelete(Reminders.CONTENT_URI);
        b.withSelection(Reminders.EVENT_ID + "=?", new String[1]);
        b.withSelectionBackReference(0, eventIdIndex);
        ops.add(b.build());

        ContentValues values = new ContentValues();
        int len = reminders.size();

        // Insert the new reminders, if any
        for (int i = 0; i < len; i++) {
            CalendarReminder re = reminders.get(i);

            values.clear();
            values.put(Reminders.MINUTES, re.getMinutes());
            values.put(Reminders.METHOD, re.getMethod());
            b = ContentProviderOperation.newInsert(Reminders.CONTENT_URI).withValues(values);
            b.withValueBackReference(Reminders.EVENT_ID, eventIdIndex);
            ops.add(b.build());
        }
        return true;
    }

    /**
     * Saves the reminders, if they changed. Returns true if operations to
     * update the database were added.
     *
     * @param ops the array of ContentProviderOperations
     * @param eventId the id of the event whose reminders are being updated
     * @param reminders the array of reminders set by the user
     * @param originalReminders the original array of reminders
     * @param forceSave if true, then save the reminders even if they didn't change
     * @return true if operations to update the database were added
     */
    public static boolean saveReminders(ArrayList<ContentProviderOperation> ops, long eventId,
                                        ArrayList<CalendarReminder> reminders, ArrayList<CalendarReminder> originalReminders,
                                        boolean forceSave) {
        // If the reminders have not changed, then don't update the database
        if (reminders.equals(originalReminders) && !forceSave) {
            return false;
        }

        // Delete all the existing reminders for this event
        String where = Reminders.EVENT_ID + "=?";
        String[] args = new String[] {Long.toString(eventId)};
        ContentProviderOperation.Builder b = ContentProviderOperation
                .newDelete(Reminders.CONTENT_URI);
        b.withSelection(where, args);
        ops.add(b.build());

        ContentValues values = new ContentValues();
        int len = reminders.size();

        // Insert the new reminders, if any
        for (int i = 0; i < len; i++) {
            CalendarReminder re = reminders.get(i);

            values.clear();
            values.put(Reminders.MINUTES, re.getMinutes());
            values.put(Reminders.METHOD, re.getMethod());
            values.put(Reminders.EVENT_ID, eventId);
            b = ContentProviderOperation.newInsert(Reminders.CONTENT_URI).withValues(values);
            ops.add(b.build());
        }
        return true;
    }


    static boolean isFirstEventInSeries(CalendarEventInstance model, CalendarEventItem originalModel) {
        return model.getBegin() == originalModel.getDtStart();
    }

}
