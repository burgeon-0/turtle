package org.burgeon.turtle.common;

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

    public static void main(String[] args) {
        String str = "\n\ntest\n\n";
        System.out.println("leftStrip start");
        System.out.println(leftStrip(str, "\n"));
        System.out.println("leftStrip end");
        System.out.println("rightStrip start");
        System.out.println(rightStrip(str, "\n"));
        System.out.println("rightStrip end");
    }

}
