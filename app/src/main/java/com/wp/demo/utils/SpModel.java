package com.wp.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.wp.demo.R;
import com.wp.demo.TimeTextBean;
import com.wp.demo.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class SpModel {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "key";
    private static SharedPreferences prefs;

    public static void init(Context app) {
        prefs = app.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void putTimeList(List<TimeTextBean> list) {
        StringBuilder listStr = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            listStr.append(list.get(i).getTime());
            if (list.size() - 1 != i) {
                listStr.append(",");
            }
        }
        setParam("timeList", listStr.toString());
    }

    public static List<TimeTextBean> getTimeList() {
        List<TimeTextBean> textBeans = new ArrayList<>();
        String timeStr = (String) getParam("timeList", "");
        if (TextUtils.isEmpty(timeStr)) {
            return textBeans;
        }
        String[] listStr = timeStr.split(",");
        for (int i = 0; i < listStr.length; i++) {
            textBeans.add(new TimeTextBean(Long.parseLong(listStr[i]) / 1000));
        }
        return textBeans;
    }

    public static void setParam(String key, Object object) {
        SharedPreferences.Editor editor = prefs.edit();

        if (object == null) {
            editor.putString(key, "null");
            return;
        } else {
            String type = object.getClass().getSimpleName();
            if ("String".equals(type)) {
                editor.putString(key, (String) object);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) object);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) object);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) object);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) object);
            }
        }
        editor.apply();
    }

    public static Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();

        if ("String".equals(type)) {
            return prefs.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return prefs.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return prefs.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return prefs.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return prefs.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static void removeData(String key) {
        prefs.edit().remove(key).apply();
    }
}
