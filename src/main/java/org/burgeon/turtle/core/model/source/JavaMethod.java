package org.burgeon.turtle.core.model.source;

import lombok.Data;

/**
 * Java方法信息
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class JavaMethod {

    private JavaComment comment;

    private JavaAnnotation[] annotations;

    private JavaModifier modifier;

    private String name;

    private JavaClass returnType;

    private JavaClass[] parameterTypes;

}
