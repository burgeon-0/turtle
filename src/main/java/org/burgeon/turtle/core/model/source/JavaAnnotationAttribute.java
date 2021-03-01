package org.burgeon.turtle.core.model.source;

import lombok.Data;

/**
 * Java注解属性
 *
 * @author luxiaocong
 * @createdOn 2021/3/1
 */
@Data
public class JavaAnnotationAttribute {

    private String name;

    private Object[] values;

}
