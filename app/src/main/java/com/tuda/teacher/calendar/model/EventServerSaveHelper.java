package com.tuda.teacher.calendar.model;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import android.text.format.Time;

import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.TudaApp;
import com.tuda.teacher.calendar.DateException;
import com.tuda.teacher.calendar.EventRecurrence;
import com.tuda.teacher.calendar.RecurrenceProcessor;
import com.tuda.teacher.calendar.RecurrenceSet;
import com.tuda.teacher.calendar.model.entity.CalendarAttendee;
import com.tuda.teacher.calendar.model.entity.CalendarEventInstance;
import com.tuda.teacher.calendar.model.entity.CalendarEventItem;
import com.tuda.teacher.calendar.model.entity.CalendarExtendedProp;
import com.tuda.teacher.calendar.model.entity.CalendarReminder;
import com.tuda.teacher.calendar.util.CalendarEventUtils;
import com.tuda.teacher.common.GACommon;
import com.tuda.teacher.network.ResponseCallback;
import com.tuda.teacher.network.entity.CalendarAttendeeForm;
import com.tuda.teacher.network.entity.CalendarEventItemForm;
import com.tuda.teacher.network.entity.CalendarExtendedPropertieForm;
import com.tuda.teacher.network.entity.CalendarReminderForm;
import com.tuda.teacher.network.entry.RequestEntry;
import com.tuda.teacher.network.entry.ResponseEntry;
import com.tuda.teacher.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class EventServerSaveHelper extends EventSaveHelper {

    public interface EventSaveCallback {
        void onSuccess(Response<ResponseEntry<Boolean>> response);
        void onError(String message, Response<ResponseEntry<Boolean>> response);
    }

    private EventSaveCallback mCallback;

    public EventServerSaveHelper(Context context) {
        super(context);
    }

    public boolean saveEvent(CalendarEventItem modifyEventItem, CalendarEventItem originalEventItem, CalendarEventInstance eventInstance, final int whichModify) {

        if (modifyEventItem == null) {
            Log.w(TAG, "저장할 데이터가 없습니다.");
            return false;
        }

        if (whichModify != CREATE_NEW_EVENT && !modifyEventItem.isSameEvent(originalEventItem)) {
            Log.w(TAG, "원본이벤트와 수정된 이벤트가 동일이벤트가 아닙니다.");
            return false;
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ContentValues values = modifyEventItem.toContentValues();

        ArrayList<CalendarReminder> reminders = modifyEventItem.getReminder();
        int len = reminders.size();
        values.put(Events.HAS_ALARM, (len > 0) ? 1 : 0);

        boolean forceSaveReminders = false;
        int eventIdIndex = -1;
        CalendarEventItemForm eventItemForm = null;

        String gaString = "";
        switch (whichModify) {
            case CREATE_NEW_EVENT: {
                //새로운 일정 추가
                // ContentProviderOperation을 이용해야 batch 처리 하는데 ContentProviderOperation를 알기전에 개발된 내용이라 추후 수정 필요함.
                //eventQueryHandler.startInsert(CREATE_NEW_EVENT, ceItem, CalendarContract.Events.CONTENT_URI, values);

                values.put(Events.HAS_ATTENDEE_DATA, 1);
                values.put(Events.STATUS, Events.STATUS_CONFIRMED);

                eventIdIndex = 0;

                eventItemForm = new CalendarEventItemForm(values);

                forceSaveReminders = true;

                gaString = "add";
                break;
            }
            case SIMPLE_MODIFY: {
                // 일반일정을 변경
                checkTimeDependentFields(originalEventItem, modifyEventItem, eventInstance, values, whichModify);

                eventItemForm = new CalendarEventItemForm(values);

                gaString = "modifySimple";
                break;
            }
            case MODIFY_NON_REPEAT_TO_REPEAT: {
                // 일반일정을 반복일정으로 변경

                eventItemForm = new CalendarEventItemForm(values);

                gaString = "modifyNormalToRepeat";
                break;
            }
            case MODIFY_SELECTED: {
                // Modify contents of the current instance of repeating event
                // Create a recurrence exception
                long begin = eventInstance.getBegin();
                values.put(Events.ORIGINAL_SYNC_ID, originalEventItem.getSyncId());
                values.put(Events.ORIGINAL_INSTANCE_TIME, begin);
                boolean allDay = originalEventItem.getAllDay();
                values.put(Events.ORIGINAL_ALL_DAY, allDay ? 1 : 0);
                values.put(Events.STATUS, originalEventItem.getEventStatus());

                eventIdIndex = 0;

                eventItemForm = new CalendarEventItemForm(values);

                forceSaveReminders = true;

                break;
            }
            case MODIFY_SELECTED_FOLLOWING: {
                if (TextUtils.isEmpty(modifyEventItem.getRRule())) {
                    // We've changed a recurring event to a non-recurring event.
                    // If the event we are editing is the first in the series,
                    // then delete the whole series. Otherwise, update the series
                    // to end at the new start time.
                    values.put(Events.STATUS, originalEventItem.getEventStatus());
                    eventItemForm = new CalendarEventItemForm(values);

                    if (isFirstEventInSeries(eventInstance, originalEventItem)) {
                        eventItemForm.setIsFirstEventInSeries(true);
                    } else {
                        // Update the current repeating event to end at the new start time.  We
                        // ignore the RRULE returned because the exception event doesn't want one.
                        updatePastEvents(eventItemForm, originalEventItem, eventInstance.getBegin());
                    }
                    eventIdIndex = 0;

                } else {
                    if (isFirstEventInSeries(eventInstance, originalEventItem)) {
                        checkTimeDependentFields(originalEventItem, modifyEventItem, eventInstance, values, whichModify);
                        eventItemForm = new CalendarEventItemForm(values);
                    } else {
                        // We need to update the existing recurrence to end before the exception
                        // event starts.  If the recurrence rule has a COUNT, we need to adjust
                        // that in the original and in the exception.  This call rewrites the
                        // original event's recurrence rule (in "ops"), and returns a new rule
                        // for the exception.  If the exception explicitly set a new rule, however,
                        // we don't want to overwrite it.
                        String newRrule = updatePastEvents(ops, originalEventItem, eventInstance.getBegin());
                        if (modifyEventItem.getRRule().equals(originalEventItem.getRRule())) {
                            values.put(Events.RRULE, newRrule);
                        }

                        // Create a new event with the user-modified fields
                        eventIdIndex = 0;
                        values.put(Events.STATUS, originalEventItem.getEventStatus());
                        eventItemForm = new CalendarEventItemForm(values);
                    }
                }
                forceSaveReminders = true;

                break;
            }
            case MODIFY_ALL: {
                if (TextUtils.isEmpty(modifyEventItem.getRRule())) {
                    // 반복일정을 일반일정으로 변경
                    eventIdIndex = 0;

                    eventItemForm = new CalendarEventItemForm(values);
                    eventItemForm.setIsRepeatToNonRepeat(true);

                    forceSaveReminders = true;

                } else {
                    // 반복일정을 모두 변경
                    checkTimeDependentFields(originalEventItem, modifyEventItem, eventInstance, values, whichModify);
                    eventItemForm = new CalendarEventItemForm(values);
                }

                gaString = "modifyAll";
                break;
            }
        }

        if (whichModify != CREATE_NEW_EVENT)
            eventItemForm.setId(originalEventItem.getId());

        eventItemForm.setWhichModify(whichModify);
        eventItemForm.setEventType(modifyEventItem.getEventType());

        boolean newEvent = (eventIdIndex != -1);

        ArrayList<CalendarReminder> originalReminders;
        if (originalEventItem != null) {
            originalReminders = originalEventItem.getReminder();
        } else {
            originalReminders = new ArrayList<>();
        }

        // reminder
        saveReminders(eventItemForm, reminders, originalReminders, forceSaveReminders);

        boolean hasExtenedProperties = modifyEventItem.hasExtendedProperties();

        if (hasExtenedProperties) {
            for(CalendarExtendedProp prop : modifyEventItem.getExtendedProps()) {
                CalendarExtendedPropertieForm exp = new CalendarExtendedPropertieForm(prop);
                eventItemForm.addExtendedProperties(exp);
            }
        }

        boolean hasAttendeeData = modifyEventItem.getHasAttendeeData();

        if (hasAttendeeData && modifyEventItem.getOwnerAttendeeId() == -1) {
            // Organizer is not an attendee

            String ownerEmail = modifyEventItem.getOwnerAccount();
            if (modifyEventItem.getAttendee().size() != 0 && CalendarEventUtils.isValidEmail(ownerEmail)
                    && !"local".equalsIgnoreCase(modifyEventItem.getAccountType())) {
                // Add organizer as attendee since we got some attendees

                CalendarAttendeeForm af = new CalendarAttendeeForm();
                af.setAttendeeEmail(ownerEmail);
                af.setAttendeeRelationship(Attendees.RELATIONSHIP_ORGANIZER);
                af.setAttendeeType(Attendees.TYPE_REQUIRED);
                af.setAttendeeStatus(Attendees.ATTENDEE_STATUS_ACCEPTED);

                eventItemForm.addAttendee(af);

            }
        } else if (hasAttendeeData &&
                modifyEventItem.getSelfAttendeeStatus() != originalEventItem.getSelfAttendeeStatus() &&
                modifyEventItem.getOwnerAttendeeId() != -1) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Setting attendee status to " + modifyEventItem.getSelfAttendeeStatus());
            }

            for (CalendarAttendee at : modifyEventItem.getAttendee()) {
                if (at.getAttendeeId().equals(modifyEventItem.getOwnerAttendeeId()))
                    at.setAttendeeStatus(modifyEventItem.getSelfAttendeeStatus());
            }
        }

        if (hasAttendeeData && (newEvent || (eventItemForm.getId() != null && eventItemForm.getId() > 0L))) {
            String attendees = modifyEventItem.getAttendeesString();
            String originalAttendeeString;

            if (originalEventItem != null) {
                originalAttendeeString = originalEventItem.getAttendeesString();
            } else {
                originalAttendeeString = "";
            }

            if (newEvent || !TextUtils.equals(originalAttendeeString, attendees)) {

                HashMap<String, CalendarAttendee> newAttendees = new HashMap<>();

                for (CalendarAttendee attendee : modifyEventItem.getAttendee()) {
                    newAttendees.put(attendee.getAttendeeEmail(), attendee);
                }

                if (!newEvent) {
                    ArrayList<CalendarAttendee> originalAttendees = originalEventItem.getAttendee();
                    for (CalendarAttendee originalAttendee : originalAttendees) {
                        CalendarAttendeeForm af = new CalendarAttendeeForm(originalAttendee);
                        if (newAttendees.containsKey(originalAttendee.getAttendeeEmail())) {
                            newAttendees.remove(originalAttendee.getAttendeeEmail());
                        } else {
                            af.setIsDelete(true);
                        }
                        eventItemForm.addAttendee(af);
                    }

                }

                if (newAttendees.size() > 0) {

                    for (CalendarAttendee attendee : newAttendees.values()) {
                        CalendarAttendeeForm af = new CalendarAttendeeForm();
                        af.setAttendeeId(attendee.getAttendeeId());
                        af.setAttendeeName(attendee.getAttendeeName());
                        af.setAttendeeEmail(attendee.getAttendeeEmail());
                        af.setAttendeeRelationship(Attendees.RELATIONSHIP_ATTENDEE);
                        af.setAttendeeType(Attendees.TYPE_REQUIRED);
                        af.setAttendeeStatus( Attendees.ATTENDEE_STATUS_NONE);

                        eventItemForm.addAttendee(af);
                    }

                }

            }
        }

        TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "click", "CalendarEventAdd_"+gaString);

        RequestEntry<CalendarEventItemForm> requestEntry = new RequestEntry<>(eventItemForm);
        TudaApp.phpApi.saveCalendarEvent(requestEntry).enqueue(new ResponseCallback<ResponseEntry<Boolean>>() {

            @Override
            public void onSuccess(Response<ResponseEntry<Boolean>> response) {
                if (mCallback != null)
                    mCallback.onSuccess(response);
            }

            @Override
            public void onDataError(String error, Response<ResponseEntry<Boolean>> response) {
                if (mCallback != null)
                    mCallback.onError(error, response);
            }

            @Override
            public void onNetworkError(Throwable t) {
            }
        });

        return true;
    }

    public EventServerSaveHelper setEventSaveCallback(EventSaveCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public String updatePastEvents(CalendarEventItemForm eventItemForm,
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

        eventItemForm.updateValues(updateValues);

        return newRrule;
    }

    /**
     * Saves the reminders, if they changed. Returns true if operations to
     * update the database were added.
     *
     * @param eventItemForm the array of ContentProviderOperations
     * @param reminders the array of reminders set by the user
     * @param originalReminders the original array of reminders
     * @param forceSave if true, then save the reminders even if they didn't change
     * @return true if operations to update the database were added
     */
    public static boolean saveReminders(CalendarEventItemForm eventItemForm,
                                        ArrayList<CalendarReminder> reminders, ArrayList<CalendarReminder> originalReminders,
                                        boolean forceSave) {
        // If the reminders have not changed, then don't update the database
        if (reminders.equals(originalReminders) && !forceSave) {
            return false;
        }

        eventItemForm.clearReminder();

        ContentValues values = new ContentValues();
        int len = reminders.size();

        // Insert the new reminders, if any
        for (int i = 0; i < len; i++) {
            CalendarReminder re = reminders.get(i);

            CalendarReminderForm rf = new CalendarReminderForm();
            rf.setMinutes(re.getMinutes());
            rf.setMethod(re.getMethod());

            eventItemForm.addReminder(rf);
        }
        return true;
    }

}
