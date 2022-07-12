package org.bg181.turtle.core.command;

/**
 * 命令执行异常
 *
 * @author Sam Lu
 * @createdOn 2021/4/20
 */
public class ExecuteException extends Exception {

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }

}
