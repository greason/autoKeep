package com.greason.autotest;

/**
 * Created by user on 16-4-19.
 */

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.greason.autotest.self.MyWatcher;
import com.greason.autotest.utils.DetectBottom;
import com.greason.autotest.utils.EmptyUtils;
import com.greason.autotest.utils.FileUtils;
import com.greason.autotest.utils.TimeUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class DemoTest extends InstrumentationTestCase {
    private UiDevice mDevice;
    private static final String MM_PACKAGE = "com.tencent.mm";
    private static final int LAUNCH_TIMEOUT = 5000;

    private Context mContext;
    private static String BASIC_SAMPLE_PACKAGE = "com.gotokeep.keep";
    private String pathShoot = "";
    private String msg = "msg.txt";

    private String commentContent = "哇，wonderful";
    private int executeTime = 100;

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);
        mContext = instrumentation.getContext();
        pathShoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar +
                "AGreason" + File.separatorChar + "keep";
        if (!new File(pathShoot).exists()) {
            new File(pathShoot).mkdirs();
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar +
                "AGreason" + File.separatorChar + msg;
        if (new File(path).exists()) {
            String content = FileUtils.readByBufferedReader(path);
            if (content != null && content.length() > 0) {
                commentContent = content;
            }
        }
//        mDevice.pressHome();
        try {
            if (!mDevice.isScreenOn()) {
                mDevice.wakeUp();
            }
            mDevice.freezeRotation();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testKeep() {
        mDevice.pressDPadCenter();
        int height = mDevice.getDisplayHeight();
        int width = mDevice.getDisplayWidth();

        mDevice.resetWatcherTriggers();
        mDevice.registerWatcher("myWatcher", new MyWatcher(mDevice));
        //有时第一次启动后后续事件不执行
        try {
            startApp();
        } catch (Exception e) {
            Log.e("error", "请先安装keep");
            return;
        }

       /* takeShootAction();
        if (!enterDynaList()) {
            enterDynaList();
        }*/

        mDevice.removeWatcher("myWatcher");
    }

    public void startApp() {
        if (mDevice == null) {
            return;
        }
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        mContext.startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
        mDevice.waitForIdle();
        mDevice.waitForWindowUpdate(BASIC_SAMPLE_PACKAGE, 3);

        //跳过
        UiObject2 state = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "circle_progress_in_splash"));
        if (EmptyUtils.isNotEmpty(state)) {
            state.clickAndWait(Until.newWindow(), 3);
        }

    }

    //动态列表
    public boolean enterDynaList() {
        boolean enter = false;
        UiObject2 state = mDevice.findObject(By.text("动态"));
        if (EmptyUtils.isNotEmpty(state)) {
            state.clickAndWait(Until.newWindow(), 3);
            mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

            int time = 0;
            UiObject2 rv = mDevice.findObject(By.clazz(RecyclerView.class));
            while (EmptyUtils.isNotEmpty(rv)) {
                try {
                    enterDynaDetail();
                    rv.scroll(Direction.DOWN, 1.f, rv.getVisibleBounds().height());
                } catch (Exception e) {
                }
                time++;
                if (time > executeTime) {
                    break;
                }
            }
            return true;
        }
        return enter;
    }

    //动态详情
    public boolean enterDynaDetail() {
        boolean enter = false;

        UiObject2 rv = mDevice.findObject(By.clazz(RecyclerView.class));
        if (!EmptyUtils.isNotEmpty(rv)) {
            return false;
        }
        DetectBottom detectBottom = new DetectBottom(mDevice, rv);
        detectBottom.prepare(mDevice.getDisplayHeight());
        boolean first = true;
        do {
            if (!detectBottom.isDective() || detectBottom.isFirstDetive()) {
                List<UiObject2> list = mDevice.findObjects(By.res(BASIC_SAMPLE_PACKAGE, "container_content"));
                int limit = 4;
                if (first) {
                    limit = 2;
                    first = false;
                }
                if (list.size() < limit) {
                    limit = list.size();
                }
                for (int i = 0; i < limit; i++) {
                    UiObject2 a = list.get(i);
                    if (EmptyUtils.isNotEmpty(a)) {
                        try {
                            a.clickAndWait(Until.newWindow(), 3000);
                            sleepAction(5);
                            enterDynaDetailList();
                            UiObject2 left_button = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "left_button"));
                            if (EmptyUtils.isNotEmpty(left_button)) {
                                try {
                                    left_button.clickAndWait(Until.newWindow(), 3000);
                                } catch (Exception e) {
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } while (detectBottom.scroll());
        sleepAction(3);
        return enter;
    }


    //动态详情列表
    public void enterDynaDetailList() {
        if (mDevice == null) {
            return;
        }
        UiObject2 rv = mDevice.findObject(By.clazz(RecyclerView.class));
        if (EmptyUtils.isNotEmpty(rv)) {
            DetectBottom detectBottom = new DetectBottom(mDevice, rv);
            detectBottom.prepare(rv.getVisibleBounds().height());
            do {
                if (!detectBottom.isDective() || detectBottom.isFirstDetive()) {

                    boolean flag = false;

                    if (flag) {
                        List<UiObject2> children = mDevice.findObjects(By.res(BASIC_SAMPLE_PACKAGE, "root_view"));
                        if (children.size() > 0) {
                            UiObject2 child = children.get(0);
                            if (EmptyUtils.isNotEmpty(child)) {
                                UiObject2 comments = child.findObject(By.res(BASIC_SAMPLE_PACKAGE, "layout_child_comment_content"));
                                UiObject2 reply = child.findObject(By.res(BASIC_SAMPLE_PACKAGE, "item_detail_reply_doreply"));
//                            if (EmptyUtils.isNotEmpty(comments)) {
//                                comments.clickAndWait(Until.newWindow(), 3000);
//                                UiObject2 msg = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "edit_input"));
//                                if (EmptyUtils.isNotEmpty(msg)) {
//                                    msg.setText("哇，wonderful");
//                                }
//                                sleepAction(3);
//                                mDevice.pressBack();
//                            } else
                                if (EmptyUtils.isNotEmpty(reply)) {
                                    try {
                                        reply.clickAndWait(Until.newWindow(), 3000);
                                        UiObject2 msg = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "edit_input"));
                                        if (EmptyUtils.isNotEmpty(msg)) {
                                            msg.setText(commentContent);
                                        }
                                        sleepAction(3);
                                        mDevice.pressBack();
                                        sleepAction(3);
                                    } catch (Exception e) {

                                    }
                                }
                                detectBottom.setHeight(child.getVisibleBounds().height());
                            }
                        } else {
                            detectBottom.setHeight(rv.getVisibleBounds().height());
                        }
                    }

                    if (flag) {
                        continue;
                    }

                    //多条评论
                    List<UiObject2> multiList = mDevice.findObjects(By.res(BASIC_SAMPLE_PACKAGE, "layout_child_comment_content"));
                    for (int i = 0; i < multiList.size(); i++) {
                        UiObject2 more = multiList.get(i).findObject(By.res(BASIC_SAMPLE_PACKAGE, "text_child_comment_count"));
                        if (EmptyUtils.isNotEmpty(more)) {
                            try {
                                more.clickAndWait(Until.newWindow(), 3000);
                                singleRvComment();
                            } catch (Exception e) {

                            }
                        } else if (EmptyUtils.isNotEmpty(multiList.get(i))) {
                            try {
                                multiList.get(i).clickAndWait(Until.newWindow(), 3000);
                                singleRvComment();
                            } catch (Exception e) {

                            }
                        }
                    }
                    //单个评论
                    List<UiObject2> singleList = mDevice.findObjects(By.res(BASIC_SAMPLE_PACKAGE, "item_detail_reply_doreply"));
                    for (int i = 0; i < singleList.size(); i++) {
                        if (EmptyUtils.isNotEmpty(singleList.get(i))) {
                            try {
                                singleList.get(i).clickAndWait(Until.newWindow(), 3000);

                                UiObject2 msg = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "edit_input"));
                                if (EmptyUtils.isNotEmpty(msg)) {
                                    msg.setText(commentContent);
                                    sleepAction(3);
                                    UiObject2 send = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "btn_send"));
                                    if (EmptyUtils.isNotEmpty(send)) {
                                        takeShootAction();
                                        send.clickAndWait(Until.newWindow(), 3000);
                                    }
                                }
                                mDevice.pressBack();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            } while (detectBottom.scroll());
            sleepAction(3);
        }
    }

    public void singleRvComment() {
        UiObject2 rv = mDevice.findObject(By.clazz(RecyclerView.class));
        if (EmptyUtils.isNotEmpty(rv)) {
            DetectBottom detectBottom = new DetectBottom(mDevice, rv);
            detectBottom.prepare(rv.getVisibleBounds().height());

            boolean needFirst = false;
            List<UiObject2> children = mDevice.findObjects(By.res(BASIC_SAMPLE_PACKAGE, "root_view"));
            int height = 0;
            if (children.size() > 0) {
                for (int i = 0; i < children.size(); i++) {
                    if (EmptyUtils.isNotEmpty(children.get(i))) {
                        height += children.get(i).getVisibleBounds().height();
                    }
                }
            }
            if (height >= rv.getVisibleBounds().height()) {
                needFirst = true;
            }
            do {
                if (!detectBottom.isDective() || (needFirst && detectBottom.isFirstDetive())) {
                    List<UiObject2> singleList = mDevice.findObjects(By.res(BASIC_SAMPLE_PACKAGE, "img_reply"));
                    for (int i = 0; i < singleList.size(); i++) {
                        if (EmptyUtils.isNotEmpty(singleList.get(i))) {
                            try {
                                singleList.get(i).clickAndWait(Until.newWindow(), 3000);

                                UiObject2 msg = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "edit_input"));
                                if (EmptyUtils.isNotEmpty(msg)) {
                                    msg.setText(commentContent);
                                    sleepAction(3);
                                    UiObject2 send = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "btn_send"));
                                    if (EmptyUtils.isNotEmpty(send)) {
                                        takeShootAction();
                                        send.clickAndWait(Until.newWindow(), 3000);
                                    }
                                }
                                UiObject2 left_button = mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "left_button"));
                                if (EmptyUtils.isNotEmpty(left_button)) {
                                    try {
                                        left_button.clickAndWait(Until.newWindow(), 3000);
                                    } catch (Exception e) {
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } while (detectBottom.scroll());
        }
        sleepAction(3);
        mDevice.pressBack();
    }

    public void takeShootAction() {
        if (mDevice != null) {
            if (!new File(pathShoot).exists()) {
                new File(pathShoot).mkdirs();
            }
            mDevice.takeScreenshot(new File(pathShoot + File.separatorChar +
                    TimeUtils.getTimeNow() + ".png"));
        }
    }

    public void sleepAction(int time) {
        if (time <= 0) {
            return;
        }
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            mDevice.unfreezeRotation();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//        mDevice.pressHome();

    }
}
