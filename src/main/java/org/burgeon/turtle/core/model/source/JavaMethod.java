package org.burgeon.turtle.core.model.source;

import lombok.Data;

import java.util.List;

/**
 * Java方法信息
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class JavaMethod {

    private JavaComment comment;

    private List<JavaAnnotation> annotations;

    private JavaModifier modifier;

    private String name;

    private JavaClass returnType;

    private List<JavaClass> parameterTypes;

}
