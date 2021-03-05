package org.burgeon.turtle.core.model.source;

import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * 源文件项目
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class SourceProject {

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
     * Java源文件
     */
    private List<File> files;

    /**
     * class文件
     */
    private List<Class> classes;

}
