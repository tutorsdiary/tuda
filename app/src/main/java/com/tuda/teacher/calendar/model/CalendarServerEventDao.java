package com.tuda.teacher.calendar.model;

import android.content.ContentResolver;
import android.content.Context;

import com.tuda.teacher.calendar.model.entity.CalendarEventItem;
import com.tuda.teacher.calendar.model.entity.CalendarItem;

import retrofit2.Callback;

/**
 * Created by crazyluv on 2015. 12. 14..
 */
public class CalendarServerEventDao extends BaseDao {

    private Context mContext;
    private ContentResolver mContentResolver;
    private CalendarDaoManager mManager;
    private static final CalendarItem calendarItem = new CalendarItem();

    public CalendarServerEventDao(Context context, CalendarDaoManager manager) {
        mContext = context;
        mManager = manager;
        mContentResolver = manager.getContentResolver();
    }

    public void save(CalendarEventItem item, final Callback callback) {


    }

}
