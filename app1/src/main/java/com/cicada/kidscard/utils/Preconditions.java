package com.cicada.kidscard.utils;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;


/**
 * 判空
 */
public final class Preconditions {

    /**
     * 如果string为空或长度为0 返回 {@code true}
     * <p>
     * 在不需要检查非空情况下,也可以规范性的使用 {@link String#isEmpty()}
     * string 也可 @see {@link TextUtils# isEmpty() }
     *
     * @param string
     * @return {@code true}
     */
    public static boolean isEmpty(@Nullable String string) {
        return TextUtils.isEmpty(string); // string.isEmpty() in Java 6
    }

    public static boolean isNotEmpty(@Nullable String string) {
        return !isEmpty(string);
    }

    public static boolean isEmpty(@Nullable List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNotEmpty(@Nullable  List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(@Nullable Map map) {
        return map == null || map.size() == 0;
    }
    public static boolean isNotEmpty(@Nullable  Map map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(@Nullable Object[] objs) {
        return objs == null || objs.length == 0;
    }
    public static boolean isNotEmpty(@Nullable Object[] objs) {
        return !isEmpty(objs);
    }

    public static boolean isEmpty(@Nullable Object obj) {
        return obj == null;
    }

    public static boolean isNotEmpty(@Nullable  Object obj) {
        return !isEmpty(obj);
    }

}