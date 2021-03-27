package org.burgeon.turtle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试匹配"{xxx}"的正则表达式
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class RegexTest {

    public static void main(String[] args) {
        String str = "/api/client/users/status/{user-id}/{uu-}/{-uu}/{uu--}/{-u--u-}";
        String regex = "\\{.*?-.*?\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}
