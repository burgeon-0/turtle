package org.bg181.turtle.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 注释模型 -_- !#$%&'()*+,/:;=?@[]\/
 *
 * @author Sam Lu
 * @createdOn 2021/4/13
 */
@Data
public class CommentModel {

    /**
     * boolean参数 -_- !#$%&'()*+,/:;=?@[]\/
     */
    @NotNull
    private boolean bool;

    /**
     * int参数
     */
    private int i;

    /**
     * 字符串参数
     */
    private String str;

    /**
     * byte数组
     */
    private byte[] bytes;

    /**
     * list参数
     */
    private List<String> list;

    /**
     * map参数
     */
    private Map<String, List> map;

    /**
     * 子参数
     */
    private CommentModel child;

}
