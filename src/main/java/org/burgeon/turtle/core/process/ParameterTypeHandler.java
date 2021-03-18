package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ParameterType;
import spoon.reflect.reference.CtTypeReference;

/**
 * 参数类型推断器
 *
 * @author luxiaocong
 * @createdOn 2021/3/17
 */
public interface ParameterTypeHandler {

    /**
     * 推断参数类型
     *
     * @param ctTypeReference
     * @return
     */
    ParameterType handle(CtTypeReference<?> ctTypeReference);

}
