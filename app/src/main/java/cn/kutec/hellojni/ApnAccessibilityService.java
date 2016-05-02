package cn.kutec.hellojni;


import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;

/**
 * Apn设置辅助服务
 *
 * @author wrx
 ***/
public class ApnAccessibilityService extends AccessibilityService {
    private static final String TAG = ApnAccessibilityService.class.getSimpleName();

    // dump view by hierarchy 观察
    /**
     *  apn设置界面 recyclePrintLayoutInfo()打印查看索引值
     */
    private static final int APN_S_RELAYOUT_ITEM = 0;
    private static final int APN_S_RADIO_BUTTON_ITEM = 1;

    private static final int APN_S_ACTION_BAR_VIEW = 0;
    private static final int APN_S_BACK_VIEW = 0;
    private static final int APN_S_ADD_APN_VIEW = 1;

    private final int APN_S_APN_LIST_VIEW = 1;

    /**
     *  apn编辑界面 -> recyclePrintLayoutInfo()打印查看索引值
     */
    private final int APN_E_ACTION_BAR_VIEW = 0; // 顶部
    private final int APN_E_APN_PROPERTY_LIST_VIEW = 1; // apn属性列表

    public static final int CMD_ADD_APN = 0;
    public static final int CMD_EDIT_APN = 1;
    public static final int CMD_DEL_APN = 2;

    private static final int STATE_EXIT = 100; // 退出
    private static final int STATE_EDIT_APN = 101; // 编辑属性
    private static final int STATE_ADD_APN = 102; // 新增APN


    private int state = -1; // 状态
    private int command = -1; // 执行操作命令


    @Override
    protected void onServiceConnected() {
        log("AccessibilityService config success!");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if( event.getSource() == null ) {
            return;
        }

        log("event type: " + AccessibilityEvent.eventTypeToString(event.getEventType()) + "\n" +
            "event package: " + event.getPackageName() + "\n" +
            "class name:  " + event.getClassName().toString());

        if( event.getPackageName().equals("com.android.settings") &&
            event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ){
            final String className = event.getClassName().toString();
            log("class name: " + className);
            if( "com.android.settings.ApnSettings".equals(className) ){
                doCommandInApnSettings(event);
            } else if( "com.android.settings.ApnEditor".equals(className) ) {
                doCommandInApnEditor(event);
            } else if( "android.app.AlertDialog".equals(className) ) {
                doCommandInAlertDialog(event);
            }
        }
    }

