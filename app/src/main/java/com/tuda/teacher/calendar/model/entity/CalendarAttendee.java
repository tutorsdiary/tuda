package com.tuda.teacher.calendar.model.entity;

import android.content.ContentValues;
import android.provider.CalendarContract;
import android.text.TextUtils;

import java.io.Serializable;


/**
 * Created by crazyluv on 2015. 11. 25..
 */
public class CalendarAttendee implements Serializable, Cloneable {

    private static final long serialVersionUID = -7590127925182079611L;
    protected static final String TAG = "CalendarAttendee";

    public static final String ATTENDEE_ID = "com.tuda.calendar.attendee_id";
    public static final String ATTENDEE_PICTURE = "com.tuda.calendar.attendee_picture";

    public static final String EMAIL_POSTFIX = "_tuda@tutorsdiary.com";

    protected int srl;
    protected long event_id;
    protected Long attendeeId;
    protected String attendee_name;
    protected String attendee_email;
    protected int attendee_relationship;
    protected int attendee_type;
    protected int attendee_status;

    public CalendarAttendee() { this.event_id = -1L; }
    public CalendarAttendee(ContentValues values) {
        this.srl = values.getAsInteger(CalendarContract.Attendees._ID);
        this.event_id = values.getAsLong(CalendarContract.Attendees.EVENT_ID);
        this.attendee_name = values.getAsString(CalendarContract.Attendees.ATTENDEE_NAME);
        this.attendee_email = values.getAsString(CalendarContract.Attendees.ATTENDEE_EMAIL);
        this.attendee_relationship = values.getAsInteger(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP);
        this.attendee_type = values.getAsInteger(CalendarContract.Attendees.ATTENDEE_TYPE);
        this.attendee_status = values.getAsInteger(CalendarContract.Attendees.ATTENDEE_STATUS);
    }

    public CalendarAttendee(long event_id) {
        this.event_id = event_id;
    }

    public CalendarAttendee(long event_id, String name, String email) {
        this.srl = -1;
        this.event_id = event_id;
        this.attendee_name = name;
        this.attendee_email = email;
    }

    public void setSrl(int srl) {
        this.srl = srl;
    }

    public int getSrl() {
        return this.srl;
    }

    public void setEventId(Long id) {
        this.event_id = id;
    }

    public long getEventId() {
        return this.event_id;
    }

    public void setAttendeeId(long memberSrl) {
        this.attendeeId = memberSrl;
    }
    public Long getAttendeeId() {

        if (this.attendeeId != null && this.attendeeId > 0)
            return this.attendeeId;

        String[] aId = this.attendee_email.split("_");

        if (aId.length != 2 || TextUtils.isEmpty(aId[1]))
            return null;

        try {
            this.attendeeId = Long.valueOf(aId[1], 16);
            return this.attendeeId;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setAttendeeName(String name) {
        this.attendee_name = name;
    }

    public String getAttendeeName() {
        return this.attendee_name;
    }

    public void setAttendeeEmail(String email) {
        this.attendee_email = email;
    }

    public String getAttendeeEmail() {
        return this.attendee_email;
    }

    public void setAttendeeRelationShip(int relationShip) {
        this.attendee_relationship = relationShip;
    }

    public int getAttendeeRelationShip() {
        return this.attendee_relationship;
    }

    public void setAttendeeType(int type) {
        this.attendee_type = type;
    }

    public int getAttendeeType() {
        return this.attendee_type > 0 ? this.attendee_type : CalendarContract.Attendees.TYPE_REQUIRED;
    }

    public void setAttendeeStatus(Integer status) {
        this.attendee_status = status;
    }

    public int getAttendeeStatus() {
        return (this.attendee_status > -1) ? this.attendee_status : CalendarContract.Attendees.ATTENDEE_STATUS_NONE;
    }

    public ContentValues toContentValues(long eventID) {

        ContentValues values = new ContentValues();

        if (getSrl() > 0)
            values.put(CalendarContract.Attendees._ID, getSrl());
        if (eventID > 0)
            values.put(CalendarContract.Attendees.EVENT_ID, eventID);
        values.put(CalendarContract.Attendees.ATTENDEE_NAME, getAttendeeName());
        values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, getAttendeeEmail());
        values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, getAttendeeRelationShip());
        values.put(CalendarContract.Attendees.ATTENDEE_TYPE, getAttendeeType());
        values.put(CalendarContract.Attendees.ATTENDEE_STATUS, getAttendeeStatus());

        return values;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        CalendarAttendee attendee = (CalendarAttendee) super.clone();
        attendee.srl = srl;
        attendee.event_id = event_id;
        attendee.attendee_name = attendee_name;
        attendee.attendee_email = attendee_email;
        attendee.attendee_relationship = attendee_relationship;
        attendee.attendee_type = attendee_type;
        attendee.attendee_status = attendee_status;

        return attendee;
    }
}
