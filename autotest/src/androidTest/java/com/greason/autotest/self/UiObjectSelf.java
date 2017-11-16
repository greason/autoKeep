package com.greason.autotest.self;

import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

/**
 * Created by Greason on 11/09/2017.
 */

public class UiObjectSelf extends UiObject {

    public UiObjectSelf(UiSelector selector) {
        super(selector);
    }

    @Override
    public UiObject getChild(UiSelector selector) {
        try {
            return super.getChild(selector);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UiObject getFromParent(UiSelector selector) {
        try {
            return super.getFromParent(selector);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getChildCount() {
        try {
            return super.getChildCount();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean clickAndWaitForNewWindow() {
        try {
            return super.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean clickAndWaitForNewWindow(long timeout) {
        try {
            return super.clickAndWaitForNewWindow(timeout);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean click() {
        try {
            return super.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getText() {
        try {
            return super.getText();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean setText(String text) {
        try {
            return super.setText(text);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
