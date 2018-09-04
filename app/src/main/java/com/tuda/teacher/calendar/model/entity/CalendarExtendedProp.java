package com.tuda.teacher.calendar.model.entity;

import android.content.ContentValues;
import android.provider.CalendarContract;

import com.tuda.teacher.network.entity.CalendarExtendedPropertieForm;

import java.io.Serializable;

/**
 * Created by crazyluv on 2015. 11. 25..
 */
public class CalendarExtendedProp implements Serializable {

    private static final long serialVersionUID = -162170185438137471L;
    protected static final String TAG = "CalendarExtendedProp";

    public static final String MEMO = "MEMO";
    public static final String IS_TUDA = "IST";
    public static final String SUBJECT = "SBJ";
    public static final String LESSON_RANGE = "LR";
    public static final String HOMEWORK = "HW";
    public static final String LESSON_TYPE = "LT";
    public static final String LESSON_COUNT = "LC";
    public static final String LESSON_COUNT_MAX = "LCM";

    protected Long id;
    protected Long event_id;
    protected String name;
    protected String value;

    public CalendarExtendedProp() {}
    public CalendarExtendedProp(CalendarExtendedPropertieForm form) {
        this.id = form.getId();
        this.event_id = form.getEventId();
        this.name = form.getName();
        this.value = form.getValue();
    }
    public CalendarExtendedProp(ContentValues values) {
        this.id = values.getAsLong(CalendarContract.ExtendedProperties._ID);
        this.event_id = values.getAsLong(CalendarContract.ExtendedProperties.EVENT_ID);
        this.name = values.getAsString(CalendarContract.ExtendedProperties.NAME);
        this.value = values.getAsString(CalendarContract.ExtendedProperties.VALUE);
    }

    public CalendarExtendedProp(String name, String value) {
        this(0L, name, value);
    }

    public CalendarExtendedProp(Long id, String name, String value) {
        this.id = 0L;
        this.event_id = id;
        this.name = name;
        this.value = value;
    }

    public void setId(Long srl) {
        this.id = srl;
    }

    public void setEventId(Long id) {
        this.event_id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return this.id;
    }

    public Long getEventId() {
        return this.event_id;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public static String convertPropName(String oldPropName) {
        if ("SUBJECT".equals(oldPropName))
            return CalendarExtendedProp.SUBJECT;
        else if ("LESSON_RANGE".equals(oldPropName))
            return CalendarExtendedProp.LESSON_RANGE;
        else if ("HOMEWORK".equals(oldPropName))
            return CalendarExtendedProp.HOMEWORK;

        return null;
    }
}
