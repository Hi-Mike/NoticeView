package win.himike.noticeview.adapter;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import win.himike.noticeview.NoticeView;

/**
 * Created by ske on 2016/11/9.
 */

public class TextAdapter implements NoticeView.Adapter<String> {
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
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    @Override
    public void bindView(int position, View nextView) {
        ((TextView) nextView).setText(getItem(position));
    }
}
