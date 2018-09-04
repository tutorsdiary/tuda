package com.tuda.teacher.calendar.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.text.format.Time;

import com.tuda.teacher.TudaApp;
import com.tuda.teacher.calendar.EventRecurrence;
import com.tuda.teacher.calendar.model.entity.CalendarEventInstance;
import com.tuda.teacher.calendar.model.entity.CalendarEventItem;
import com.tuda.teacher.common.GACommon;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class EventLocalDeleteHelper extends EventDeleteHelper {

    public EventLocalDeleteHelper(Context context) {
        super(context);
    }

    public void deleteEvent(CalendarEventItem eventItem, CalendarEventInstance instanceItem, int deleteMode) {

        String gaLabel = "delete";

        Long eventId = eventItem.getId();

        String rRule = eventItem.getRRule();
        boolean allDay = eventItem.getAllDay();
        long dtstart = eventItem.getDtStart();
        long id = eventItem.getId();
        long startMillis = instanceItem.getBegin();
        long endMillis = instanceItem.getEnd();
        String syncId = eventItem.getSyncId();


        switch (deleteMode) {
            case EVENT_DELETE_SELECTED: {
                // 선택한 날짜만 삭제

                ContentValues values = new ContentValues();
                String title = eventItem.getTitle();
                values.put(Events.TITLE, title);

                String timezone = eventItem.getEventTimezone();
                long calendarId = eventItem.getCalendarId();

                values.put(Events.EVENT_TIMEZONE, timezone);
                values.put(Events.ALL_DAY, allDay ? 1 : 0);
                values.put(Events.ORIGINAL_ALL_DAY, allDay ? 1 : 0);
                values.put(Events.CALENDAR_ID, calendarId);
                values.put(Events.DTSTART, startMillis);
                values.put(Events.DTEND, endMillis);
                values.put(Events.ORIGINAL_SYNC_ID, syncId);
                values.put(Events.ORIGINAL_ID, id);
                values.put(Events.ORIGINAL_INSTANCE_TIME, startMillis);
                values.put(Events.STATUS, Events.STATUS_CANCELED);

                mService.startInsert(mService.getNextToken(), null, Events.CONTENT_URI, values, 0);

                gaLabel += "Current";
                break;
            }
            case EVENT_DELETE_ALL_FOLLOWING: {
                // 선택한 날짜 포함 이후 일정 모두 삭제
                if (startMillis == dtstart) {
                    // 선택한 날짜가 전체 일정의 시작일때는 모든 일정 삭제
                    Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
                    mService.startDelete(mService.getNextToken(), null, uri, null, null, 0);

                    gaLabel += "AfterAll";
                    break;
                }

                // 오늘 포함 이후 일정 삭제 - 반복룰을 선택한 날짜의 시작시간보다 1초 전으로 이동
                EventRecurrence eventRecurrence = new EventRecurrence();
                eventRecurrence.parse(rRule);
                Time date = new Time();
                if (allDay) {
                    date.timezone = Time.TIMEZONE_UTC;
                }
                date.set(startMillis);
                date.second--;
                date.normalize(false);

                date.switchTimezone(Time.TIMEZONE_UTC);
                eventRecurrence.until = date.format2445();

                ContentValues values = new ContentValues();
                values.put(Events.DTSTART, dtstart);
                values.put(Events.RRULE, eventRecurrence.toString());
                Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
                mService.startUpdate(mService.getNextToken(), null, uri, values, null, null, 0);

                gaLabel += "After";
                break;
            }
            case EVENT_DELETE_ALL: {
                // 모든 일정 삭제
                Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
                mService.startDelete(mService.getNextToken(), null, uri, null, null, 0);

                gaLabel += "All";
                break;
            }
            case EVENT_DELETE_EXCEPTION_EVENT: {
                // 하루만 편집된 반복일정 일정 삭제
                ContentValues values = new ContentValues();
                values.put(Events.STATUS, Events.STATUS_CANCELED);

                Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
                mService.startUpdate(mService.getNextToken(), null, uri, values, null, null, 0);

                gaLabel += "Exeption";

                break;
            }
        }


        TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "click", "CalendarEventView_" + gaLabel);
    }

}
