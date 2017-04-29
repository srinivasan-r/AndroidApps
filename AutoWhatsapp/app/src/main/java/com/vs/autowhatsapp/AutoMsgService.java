package com.vs.autowhatsapp;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by 305022193 on 2/17/2017.
 */

public class AutoMsgService extends AccessibilityService {
    public static boolean sActive = false;
    public static String sContact = "Empty";
    public static String sMsg = "Empty";
    private static AutoMsgService sInstance = null;
    public final String TAG = "AutoMsgService";

    public static AutoMsgService getInstance() {
        return sInstance;
    }

    private String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                return "TYPE_VIEW_HOVER_ENTER";
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                return "TYPE_VIEW_HOVER_EXIT";
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                return "TYPE_VIEW_SCROLLED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "TYPE_WINDOW_CONTENT_CHANGED";
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                return "TYPE_ANNOUNCEMENT";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                return "TYPE_GESTURE_DETECTION_START";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                return "TYPE_GESTURE_DETECTION_END";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                return "TYPE_TOUCH_INTERACTION_START";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                return "TYPE_TOUCH_INTERACTION_END";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                return "TYPE_WINDOWS_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
        }

        return "default";
    }

    private boolean printChildNodes(AccessibilityNodeInfo nodeInfo, String spaces) {
        Log.i(TAG, spaces + "Classname " + nodeInfo.getClassName().toString() + ", " + nodeInfo.getViewIdResourceName());
        int childCount = nodeInfo.getChildCount();
        if (childCount == 0)
            return false;
        String nodeText = "";
        if (nodeInfo.getText() != null)
            nodeText = nodeInfo.getText().toString();
        Log.i(TAG, spaces + nodeText + " child count " + String.valueOf(childCount));
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
            if (childNode == null)
                continue;
            if (childNode.getText() != null) {
                Log.i(TAG, spaces + "child:" + String.valueOf(i) + "-" + childNode.getText().toString());
            }
            if (printChildNodes(childNode, spaces + "  "))
                return true;
        }
        return false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!sActive || event.getSource() == null) {
            Log.i(TAG, "Quitting " + sActive + event.getSource());
            return;
        }
        AccessibilityNodeInfo nodeInfo = event.getSource().getParent();
        if (nodeInfo == null) {
            Log.i(TAG, "Parent is null");
            nodeInfo = event.getSource();
        }
        Log.i(TAG, "onAccessibilityEvent " + getEventType(event));
        Log.i(TAG, "onAccessibilityEvent " + getPackageName());
        Log.i(TAG, "onAccessibilityEvent " + nodeInfo.getViewIdResourceName());
        Log.i(TAG, "");
        List<AccessibilityNodeInfo> contactNameNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/conversation_contact_name");
        if (contactNameNodes.size() == 0) {
            Log.i(TAG, "contact name not visible");
            return;
        } else if (contactNameNodes.size() == 1) {
            if (contactNameNodes.get(0).getText().toString().equals(sContact))
                Log.i(TAG, "Found " + sContact);
            else {
                Log.i(TAG, "Wrong contact " + contactNameNodes.get(0).getText());
                return;
            }
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            printChildNodes(nodeInfo, "  ");
            List<AccessibilityNodeInfo> entryETNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
            Log.i(TAG, "editing text " + entryETNodes.size());
            if (entryETNodes.size() == 1) {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, sMsg);
                entryETNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (nodeInfo.getViewIdResourceName() == null) {
                List<AccessibilityNodeInfo> sendBtnNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                Log.i(TAG, "clicking button" + sendBtnNodes.size());
                if (sendBtnNodes.size() == 1) {
                    List<AccessibilityNodeInfo> entryETNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
                    Log.i(TAG, "entry nodes size " + entryETNodes.size());
                    if (entryETNodes.size() == 1) {
                        Log.i(TAG, "entry node text " + entryETNodes.get(0).getText());
                        if (entryETNodes.get(0).getText().toString().equals(sMsg)) {
                            sendBtnNodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            sActive = false;
                        }
                    }
                }
            }
        } else printChildNodes(nodeInfo, "  ");
        nodeInfo.recycle();
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {

        super.onServiceConnected();
        sInstance = this;
        Log.i(TAG, "service connected");
    }
}
