package org.burgeon.turtle;

import org.burgeon.turtle.core.utils.EnvUtils;

/**
 * @author Sam Lu
 * @createdOn 2021/4/27
 */
public class EncodingTest {

    public static void main(String[] args) {
        System.out.println(EnvUtils.getStringProperty("file.encoding"));
    }

}
