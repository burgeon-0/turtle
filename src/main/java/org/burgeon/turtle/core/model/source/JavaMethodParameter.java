package org.burgeon.turtle.core.model.source;

import lombok.Data;

/**
 * Java方法参数
 *
 * @author luxiaocong
 * @createdOn 2021/3/2
 */
@Data
public class JavaMethodParameter {

    private JavaAnnotation[] annotations;

    private JavaModifier modifier;

    private JavaType type;

    private String name;

}
