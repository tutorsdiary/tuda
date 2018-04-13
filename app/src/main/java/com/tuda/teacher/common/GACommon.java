package com.tuda.teacher.common;

import java.util.HashMap;

public class GACommon {
    public static final HashMap<String, String> eventCodeMap;  //  이벤트 카테고리 정보
    static {
        eventCodeMap = new HashMap<String, String>();

        eventCodeMap.put("CA", "common_action");
        eventCodeMap.put("GNBA", "gnb_action");
        eventCodeMap.put("SNBA", "subnav_action");
        eventCodeMap.put("FABA", "floatingbutton_action");
    }
}
