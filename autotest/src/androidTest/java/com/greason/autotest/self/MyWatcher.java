package com.greason.autotest.self;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiWatcher;

/**
 * Created by Greason on 16/09/2017.
 */

public class MyWatcher implements UiWatcher {
    private UiDevice mDevice;

    public MyWatcher(UiDevice device) {
        mDevice = device;
    }

    @Override
    public boolean checkForCondition() {
        if (mDevice.hasObject(By.text("删除安装包"))) {
//            mDevice.pressBack();
            return true;
        } else {

        }
        return true;
    }
}
