package com.wp.demo;

import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.wheelpicker.widget.PickString;
import com.wp.demo.base.MyApplication;


public class TimeTextBean implements PickString {

    private long time;

    private long countdown;
    private boolean isOpenRing = true;
    private TimeStatus status = TimeStatus.onReady;

    private CountDownTimer timer;

    public boolean isOpenRing() {
        return isOpenRing;
    }

    public void setOpenRing(boolean openRing) {
        isOpenRing = openRing;
    }

    public TimeStatus getStatus() {
        return status;
    }

    public void setStatus(TimeStatus status) {
        this.status = status;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public void start() {
        if (timer != null) {
            timer.start();
        }
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public TimeTextBean(long time) {
        this.time = time * 1000;
        this.countdown = time * 1000;
    }

    public long getCountdown() {
        return countdown;
    }

    public void setCountdown(long countdown) {
        this.countdown = countdown;
    }

    public boolean isEnd() {
        if (countdown <= 0) {
            return true;
        }
        return false;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String pickDisplayName() {
        return String.valueOf(time / 1000);
    }
}
