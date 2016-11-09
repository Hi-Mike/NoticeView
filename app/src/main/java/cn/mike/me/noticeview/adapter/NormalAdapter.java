package cn.mike.me.noticeview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.mike.me.noticeview.NormalItem;
import cn.mike.me.noticeview.R;
import cn.mike.noticeview.NoticeNormalView;

/**
 * Created by ske on 2016/11/9.
 */

public class NormalAdapter implements NoticeNormalView.Adapter<NormalItem> {
    List<NormalItem> datas;

    public NormalAdapter(List<NormalItem> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public NormalItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return view;
    }

    @Override
    public void bindView(int position, View nextView) {
        Glide.with(nextView.getContext()).load(datas.get(position).getImg()).into((ImageView) nextView.findViewById(R.id.image));
        ((TextView) nextView.findViewById(R.id.text)).setText(datas.get(position).getText());
    }
}
