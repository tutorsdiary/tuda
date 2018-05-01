package com.tuda.teacher.util;

import com.crashlytics.android.Crashlytics;
import com.tuda.teacher.BuildConfig;
import com.tuda.teacher.common.Tuda;

import java.io.*;


public class ArchiveManager {
    private static final String rootPath = Tuda.DATABASE_FILE_PATH;
    private static final String rootPathTemp = rootPath + File.separator + "temp";
    private static final String fileNormal = "TUDA_DATABASE.zip";
    private static final String fileName1 = "TUDA_DB.sql";
    private static final String fileName2 = "TUDA_DB.sql-journal";

    static {
        // 필요한 폴더 생성
        File database = new File(rootPath);
        File databaseTemp = new File(rootPathTemp);
        if( !database.exists() ) database.mkdirs();
        if( !databaseTemp.exists() ) databaseTemp.mkdirs();
    }

    public static void init() {
        // do nothing...
    }
    public static void forceInit() {
        try {
            new File(rootPath, fileName1).delete();
            new File(rootPath, fileName2).delete();
            new File(rootPathTemp, fileNormal).delete();
        } catch (Exception e) {
            if (!BuildConfig.DEBUG)
                Crashlytics.logException(e);
            else
                e.printStackTrace();
        }
    }
}
