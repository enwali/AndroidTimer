package com.wp.demo.utils;

import android.media.MediaPlayer;

import com.wp.demo.R;
import com.wp.demo.base.MyApplication;

public enum PlayUtils {
    INSTANCE;

    private MediaPlayer mMediaPlayer;


    public void startRing() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(MyApplication.getInstance(), R.raw.game);
        }
        if (isPlaying()) {
            mMediaPlayer.stop();
        }
        // 设置铃声为循环播放
        mMediaPlayer.setLooping(true);
        // 播放铃声
        mMediaPlayer.start();
        mMediaPlayer.isPlaying();
    }

    public void stopRing() {
        if (mMediaPlayer == null) {
            return;
        }
        // 停止播放铃声
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public boolean isPlaying() {
        if (mMediaPlayer == null) {
            return false;
        }
        return mMediaPlayer.isPlaying();
    }
}
