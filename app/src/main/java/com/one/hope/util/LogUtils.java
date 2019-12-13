package com.one.hope.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Admin
 * @time 16/9/11.16:14
 */
public class LogUtils {
    private static boolean IS_DEBUG = true;
    private static final int JSON_INDENT = 2;

    /**
     * 打印没有指定tag的Log（默认为json），级别为i
     *
     * @param logStr 需要打印的Log日志
     */
    public static void i(String logStr) {
        i(null, logStr);
    }

    /**
     * 打印没有指定tag的Log，级别为i
     *
     * @param tag    用户自定义tag
     * @param logStr 需要打印的Log日志
     */
    public static void i(String tag, String logStr) {
        if (!IS_DEBUG) {
            return;
        }
        LogText.i(getFinalTag(tag), logStr);
    }

    /**
     * 打印没有指定tag的Log，内容为json数据，级别为i
     *
     * @param jsonStr 需要打印的json数据
     */
    public static void json(String jsonStr) {
        json(null, jsonStr);
    }

    /**
     * 打印指定tag的Log，内容为json数据，级别为i
     *
     * @param tag     用户自定义tag
     * @param jsonStr 需要打印的json数据
     */
    public static void json(String tag, String jsonStr) {
        if (!IS_DEBUG) {
            return;
        }
        LogText.i(getFinalTag(tag), getPrettyJson(jsonStr));
    }

    /**
     * 将json字符串转换为json格式
     *
     * @param jsonStr 传入的json数据
     * @return 转换后的json格式字符串
     */
    private static String getPrettyJson(String jsonStr) {
        try {
            jsonStr = jsonStr.trim();
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                return jsonObject.toString(JSON_INDENT);
            }
            if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                return jsonArray.toString(JSON_INDENT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Invalid Json, Please Check: " + jsonStr;
    }

    /**
     * 判断用户使用时有没有自定义tag，如果没有则使用默认的json
     *
     * @param tag 过滤的tag
     * @return 用户或者默认的tag
     */
    private static String getFinalTag(String tag) {
        String TAG = "json";
        if (!TextUtils.isEmpty(tag)) {
            return tag;
        }
        return TAG;
    }

    /**
     * 真正执行打印的类
     */
    private static class LogText {
        private static final String SINGLE_DIVIDER = "───────────────────────log start────────────────────\n";
        private static final String DOUBLE_DIVIDER = "═══════════════════════log end═════════════════════\n";

        private String tag;

        LogText(String tag) {
            this.tag = tag;
        }

        /**
         * 接受数据执行打印
         *
         * @param tag    过滤的tag
         * @param logStr 需要打印的log
         */
        public static void i(String tag, String logStr) {
            LogText logText = new LogText(tag);
            logText.setup(logStr);
        }

        /**
         * 接受数据执行打印
         *
         * @param logStr 需要打印的log
         */
        void setup(String logStr) {
            setUpHeader();
            setUpContent(logStr);
            setUpFooter();
        }

        /**
         * 打印开始分割线
         */
        private void setUpHeader() {
            Log.i(tag, SINGLE_DIVIDER);
        }

        /**
         * 打印结束分割线
         */
        private void setUpFooter() {
            Log.i(tag, DOUBLE_DIVIDER);
        }

        /**
         * 打印log
         *
         * @param logStr 需要打印的log
         */
        void setUpContent(String logStr) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.i(tag, "(" + targetStackTraceElement.getFileName() + ":" + targetStackTraceElement.getLineNumber() + ")");
            Log.i(tag, logStr);
        }

        /**
         * 拿到当前调用的栈帧集合，每进入一个方法，会将该方法的相关信息（例如：类名，方法名，方法调用行数等）存储下来，压入到一个栈中，当方法返回的时候再将其出栈。
         *
         * @return 当前栈
         */
        private StackTraceElement getTargetStackTraceElement() {
            // find the target invoked method
            StackTraceElement targetStackTrace = null;
            boolean shouldTrace = false;
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                boolean isLogMethod = stackTraceElement.getClassName().equals(LogUtils.class.getName());
                if (shouldTrace && !isLogMethod) {
                    targetStackTrace = stackTraceElement;
                    break;
                }
                shouldTrace = isLogMethod;
            }
            return targetStackTrace;
        }
    }
}
