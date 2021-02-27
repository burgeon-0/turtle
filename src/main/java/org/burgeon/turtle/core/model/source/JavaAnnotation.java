package org.burgeon.turtle.core.model.source;

import lombok.Data;

import java.util.List;

/**
 * Java注解
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class JavaAnnotation {

    private JavaComment comment;

    private List<JavaAnnotation> annotations;

    private String name;

    private List<JavaMethod> methods;

}
