package org.burgeon.turtle.core.model.source;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Java源文件信息
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JavaClass extends JavaType {

    private JavaComment comment;

    private List<JavaAnnotation> annotations;

    private String packageName;

    private JavaModifier modifier;

    private String name;

    /**
     * 对于set、list、map，存在泛型类型
     */
    private List<JavaClass> genericClasses;

    private boolean isClass;

    private boolean isInterface;

    private boolean isAnnotation;

    private boolean isEnum;

    private JavaClass superClass;

    private List<JavaClass> interfaces;

    private List<JavaMethod> constructors;

    private List<JavaField> fields;

    private List<JavaMethod> methods;

    private List<JavaClass> innerClasses;

}
