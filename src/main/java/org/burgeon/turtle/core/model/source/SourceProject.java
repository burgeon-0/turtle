package org.burgeon.turtle.core.model.source;

import lombok.Data;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import java.util.HashMap;
import java.util.Map;

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
     * spoon root meta model
     */
    private CtModel model;

    private Map<String, CtClass<?>> ctClassMap = new HashMap<>();
    private Map<String, CtMethod<?>> ctMethodMap = new HashMap<>();

    public void putCtClass(String key, CtClass ctClass) {
        ctClassMap.put(key, ctClass);
    }

    public CtClass getCtClass(String key) {
        return ctClassMap.get(key);
    }

    public void putCtMethod(String key, CtMethod ctMethod) {
        ctMethodMap.put(key, ctMethod);
    }

    public CtMethod getCtMethod(String key) {
        return ctMethodMap.get(key);
    }

}
