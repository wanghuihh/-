package com.example.qqxtx4;

import android.support.annotation.NonNull;

/**
 * Created by wh on 2017/5/24.
 */

public class Friend implements Comparable<Friend> {
    public String name;
    public String pinyin;
    public Friend(String name) {
        this.name = name;
        //直接获取拼音
        pinyin = PinYinUtil.getPinYin(name);
    }
    @Override
    public int compareTo(@NonNull Friend o) {
        //aa -> ab
        return this.pinyin.compareTo(o.pinyin);
    }
}