    /**
     * 执行alertdialog中的命令操作
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doCommandInAlertDialog(AccessibilityEvent event) {
        AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityEventCompat.asRecord(event).getSource();
        //AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            log("Root node info is　null");
            return;
        }

        if( state == STATE_EDIT_APN ) {
            //recyclePrintLayoutInfo(rootNodeInfo, -1);

            AccessibilityNodeInfoCompat editorNodeInfo = rootNodeInfo.getChild(1).getChild(0); // 值输入编辑框
            if (editorNodeInfo == null) {
                return;
            }


            /*int count = editorNodeInfo.getChildCount();
            for( int i=0; i<count; i++ ) {
                log(i + " cls: " + rootNodeInfo.getChild(i).getClassName());
            }*/
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "apn 测试名称");
            editorNodeInfo.performAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SET_TEXT.getId(), arguments);


            /*Bundle arguments = new Bundle();
            arguments.putInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT, AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE);
            arguments.putBoolean(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, true);
            editorNodeInfo.performAction(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("a","abc" );
            clipboard.setPrimaryClip(clip);
            editorNodeInfo.performAction(AccessibilityNodeInfoCompat.ACTION_PASTE);*/


            AccessibilityNodeInfoCompat confirmNodeInfo = rootNodeInfo.getChild(3); // 确定按钮
            if (confirmNodeInfo != null) {
                confirmNodeInfo.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
            }
            log("==设置属性");
            state = STATE_EXIT;
        }
    }

    /**
     * apn编辑界面执行相应操作
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void doCommandInApnEditor(AccessibilityEvent event) {
        AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityEventCompat.asRecord(event).getSource();
        //AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            log("Root node info is　null. doCommandInApnEditor.");
            return;
        }

        if( state == STATE_EXIT ) {

        } else if( state == STATE_EDIT_APN ){
            final AccessibilityNodeInfoCompat apnPropertyListNode = rootNodeInfo.getChild(APN_S_APN_LIST_VIEW);
            apnPropertyListNode.getChild(0)
                    .performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);

        } else {

        }
    }


    /**
     * apn设置界面执行相应操作
     */

    private void doCommandInApnSettings(AccessibilityEvent event) {
        AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityEventCompat.asRecord(event).getSource();

        //AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if (rootNodeInfo == null) {
            log("Root node info is　null");
            return;
        }
        state = STATE_ADD_APN;//测试使用
        //selectApnWithName(apnName, rootNodeInfo);
        //editApnWithName(apnName, rootNodeInfo);
        if( state == STATE_ADD_APN ) {
            final String apnName = "test apn name";
            addApnWithName(apnName, rootNodeInfo);
        }
        //exitApnSettings(rootNodeInfo);
    }

    /**
     * 退出apn设置界面
     * @param rootNode 根节点
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exitApnSettings(AccessibilityNodeInfoCompat rootNode){
        final AccessibilityNodeInfoCompat actionbarNode = rootNode.getChild(APN_S_ACTION_BAR_VIEW);
        actionbarNode.getChild(APN_S_BACK_VIEW)
                .performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
    }

    /**
     * 根据apnName选择对应的APN接入点
     * @param apnName apn名称
     * @param rootNode 活动窗口根节点
     */
    private void selectApnWithName(@NonNull String apnName, @NonNull AccessibilityNodeInfoCompat rootNode){

        final AccessibilityNodeInfoCompat apnListNode = rootNode.getChild(APN_S_APN_LIST_VIEW);

        int findItemIndex = -1; // 记录apnName对应的列表项
        final int count = apnListNode.getChildCount();
        AccessibilityNodeInfoCompat linearLayoutItemNode = null;
        AccessibilityNodeInfoCompat relayoutNode = null;
        for(int i = 0; i<count; i++ ){
            linearLayoutItemNode = apnListNode.getChild(i);
            relayoutNode = linearLayoutItemNode.getChild(APN_S_RELAYOUT_ITEM);
            for( int j=0; j<relayoutNode.getChildCount(); j++ ){
                if( apnName.equalsIgnoreCase(relayoutNode.getChild(j).getText().toString()) ){
                    findItemIndex = i;
                    break;
                }
            }
            if( findItemIndex != -1 ){
                linearLayoutItemNode.getChild(APN_S_RADIO_BUTTON_ITEM)
                                    .performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
                log("find apn:  " + apnName + ", index: " + findItemIndex);
                break;
            }
        }
        if( findItemIndex == -1 ) {
            log("not find apn:  " + apnName);
            return ;
        }
    }

    /**
     * 进入apnName对应的APN编辑界面
     * @param apnName apn名称
     * @param rootNode 活动窗口根节点
     */
    private void editApnWithName(@NonNull String apnName, @NonNull AccessibilityNodeInfoCompat rootNode){
        final AccessibilityNodeInfoCompat apnListNode = rootNode.getChild(APN_S_APN_LIST_VIEW);

        int findItemIndex = -1; // 记录apnName对应的列表项
        final int count = apnListNode.getChildCount();
        AccessibilityNodeInfoCompat linearLayoutItemNode = null;
        AccessibilityNodeInfoCompat relayoutNode = null;
        for(int i = 0; i<count; i++ ){
            linearLayoutItemNode = apnListNode.getChild(i);
            relayoutNode = linearLayoutItemNode.getChild(APN_S_RELAYOUT_ITEM);
            for( int j=0; j<relayoutNode.getChildCount(); j++ ){
                if( apnName.equalsIgnoreCase(relayoutNode.getChild(j).getText().toString()) ){
                    findItemIndex = i;
                    break;
                }
            }
            if( findItemIndex != -1 ){
                linearLayoutItemNode.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
                log("find apn:  " + apnName + ", index: " + findItemIndex);
                break;
            }
        }
        if( findItemIndex == -1 ) {
            log("not find apn:  " + apnName);
            return ;
        }
    }

    /**
     * 添加一个名为apnName的接入点
     * @param apnName apn名称
     * @param rootNode 活动窗口根节点
     */
    private void addApnWithName(@NonNull String apnName, @NonNull AccessibilityNodeInfoCompat rootNode) {
        final AccessibilityNodeInfoCompat actionbarNode = rootNode.getChild(APN_S_ACTION_BAR_VIEW);
        actionbarNode.getChild(APN_S_ADD_APN_VIEW)
                     .performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
        state = STATE_EDIT_APN;
    }

    @Override
    public void onInterrupt() {
    }

    /**
     * 递归打印节点信息
     * @param rootNode 根节点
     * @param level 节点层级
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void recyclePrintLayoutInfo(AccessibilityNodeInfoCompat rootNode, int level) {
        if (rootNode.getChildCount() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("child widget: " + rootNode.getClassName())
                    .append("\n")
                    .append("showDialog:" + rootNode.canOpenPopup())
                    .append("\n")
                    .append("Text：" + rootNode.getText())
                    .append("\n")
                    .append("level: " + level)
                    .append("\n")
                    .append("windowId:" + rootNode.getWindowId());
            log(sb.toString());
        } else {
            for (int i = 0; i < rootNode.getChildCount(); i++) {
                if( rootNode.getChild(i) != null ){
                    log("i: " + i);
                    recyclePrintLayoutInfo(rootNode.getChild(i), i);
                }
            }
        }
    }

    private void log(@NonNull String msg) {
        //Log.debug(TAG, msg);
        android.util.Log.i("test", msg);
    }

    /*@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void printIdName(AccessibilityNodeInfo nodeInfo) {
        if( nodeInfo == null ) {
            return ;
        }
        log("rnodeInfo.getViewIdResourceName: " + nodeInfo.getViewIdResourceName());
        int count = nodeInfo.getChildCount();
        for( int i=0; i<count; i++ ) {
            printIdName(nodeInfo.getChild(i));
        }
    }*/
}
