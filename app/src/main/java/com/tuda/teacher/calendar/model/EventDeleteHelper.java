package com.tuda.teacher.calendar.model;

import android.content.Context;

/**
 * Created by crazyluv on 2016. 1. 25..
 */
public class EventDeleteHelper extends BaseHelper {

    // 이벤트 삭제 모드
    public static final int EVENT_DELETE_SELECTED = 1;
    public static final int EVENT_DELETE_ALL_FOLLOWING = 2;
    public static final int EVENT_DELETE_ALL = 3;
    public static final int EVENT_DELETE_EXCEPTION_EVENT = 4;

    public EventDeleteHelper(Context context) {
        super(context);
    }
}
