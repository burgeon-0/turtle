package org.burgeon.turtle.core.model.source;

import lombok.Data;

import java.util.List;

/**
 * 项目
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class Group {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目根路径
     */
    private String host;

    /**
     * 项目Java源文件
     */
    private List<JavaClass> javaClasses;

}
