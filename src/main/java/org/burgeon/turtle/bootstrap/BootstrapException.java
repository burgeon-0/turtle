package org.burgeon.turtle.bootstrap;

/**
 * Bootstrap异常
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class BootstrapException extends Exception {

    /**
     * 命令行处理异常
     *
     * @param message
     */
    public BootstrapException(String message) {
        super(message);
    }

}
