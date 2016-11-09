package cn.mike.me.noticeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import cn.mike.me.noticeview.adapter.NormalAdapter;
import cn.mike.me.noticeview.adapter.TextAdapter;
import cn.mike.noticeview.NoticeNormalView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoticeNormalView normalView = (NoticeNormalView) findViewById(R.id.notice_view);
        normalView.setAdapter(new TextAdapter());
        normalView.start();

        ArrayList<NormalItem> datas = new ArrayList<>();
        datas.add(new NormalItem("http://img5.duitang.com/uploads/item/201505/22/20150522125714_jSecu.jpeg", "可爱图。。。"));
        datas.add(new NormalItem("http://img5q.duitang.com/uploads/item/201506/19/20150619182959_Rhzn2.jpeg", "双十一送钱"));
        datas.add(new NormalItem("http://img2.3lian.com/2014/f5/126/d/101.jpg", "单身狗日常"));

        NoticeNormalView noticeNormalView = (NoticeNormalView) findViewById(R.id.notice_normal);
        noticeNormalView.setAdapter(new NormalAdapter(datas));
        noticeNormalView.start();

        NoticeNormalView noticeIcon = (NoticeNormalView) findViewById(R.id.notice_icon);
        noticeIcon.setAdapter(new NormalAdapter(datas));
        noticeIcon.start();
    }
}