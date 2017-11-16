package com.greason.autotest.utils;

import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;

/**
 * Created by Greason on 11/09/2017.
 */

public class EmptyUtils {

    public static boolean isNotEmpty(UiObject object){
        if (object != null && object.exists()) {
            return true;
        } else {
            LogUtils.d("UiObject is empty.");
            return false;
        }
    }

    public static boolean isNotEmpty(UiObject2 object){
        if (object != null) {
            return true;
        } else {
            LogUtils.d("UiObject2 is empty.");
            return false;
        }
    }

}
