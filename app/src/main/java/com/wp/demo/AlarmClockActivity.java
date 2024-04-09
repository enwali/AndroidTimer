package com.wp.demo;

import static com.wheelpicker.PickOption.getPickDefaultOptionBuilder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wheelpicker.BottomSheet;
import com.wheelpicker.DataPickerUtils;
import com.wheelpicker.PickOption;
import com.wheelpicker.SingleTextWheelPicker;
import com.wheelpicker.core.WheelPickerUtil;
import com.wheelpicker.widget.TextWheelPickerAdapter;
import com.wp.demo.adapter.TimerAdapter;
import com.wp.demo.utils.DataUtil;
import com.wp.demo.utils.PlayUtils;
import com.wp.demo.utils.SpModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AlarmClockActivity extends Activity {

    public final String TAG = getClass().getName();
    Context mContext;
    private RecyclerView mRvAlarm;
    private TextView mTvAddTime;
    private LinearLayout mLlAllCtrl;
    private TimerAdapter mAdapter;

    List<TimeTextBean> mProgressData = new ArrayList<>();

    SingleTextWheelPicker hourPicker;
    SingleTextWheelPicker seedPicker;
    SingleTextWheelPicker secondPicker;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        mContext = this;
        mRvAlarm = findViewById(R.id.rv_alarm);
        mTvAddTime = findViewById(R.id.tv_add_time);
        mLlAllCtrl = findViewById(R.id.ll_all_ctrl);

        List<TimeTextBean> hourData = getHour();
        List<TimeTextBean> seedData = getSecond();
        List<TimeTextBean> secondData = getSecond();
        hourPicker = setPickerView(R.id.fl_hour, 0, hourData);
        seedPicker = setPickerView(R.id.fl_seed, 5, seedData);
        secondPicker = setPickerView(R.id.fl_second, 0, secondData);
        initAlarmAdapter();

        mTvAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long hour = Long.parseLong(hourPicker.getPickedData());
                long seed = Long.parseLong(seedPicker.getPickedData());
                long second = Long.parseLong(secondPicker.getPickedData());
                long time = hour * 60 * 60 + seed * 60 + second;
                if (time != 0) {
                    mProgressData.add(0, new TimeTextBean(time));
                    mLlAllCtrl.setVisibility(View.VISIBLE);
                    SpModel.putTimeList(mProgressData);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        findViewById(R.id.tv_stop_ring).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayUtils.INSTANCE.stopRing();
            }
        });
        findViewById(R.id.tv_clear_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mProgressData.size(); i++) {
                    TimeTextBean bean = mProgressData.get(i);
                    bean.getTimer().cancel();
                    PlayUtils.INSTANCE.stopRing();
                }
                mProgressData.clear();
                SpModel.putTimeList(mProgressData);
                mLlAllCtrl.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
            }
        });
        mLlAllCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayUtils.INSTANCE.stopRing();
                for (TimeTextBean bean : mProgressData) {
                    bean.setOpenRing(true);
                    bean.setStatus(TimeStatus.onDoing);
                    bean.setCountdown(bean.getTime());
                    setDownTimer(bean);
                    bean.start();
                }
            }
        });
    }

    private SingleTextWheelPicker setPickerView(int id, int currentIndex, List<TimeTextBean> srcData) {
        PickOption option = getPickDefaultOptionBuilder(mContext).setMiddleTitleText("").setItemTextColor(0XFFBBBBBB).setItemLineColor(Color.BLACK).setBackgroundColor(Color.TRANSPARENT).setItemTextSize(mContext.getResources().getDimensionPixelSize(com.wheelpicker.R.dimen.font_36px)).setItemSpace(mContext.getResources().getDimensionPixelSize(com.wheelpicker.R.dimen.px36)).build();
        final SingleTextWheelPicker picker = new SingleTextWheelPicker(mContext);
        DataPickerUtils.setPickViewStyle(picker, option);
        TextWheelPickerAdapter adapter = new TextWheelPickerAdapter(srcData);
        picker.setAdapter(adapter);
        picker.setCurrentItem(Math.max(currentIndex, 0));
        FrameLayout frameLayout = findViewById(id);

        frameLayout.addView(picker.asView());
        return picker;
    }

    private List<TimeTextBean> getHour() {
        List<TimeTextBean> data = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            data.add(new TimeTextBean(i));
        }
        return data;
    }

    private List<TimeTextBean> getSecond() {
        List<TimeTextBean> data = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            data.add(new TimeTextBean(i));
        }
        return data;
    }

    private void initDefaultList() {
        mProgressData = SpModel.getTimeList();
        if (mProgressData.isEmpty()) {
            mProgressData.add(new TimeTextBean(60));
            mProgressData.add(new TimeTextBean(5 * 60));
            mProgressData.add(new TimeTextBean(10 * 60));
            mProgressData.add(new TimeTextBean(20 * 60));
            mProgressData.add(new TimeTextBean(30 * 60));
            mProgressData.add(new TimeTextBean(40 * 60));
            SpModel.putTimeList(mProgressData);
        }
    }

    private void initAlarmAdapter() {
        mProgressData.clear();
        initDefaultList();
        for (int i = 0; i < mProgressData.size(); i++) {
            final TimeTextBean textBean = mProgressData.get(i);
            setDownTimer(textBean);
        }

        mRvAlarm.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TimerAdapter(mProgressData);
        mRvAlarm.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TimeTextBean bean = mProgressData.get(position);
                switch (view.getId()) {
                    case R.id.iv_reset:
                        bean.setOpenRing(true);
                        bean.setCountdown(bean.getTime());
                        setDownTimer(bean);
                        bean.setStatus(TimeStatus.onReady);
                        if (PlayUtils.INSTANCE.isPlaying()) {
                            PlayUtils.INSTANCE.stopRing();
                        }
                        break;
                    case R.id.iv_ring:
                        if (PlayUtils.INSTANCE.isPlaying()) {
                            PlayUtils.INSTANCE.stopRing();
                            bean.setOpenRing(false);
                            return;
                        }
                        bean.setOpenRing(!bean.isOpenRing());
                        break;
                    case R.id.tv_delete:
                        mProgressData.remove(position);
                        SpModel.putTimeList(mProgressData);
                        break;
                    case R.id.tv_ctrl:
                        switch (bean.getStatus()) {
                            case onReady:
                                bean.setStatus(TimeStatus.onDoing);
                                if (bean.getTimer() == null) {
                                    setDownTimer(bean);
                                }
                                bean.start();
                                break;
                            case onDoing:
                                bean.setStatus(TimeStatus.onPause);
                                bean.cancel();
                                bean.setTimer(null);
                                break;
                            case onPause:
                                bean.setStatus(TimeStatus.onDoing);
                                setDownTimer(bean);
                                bean.start();
                                break;
                            case onEnd:
                                PlayUtils.INSTANCE.stopRing();
                                bean.setOpenRing(true);
                                bean.setStatus(TimeStatus.onDoing);
                                bean.setCountdown(bean.getTime());
                                setDownTimer(bean);
                                bean.start();
                                break;
                        }
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mHandler = new Handler();
        startRepeatingTask();
    }

    private void setDownTimer(final TimeTextBean textBean) {
        textBean.cancel();
        textBean.setTimer(null);
        long time = textBean.getCountdown();
        CountDownTimer timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                Log.i(TAG, "onTick: " + l);
                textBean.setCountdown(l);
                notifyProgress(textBean);
            }

            @Override
            public void onFinish() {
                textBean.setCountdown(0);
                textBean.setStatus(TimeStatus.onEnd);
                if (textBean.isOpenRing()) {
                    PlayUtils.INSTANCE.startRing();
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        textBean.setTimer(timer);
    }

    private void notifyProgress(TimeTextBean textBean) {
        for (int i = 0; i < mProgressData.size(); i++) {
            Log.i(TAG, i + "  mProgressData UUID: " + mProgressData.get(i).getUniqueStringID() + "    textBean UUID: " + textBean.getUniqueStringID());
            if (mProgressData.get(i).getUniqueStringID().equals(textBean.getUniqueStringID())) {
                Log.e(TAG, i + " textBean Countdown: " + textBean.getCountdown());
                mAdapter.notifyItemChanged(i, "notifyProgress");
                return;
            }
        }
    }

    private Handler mHandler;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            mHandler.postDelayed(this, 1000);
        }
    };

    void startRepeatingTask() {
        runnable.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(runnable);
    }

    private void updateUI() {
        if (mAdapter != null) {
            mAdapter.setNewData(mProgressData);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时停止定时任务，防止内存泄漏
        stopRepeatingTask();
        for (TimeTextBean bean : mProgressData) {
            bean.cancel();
        }
        SpModel.putTimeList(mProgressData);
        PlayUtils.INSTANCE.stopRing();
    }
}
