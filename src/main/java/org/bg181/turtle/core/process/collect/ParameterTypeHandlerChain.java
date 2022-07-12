package org.bg181.turtle.core.process.collect;

import org.bg181.turtle.core.model.api.ParameterType;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数类型推断器职责链
 *
 * @author Sam Lu
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
    public void addParameterTypeHandler(ParameterTypeHandler handler) {
        handlers.add(handler);
    }

    /**
     * 移除参数类型推断器
     *
     * @param handler
     */
    public void removeParameterTypeHandler(ParameterTypeHandler handler) {
        handlers.remove(handler);
    }

}
