import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;

/**
 * 测试 Custom Project
 * -i ./test-projects/custom-project-demo/src
 * -c ./test-projects/custom-project-demo/lib1;./test-projects/custom-project-demo/lib2
 *
 * @author Sam Lu
 * @createdOn 2021/3/13
 */
public class CustomProjectTest {

    public static void main(String[] args) {
        System.out.println(StringUtils.join(getCookie().getName(), ":", getCookie().getValue()));
    }

    private static Cookie getCookie() {
        Cookie cookie = new Cookie("token", "custom-project-demo-token");
        return cookie;
    }

}
