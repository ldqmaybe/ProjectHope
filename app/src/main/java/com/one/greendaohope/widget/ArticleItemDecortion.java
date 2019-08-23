package com.one.greendaohope.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.one.greendaohope.R;

/**
 * Created by admin on 2017/6/27.
 */

public class ArticleItemDecortion extends RecyclerView.ItemDecoration {
    /**
     * 分割线的高度
     */
    private int dividerHeight;
    /**
     * 分割线的画笔
     */
    private Paint dividerPaint;

    public ArticleItemDecortion(Context context) {
        dividerHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_1);
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.gray));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount-1; i++) {
            View view = parent.getChildAt(i);
//            int left = view.getPaddingLeft();
//            int right = view.getWidth() - view.getPaddingRight();
            int top = view.getBottom();
            int bottom = view.getBottom()+dividerHeight;
            c.drawRect(left,top,right,bottom,dividerPaint);
        }
    }
}
