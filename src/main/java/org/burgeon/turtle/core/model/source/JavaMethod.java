package org.burgeon.turtle.core.model.source;

import lombok.Data;

/**
 * Java方法
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

    private JavaType returnType;

    private JavaMethodParameter[] parameters;

}
