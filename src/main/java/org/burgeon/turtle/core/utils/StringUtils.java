package org.burgeon.turtle.core.utils;

/**
 * 字符串工具
 *
 * @author luxiaocong
 * @createdOn 2021/3/14
 */
public class StringUtils {

    /**
     * 去除左侧的字符子串
     *
     * @param str
     * @param sub
     * @return
     */
    public static String leftStrip(String str, String sub) {
        String lStrip = str.replaceAll("^(" + sub + ")+", "");
        return lStrip;
    }

    /**
     * 去除右侧的字符子串
     *
     * @param str
     * @param sub
     * @return
     */
    public static String rightStrip(String str, String sub) {
        String lStrip = str.replaceAll("(" + sub + ")+$", "");
        return lStrip;
    }

    /**
     * 字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean notBlank(String str) {
        if (str != null && !str.trim().equals("")) {
            return true;
        }
        return false;
    }

}
