package org.burgeon.turtle.core.utils;

/**
 * 字符串工具
 *
 * @author Sam Lu
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
        String rStrip = str.replaceAll("(" + sub + ")+$", "");
        return rStrip;
    }

    /**
     * 去除指定的字符子串
     *
     * @param str
     * @param sub
     * @return
     */
    public static String strip(String str, String sub) {
        String strip = str.replaceAll("(" + sub + ")", "");
        return strip;
    }

    /**
     * 字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean notBlank(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        }
        return false;
    }

}
