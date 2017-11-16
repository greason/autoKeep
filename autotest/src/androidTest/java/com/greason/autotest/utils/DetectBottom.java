package com.greason.autotest.utils;

import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

/**
 * Created by Greason on 13/09/2017.
 */

public class DetectBottom {

    public UiDevice mDevice;
    UiObject2 scrollView;
    public int height = 0;
    public int paddingHeight = 0;
    public int detectTime = 3;
    public int currentDetectTime = 0;
    public boolean isBottom = false;

    public boolean isDetective = false;

    public DetectBottom(UiDevice mDevice, UiObject2 scrollView) {
        currentDetectTime = 0;
        this.mDevice = mDevice;
        this.scrollView = scrollView;
        isBottom = false;
    }

    public void setPaddingHeight(int paddingHeight) {
        this.paddingHeight = paddingHeight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void prepare(int height) {
        isBottom = false;
        isDetective = false;
        this.height = height;
        currentDetectTime = 0;
    }

    public boolean scroll() {
        if (height == 0 || currentDetectTime == detectTime) {
            return false;
        }
        if (scrollView == null) {
            return false;
        }
        try {
            isBottom = scrollView.scroll(Direction.DOWN, 1.f, height + paddingHeight);
        } catch (Exception e) {
            isBottom = false;
        }

        if (!isBottom) {
            if (currentDetectTime < detectTime) {
                isDetective = true;
                isBottom = true;
                currentDetectTime++;
            } else {
                isDetective = false;
                return false;
            }
        } else {
            isDetective = false;
            return isBottom;
        }
        return isBottom;
    }

    /**
     * 碰撞检测
     * true 碰撞检测中
     *
     * @return
     */
    public boolean isDective() {
        return isDetective;
    }

    public boolean isFirstDetive() {
        if (currentDetectTime == 1) {
            return true;
        }
        return false;
    }

}
