package org.burgeon.turtle.core.process.collect;

import org.burgeon.turtle.core.model.api.ParameterType;
import spoon.reflect.reference.CtTypeReference;

/**
 * 【Java基础类型】参数类型推断器
 *
 * @author Sam Lu
 * @createdOn 2021/3/17
 */
public class PrimitiveParameterTypeHandler implements ParameterTypeHandler {

    private String arraySuffix = "[]";

    @Override
    public ParameterType handle(CtTypeReference<?> ctTypeReference) {
        ParameterType type = null;
        String typeName = ctTypeReference.getQualifiedName();
        switch (typeName) {
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
                type = ParameterType.NUMBER;
                break;
            case "boolean":
                type = ParameterType.BOOLEAN;
                break;
            case "char":
                type = ParameterType.STRING;
                break;
            default:
                if (typeName.endsWith(arraySuffix)) {
                    type = ParameterType.ARRAY;
                }
                break;
        }
        return type;
    }

}
