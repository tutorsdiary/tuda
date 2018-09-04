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
import com.tuda.teacher.network.ResponseCallback;
import com.tuda.teacher.network.entity.CalendarEventItemForm;
import com.tuda.teacher.network.entry.RequestEntry;
import com.tuda.teacher.network.entry.ResponseEntry;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class EventServerDeleteHelper extends EventDeleteHelper {

    public interface EventDeleteCallback {
        void onSuccess(Response<ResponseEntry<Boolean>> response);
        void onError(String message, Response<ResponseEntry<Boolean>> response);
    }

    private EventDeleteCallback mCallback;

    public EventServerDeleteHelper(Context context) {
        super(context);
    }

    public EventServerDeleteHelper setEventSaveCallback(EventDeleteCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public void deleteEvent(CalendarEventItem eventItem, CalendarEventInstance instanceItem, int deleteMode) {

        String gaLabel = "delete";

        Long eventId = eventItem.getId();

        String rRule = eventItem.getRRule();
        boolean allDay = eventItem.getAllDay();
        long dtstart = eventItem.getDtStart();
        long id = eventItem.getId();
        long instnaceId = instanceItem.getId();
        long startMillis = instanceItem.getBegin();
        long endMillis = instanceItem.getEnd();
        String syncId = eventItem.getSyncId();

        CalendarEventItemForm eventItemForm = new CalendarEventItemForm();

        switch (deleteMode) {
            case EVENT_DELETE_SELECTED: {
                // 선택한 날짜만 삭제
                // 서버에서는 선택한 날짜에 취소처리된 exception 일정으로 추가

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

                eventItemForm.updateValues(values);

                gaLabel += "Current";
                break;
            }
            case EVENT_DELETE_ALL_FOLLOWING: {
                // 선택한 날짜 포함 이후 일정 모두 삭제
                if (startMillis == dtstart) {
                    // 선택한 날짜가 전체 일정의 시작일때는 모든 일정 삭제
                    eventItemForm.setIsFirstEventInSeries(true);

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
                eventRecurrence.count = 0;

                eventItemForm.setStartDate(dtstart);
                eventItemForm.setRRule(eventRecurrence.toString());

                gaLabel += "After";
                break;
            }
            case EVENT_DELETE_ALL: {
                // 모든 일정 삭제

                gaLabel += "All";
                break;
            }
            case EVENT_DELETE_EXCEPTION_EVENT: {
                // 하루만 편집된 반복일정 일정 삭제
                // 서버에서는 cancel된 일정으로 추가
                eventItemForm.setEventStatus(Events.STATUS_CANCELED);

                gaLabel += "Exeption";

                break;
            }
        }

        eventItemForm.setId(eventId);
        eventItemForm.setDeleteMode(deleteMode);

        RequestEntry<CalendarEventItemForm> request = new RequestEntry<>(eventItemForm);

        TudaApp.phpApi.deleteCalendarEvent(request).enqueue(new ResponseCallback<ResponseEntry<Boolean>>() {

            @Override
            public void onSuccess(Response<ResponseEntry<Boolean>> response) {
                if (mCallback!=null)
                    mCallback.onSuccess(response);
            }

            @Override
            public void onDataError(String error, Response<ResponseEntry<Boolean>> response) {
                if (mCallback!=null)
                    mCallback.onError(error, response);
            }

            @Override
            public void onNetworkError(Throwable t) {

            }
        });

        TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "click", "CalendarEventView_" + gaLabel);
    }

}
