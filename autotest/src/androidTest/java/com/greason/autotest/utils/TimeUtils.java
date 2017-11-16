package com.greason.autotest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Greason on 16/09/2017.
 */

public class TimeUtils {

    public static SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String getTimeNow() {
        return sFormat.format(new Date());
    }


}
