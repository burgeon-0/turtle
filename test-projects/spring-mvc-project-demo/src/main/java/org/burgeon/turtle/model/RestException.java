package org.burgeon.turtle.model;

/**
 * @author Sam Lu
 * @createdOn 2021/4/27
 */
public class RestException extends RuntimeException {

    private int code;

    public RestException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取错误编码
     */
    public int getCode() {
        return code;
    }

}
