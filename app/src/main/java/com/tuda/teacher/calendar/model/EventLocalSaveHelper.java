package com.tuda.teacher.calendar.model;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Attendees;
import android.text.TextUtils;

import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.TudaApp;
import com.tuda.teacher.calendar.model.entity.CalendarAttendee;
import com.tuda.teacher.calendar.model.entity.CalendarEventInstance;
import com.tuda.teacher.calendar.model.entity.CalendarEventItem;
import com.tuda.teacher.calendar.model.entity.CalendarReminder;
import com.tuda.teacher.calendar.util.CalendarEventUtils;
import com.tuda.teacher.common.GACommon;
import com.tuda.teacher.common.Preference;
import com.tuda.teacher.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class EventLocalSaveHelper extends EventSaveHelper {
    private static final String ATTENDEES_DELETE_PREFIX = CalendarContract.Attendees.EVENT_ID + "=? AND " + CalendarContract.Attendees.ATTENDEE_EMAIL + " IN (";

    public EventLocalSaveHelper(Context context) {
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

        Uri uri = null;
        if (modifyEventItem.getUri() != null)
            uri = Uri.parse(modifyEventItem.getUri());

        ArrayList<CalendarReminder> reminders = modifyEventItem.getReminder();
        int len = reminders.size();
        values.put(Events.HAS_ALARM, (len > 0) ? 1 : 0);

        boolean forceSaveReminders = false;
        int eventIdIndex = -1;
        long calendarId = modifyEventItem.getCalendarId();

        String gaString = "";
        switch (whichModify) {
            case CREATE_NEW_EVENT: {
                //새로운 일정 추가
                // ContentProviderOperation을 이용해야 batch 처리 하는데 ContentProviderOperation를 알기전에 개발된 내용이라 추후 수정 필요함.
                //eventQueryHandler.startInsert(CREATE_NEW_EVENT, ceItem, CalendarContract.Events.CONTENT_URI, values);

                values.put(Events.HAS_ATTENDEE_DATA, 1);
                values.put(Events.STATUS, Events.STATUS_CONFIRMED);
                eventIdIndex = ops.size();
                ContentProviderOperation.Builder b = ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values);
                ops.add(b.build());
                forceSaveReminders = true;

                gaString = "add";
                break;
            }
            case SIMPLE_MODIFY: {
                // 일반일정을 변경
                checkTimeDependentFields(originalEventItem, modifyEventItem, eventInstance, values, whichModify);
                ops.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());

                gaString = "modifySimple";
                break;
            }
            case MODIFY_NON_REPEAT_TO_REPEAT: {
                // 일반일정을 반복일정으로 변경

                ops.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
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

                eventIdIndex = ops.size();
                ContentProviderOperation.Builder b = ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values);
                ops.add(b.build());
                forceSaveReminders = true;

                break;
            }
            case MODIFY_SELECTED_FOLLOWING: {
                if (TextUtils.isEmpty(modifyEventItem.getRRule())) {
                    // We've changed a recurring event to a non-recurring event.
                    // If the event we are editing is the first in the series,
                    // then delete the whole series. Otherwise, update the series
                    // to end at the new start time.
                    if (isFirstEventInSeries(eventInstance, originalEventItem)) {
                        ops.add(ContentProviderOperation.newDelete(uri).build());
                    } else {
                        // Update the current repeating event to end at the new start time.  We
                        // ignore the RRULE returned because the exception event doesn't want one.
                        updatePastEvents(ops, originalEventItem, eventInstance.getBegin());
                    }
                    eventIdIndex = ops.size();
                    values.put(Events.STATUS, originalEventItem.getEventStatus());
                    ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values)
                            .build());
                } else {
                    if (isFirstEventInSeries(eventInstance, originalEventItem)) {
                        checkTimeDependentFields(originalEventItem, modifyEventItem, eventInstance, values, whichModify);
                        ContentProviderOperation.Builder b = ContentProviderOperation.newUpdate(uri)
                                .withValues(values);
                        ops.add(b.build());
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
                        eventIdIndex = ops.size();
                        values.put(Events.STATUS, originalEventItem.getEventStatus());
                        ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values).build());
                    }
                }
                forceSaveReminders = true;

                break;
            }
            case MODIFY_ALL: {
                if (TextUtils.isEmpty(modifyEventItem.getRRule())) {
                    // 반복일정을 일반일정으로 변경
                    ops.add(ContentProviderOperation.newDelete(uri).build());

                    eventIdIndex = ops.size();

                    ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values).build());

                    forceSaveReminders = true;

                } else {
                    // 반복일정을 모두 변경
                    checkTimeDependentFields(originalEventItem, modifyEventItem, eventInstance, values, whichModify);
                    ops.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
                }

                gaString = "modifyAll";
                break;
            }
        }

        boolean newEvent = (eventIdIndex != -1);

        ArrayList<CalendarReminder> originalReminders;
        if (originalEventItem != null) {
            originalReminders = originalEventItem.getReminder();
        } else {
            originalReminders = new ArrayList<>();
        }

        // reminder
        if (newEvent) {
            saveRemindersWithBackRef(ops, eventIdIndex, reminders, originalReminders, forceSaveReminders);
        } else {
            long eventId = ContentUris.parseId(uri);
            saveReminders(ops, eventId, reminders, originalReminders, forceSaveReminders);
        }

        ContentProviderOperation.Builder b;

        boolean hasAttendeeData = modifyEventItem.getHasAttendeeData();

        if (hasAttendeeData && modifyEventItem.getOwnerAttendeeId() == -1) {
            // Organizer is not an attendee

            String ownerEmail = modifyEventItem.getOwnerAccount();
            if (modifyEventItem.getAttendee().size() != 0 && CalendarEventUtils.isValidEmail(ownerEmail)
                    && !"local".equalsIgnoreCase(modifyEventItem.getAccountType())) {
                // Add organizer as attendee since we got some attendees

                values.clear();
                values.put(Attendees.ATTENDEE_EMAIL, ownerEmail);
                values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ORGANIZER);
                values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_REQUIRED);
                values.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_ACCEPTED);

                if (newEvent) {
                    b = ContentProviderOperation.newInsert(Attendees.CONTENT_URI)
                            .withValues(values);
                    b.withValueBackReference(Attendees.EVENT_ID, eventIdIndex);
                } else {
                    values.put(Attendees.EVENT_ID, modifyEventItem.getId());
                    b = ContentProviderOperation.newInsert(Attendees.CONTENT_URI)
                            .withValues(values);
                }
                ops.add(b.build());
            }
        } else if (hasAttendeeData &&
                modifyEventItem.getSelfAttendeeStatus() != originalEventItem.getSelfAttendeeStatus() &&
                modifyEventItem.getOwnerAttendeeId() != -1) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Setting attendee status to " + modifyEventItem.getSelfAttendeeStatus());
            }
            Uri attUri = ContentUris.withAppendedId(Attendees.CONTENT_URI, modifyEventItem.getOwnerAttendeeId());

            values.clear();
            values.put(Attendees.ATTENDEE_STATUS, modifyEventItem.getSelfAttendeeStatus());
            values.put(Attendees.EVENT_ID, modifyEventItem.getId());
            b = ContentProviderOperation.newUpdate(attUri).withValues(values);
            ops.add(b.build());
        }

        if (hasAttendeeData && (newEvent || uri != null)) {
            String attendees = modifyEventItem.getAttendeesString();
            String originalAttendeeString;

            if (originalEventItem != null) {
                originalAttendeeString = originalEventItem.getAttendeesString();
            } else {
                originalAttendeeString = "";
            }

            if (newEvent || !TextUtils.equals(originalAttendeeString, attendees)) {

                HashMap<String, CalendarAttendee> newAttendees = new HashMap<>();
                LinkedList<String> removeAttendees = new LinkedList<>();

                for (CalendarAttendee attendee : modifyEventItem.getAttendee()) {
                    newAttendees.put(attendee.getAttendeeEmail(), attendee);
                }

                long eventId = uri != null ? ContentUris.parseId(uri) : -1;

                if (!newEvent) {
                    removeAttendees.clear();
                    ArrayList<CalendarAttendee> originalAttendees = originalEventItem.getAttendee();
                    for (CalendarAttendee originalAttendee : originalAttendees) {
                        if (newAttendees.containsKey(originalAttendee.getAttendeeEmail())) {
                            newAttendees.remove(originalAttendee.getAttendeeEmail());
                        } else {
                            removeAttendees.add(originalAttendee.getAttendeeEmail());
                        }
                    }

                    if (removeAttendees.size() > 0) {
                        b = ContentProviderOperation.newDelete(Attendees.CONTENT_URI);

                        String[] args = new String[removeAttendees.size() + 1];
                        args[0] = Long.toString(eventId);

                        int i = 1;

                        StringBuilder deleteWhere = new StringBuilder(ATTENDEES_DELETE_PREFIX);
                        for (String removeAttendee : removeAttendees) {
                            if (i > 1) {
                                deleteWhere.append(",");
                            }
                            deleteWhere.append("?");
                            args[i++] = removeAttendee;
                        }

                        deleteWhere.append(")");
                        b.withSelection(deleteWhere.toString(), args);
                        ops.add(b.build());

                    }
                }

                if (newAttendees.size() > 0) {

                    for (CalendarAttendee attendee : newAttendees.values()) {
                        values.clear();
                        values.put(Attendees.ATTENDEE_NAME, attendee.getAttendeeName());
                        values.put(Attendees.ATTENDEE_EMAIL, attendee.getAttendeeEmail());
                        values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ATTENDEE);
                        values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_REQUIRED);
                        values.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_NONE);

                        if (newEvent) {
                            b = ContentProviderOperation.newInsert(Attendees.CONTENT_URI).withValues(values);
                            b.withValueBackReference(Attendees.EVENT_ID, eventIdIndex);
                        } else {
                            values.put(Attendees.EVENT_ID, eventId);
                            b = ContentProviderOperation.newInsert(Attendees.CONTENT_URI).withValues(values);
                        }

                        ops.add(b.build());
                    }

                }

            }
        }

        TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "click", "CalendarEventAdd_"+gaString);

        mService.startBatch(mService.getNextToken(), null, CalendarContract.AUTHORITY, ops, 0);

        Preference.setDefaultSyncCalendarId(calendarId);

        return true;
    }

}
