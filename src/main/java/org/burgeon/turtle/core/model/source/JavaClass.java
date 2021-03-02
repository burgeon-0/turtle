package org.burgeon.turtle.core.model.source;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Java类
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class JavaClass extends JavaType {

    private JavaComment comment;

    private JavaAnnotation[] annotations;

    private String packageName;

    private JavaModifier modifier;

    private String name;

    /**
     * TODO
     * <p>
     * 对于set、list、map，存在泛型类型
     */
    private JavaClass[] genericClasses;

    private boolean isClass;

    private boolean isInterface;

    private boolean isAnnotation;

    private boolean isEnum;

    private JavaClass superClass;

    private JavaClass[] interfaces;

    private JavaMethod[] constructors;

    private JavaField[] fields;

    private JavaMethod[] methods;

    private JavaClass[] innerClasses;

}
