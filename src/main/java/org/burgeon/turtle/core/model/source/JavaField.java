package org.burgeon.turtle.core.model.source;

import lombok.Data;

import java.util.List;

/**
 * Java属性信息
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class JavaField {

    private JavaComment comment;

    private List<JavaAnnotation> annotations;

    private JavaModifier modifier;

    private JavaClass type;

    private String name;

}
