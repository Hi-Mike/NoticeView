package cn.mike.me.noticeview.adapter;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.mike.noticeview.NoticeNormalView;

/**
 * Created by ske on 2016/11/9.
 */

public class TextAdapter implements NoticeNormalView.Adapter<String> {
    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public String getItem(int position) {
        return "pos:" + position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    @Override
    public void bindView(int position, View nextView) {
        ((TextView) nextView).setGravity(Gravity.CENTER);
        ((TextView) nextView).setText(getItem(position));
    }
}
