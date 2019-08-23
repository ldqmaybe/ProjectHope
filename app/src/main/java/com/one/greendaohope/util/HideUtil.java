package com.one.greendaohope.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/*
使用事咧：
public class HideUtilActivity extends AppCompatActivity
        @Override
     protected void onCreate(Bundle savedInstanceState)
             super.onCreate(savedInstanceState);
             setContentView(layoutId);
             HideUtil.init(this);//在此调用
     }
*/

/**
 * 软键盘的弹出，隐藏<br/>
 */
public class HideUtil {
    public static void init(Activity activity) {
        new HideUtil(activity);
    }

    /**
     * @param activity 传入当前activity
     */
    private HideUtil(final Activity activity) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                dispatchTouchEvent(activity, motionEvent);

                return false;
            }
        });
    }

    /**
     * @param mActivity 当前activity
     * @param ev        触摸事件
     * @return 返回boolean值
     */
    private boolean dispatchTouchEvent(Activity mActivity, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = mActivity.getCurrentFocus();
            if (null != v && isShouldHideInput(v, ev)) {
                hideSoftInput(mActivity, v.getWindowToken());
            }
        }
        return false;
    }

    /**
     * @param v     View
     * @param event 触摸事件
     * @return 返回boolean值
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            Rect rect = new Rect();
            v.getHitRect(rect);
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param mActivity 当前activity
     * @param token     IBinder
     */
    private void hideSoftInput(Activity mActivity, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}