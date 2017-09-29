package win.himike.noticeview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ViewSwitcher;

/**
 * Created by ske on 2016/11/9.
 */

public class NoticeView extends ViewSwitcher {

    private Animation mInUp = anim(1.5f, 0);
    private Animation mOutUp = anim(0, -1.5f);

    private int mIndex = 0;
    private int mInterval = 3000;
    private int mDuration = 900;

    private Drawable mIcon;
    private int mIconTint = 0xff999999;
    private int mIconPadding = 0;
    private int mPaddingLeft = 0;

    private boolean mIsVisible = false;
    private boolean mIsStarted = false;
    //    private boolean mIsResumed = true;
    private boolean mIsRunning = false;
    private boolean mUserPresent = true;
    private final NoticeView.DefaultViewFactory mDefaultFactory = new NoticeView.DefaultViewFactory();

    private final int FLIP_MSG = 1;
    private Handler mHandler = null;
    Adapter mAdapter;

    public NoticeView(Context context) {
        this(context, null);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context, attrs);
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);
        mInUp.setDuration(mDuration);
        mOutUp.setDuration(mDuration);
    }

    private void initWithContext(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NoticeView);
        mIcon = a.getDrawable(R.styleable.NoticeView_nvIcon);
        mIconPadding = (int) a.getDimension(R.styleable.NoticeView_nvIconPadding, 0);

        boolean hasIconTint = a.hasValue(R.styleable.NoticeView_nvIconTint);

        if (hasIconTint) {
            mIconTint = a.getColor(R.styleable.NoticeView_nvIconTint, 0xff999999);
        }

        mInterval = a.getInteger(R.styleable.NoticeView_nvInterval, 4000);
        mDuration = a.getInteger(R.styleable.NoticeView_nvDuration, 900);

        a.recycle();

        if (mIcon != null) {
            mPaddingLeft = getPaddingLeft();
            int realPaddingLeft = mPaddingLeft + mIconPadding + mIcon.getIntrinsicWidth();
            setPadding(realPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());

            if (hasIconTint) {
                mIcon = mIcon.mutate();
                DrawableCompat.setTint(mIcon, mIconTint);
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIcon != null) {
            int y = (getMeasuredHeight() - mIcon.getIntrinsicWidth()) / 2;
            mIcon.setBounds(mPaddingLeft, y, mPaddingLeft + mIcon.getIntrinsicWidth(), y + mIcon.getIntrinsicHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIcon != null) {
            mIcon.draw(canvas);
        }
    }

    public int getIndex() {
        return mIndex;
    }

    public void start() {
        setFactory(mDefaultFactory);
        if (mAdapter == null || mAdapter.getCount() < 1) {
            mIsStarted = false;
            update();
        } else {
            mIsStarted = true;
            update();
            show(0);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
                update();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mUserPresent = true;
                update();
            }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler = new Handler(getHandler().getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == FLIP_MSG) {
                    if (mIsRunning) {
                        show(mIndex + 1);
                        msg = obtainMessage(FLIP_MSG);
                        sendMessageDelayed(msg, mInterval);
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        getContext().registerReceiver(mReceiver, filter, null, mHandler);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsVisible = false;
        getContext().unregisterReceiver(mReceiver);
        update();
        mHandler = null;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mIsVisible = visibility == VISIBLE;
        update();
    }

//    测试时会出现touch后没有up、cancel的情况
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mIsResumed = false;
//                update();
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mIsResumed = true;
//                update();
//                break;
//
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private void update() {
        boolean running = mIsVisible && mIsStarted && mUserPresent;
        if (running != mIsRunning) {
            if (running) {
                Message msg = mHandler.obtainMessage(FLIP_MSG);
                mHandler.sendMessageDelayed(msg, mInterval);
            } else {
                mHandler.removeMessages(FLIP_MSG);
            }
            mIsRunning = running;
        }
    }

    private void show(int index) {
        mIndex = index % mAdapter.getCount();
        mAdapter.bindView(mIndex, getNextView());
        showNext();
    }

    private Animation anim(float from, float to) {
        final TranslateAnimation anim = new TranslateAnimation(0, 0f, 0, 0f, Animation.RELATIVE_TO_PARENT, from, Animation.RELATIVE_TO_PARENT, to);
        anim.setDuration(mDuration);
        anim.setFillAfter(false);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    class DefaultViewFactory implements ViewFactory {

        @Override
        public View makeView() {
            return mAdapter.getView(NoticeView.this);
        }
    }

    public void setAdapter(Adapter adapter) {
        removeAllViews();
        mAdapter = adapter;
    }

    public interface Adapter<T> {
        int getCount();

        T getItem(int position);

        long getItemId(int position);

        View getView(ViewGroup parent);

        void bindView(int position, View nextView);
    }
}
