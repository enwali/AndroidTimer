package com.wp.demo.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wp.demo.R;
import com.wp.demo.TimeStatus;
import com.wp.demo.TimeTextBean;
import com.wp.demo.view.DefineProgressView;

import java.util.List;

public class TimerAdapter extends BaseQuickAdapter<TimeTextBean, BaseViewHolder> {

    List<TimeTextBean> mDataList;

    public TimerAdapter(@Nullable List<TimeTextBean> data) {
        super(R.layout.item_alarm, data);
        mDataList = data;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            String payload = payloads.get(0).toString();
            if ("notifyProgress".equals(payload)) {
                TimeTextBean item = mDataList.get(position);
                DefineProgressView progressView = holder.getView(R.id.dv_progress);
                long time = item.getTime();
                long countdown = item.getCountdown();
                long progress = 100 - (countdown * 100) / time;
                if (countdown == 0) {
                    progress = 100;
                }
                Log.e(TAG, position + " textBean Countdown: " + countdown + "    progress:" + progress);
                progressView.resetLevelProgress((int) progress);
                holder.setText(R.id.tv_countdown, formatTime(countdown));
            }
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final TimeTextBean item) {

        Log.i(TAG, "convert ");

        helper.setText(R.id.tv_initial_time, formatTime(item.getTime()));
        helper.setText(R.id.tv_countdown, formatTime(item.getCountdown()));
        DefineProgressView progressView = helper.getView(R.id.dv_progress);
        helper.addOnClickListener(R.id.iv_reset);
        helper.addOnClickListener(R.id.iv_ring);
        helper.addOnClickListener(R.id.tv_delete);
        helper.addOnClickListener(R.id.tv_ctrl);

        long time = item.getTime();
        long countdown = item.getCountdown();
        long progress = 100 - (countdown * 100) / time;
        if (countdown == 0) {
            progress = 100;
        }
        progressView.resetLevelProgress((int) progress);

        if (item.isOpenRing()) {
            helper.setBackgroundRes(R.id.iv_ring, R.mipmap.ic_ring_yellow);
        } else {
            helper.setBackgroundRes(R.id.iv_ring, R.mipmap.ic_ring_gray);
        }

        switch (item.getStatus()) {
            case onReady:
                helper.setBackgroundRes(R.id.tv_ctrl, R.drawable.shape_light_green_132914);
                helper.setTextColor(R.id.tv_ctrl, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setText(R.id.tv_ctrl, "开始");
                break;
            case onDoing:
                helper.setBackgroundRes(R.id.tv_ctrl, R.drawable.shape_light_yellow_eca537);
                helper.setTextColor(R.id.tv_ctrl, ContextCompat.getColor(mContext, R.color.yellow_eca537));
                helper.setText(R.id.tv_ctrl, "暂停");
                break;
            case onPause:
                helper.setBackgroundRes(R.id.tv_ctrl, R.drawable.shape_light_green_132914);
                helper.setTextColor(R.id.tv_ctrl, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setText(R.id.tv_ctrl, "继续");
                break;
            case onEnd:
                helper.setBackgroundRes(R.id.tv_ctrl, R.drawable.shape_light_green_132914);
                helper.setTextColor(R.id.tv_ctrl, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setText(R.id.tv_ctrl, "重开");
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    public String formatTime(long seconds) {
        seconds = seconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
        } else if (minutes > 0) {
            return String.format("%02d:%02d", minutes, remainingSeconds);
        } else {
            return String.valueOf(remainingSeconds);
        }
    }


}
