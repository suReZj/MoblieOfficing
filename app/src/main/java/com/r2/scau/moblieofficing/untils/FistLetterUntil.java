package com.r2.scau.moblieofficing.untils;

import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * Created by 嘉进 on 13:18.
 */

public class FistLetterUntil {
    public static String getSortKey(String sortKeyString) {
        String key = Pinyin.toPinyin(sortKeyString.charAt(0)).substring(0, 1).toUpperCase();
        Log.e(sortKeyString, key);
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }
}
