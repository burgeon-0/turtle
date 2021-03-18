package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ParameterType;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数类型推断器职责链
 *
 * @author luxiaocong
 * @createdOn 2021/3/17
 */
public class ParameterTypeHandlerChain {

    private List<ParameterTypeHandler> handlers = new ArrayList<>();

    /**
     * 推断参数类型
     *
     * @param ctTypeReference
     * @return
     */
    ParameterType handle(CtTypeReference<?> ctTypeReference) {
        ParameterType type;
        for (ParameterTypeHandler handler : handlers) {
            type = handler.handle(ctTypeReference);
            if (type != null) {
                return type;
            }
        }
        return ParameterType.OBJECT;
    }

    /**
     * 添加参数类型推断器
     *
     * @param handler
     */
    public void addHandler(ParameterTypeHandler handler) {
        handlers.add(handler);
    }

    /**
     * 移除参数类型推断器
     *
     * @param handler
     */
    public void removeHandler(ParameterTypeHandler handler) {
        handlers.remove(handler);
    }

}
