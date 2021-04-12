package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ParameterType;
import spoon.reflect.reference.CtTypeReference;

import java.util.Set;

/**
 * 默认参数类型推断器
 *
 * @author luxiaocong
 * @createdOn 2021/3/17
 */
public class DefaultParameterTypeHandler implements ParameterTypeHandler {

    @Override
    public ParameterType handle(CtTypeReference<?> ctTypeReference) {
        ParameterType type;
        String typeName = ctTypeReference.getQualifiedName();
        switch (typeName) {
            case "java.lang.Byte":
            case "java.lang.Short":
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.math.BigInteger":
            case "java.math.BigDecimal":
            case "java.lang.Number":
            case "java.util.Date":
                type = ParameterType.NUMBER;
                break;
            case "java.lang.Boolean":
                type = ParameterType.BOOLEAN;
                break;
            case "java.lang.Character":
            case "java.lang.String":
            case "java.util.UUID":
                type = ParameterType.STRING;
                break;
            case "java.util.List":
            case "java.util.ArrayList":
            case "java.util.LinkedList":
            case "java.util.Set":
            case "java.util.HashSet":
            case "java.util.TreeSet":
            case "java.util.Collection":
                type = ParameterType.ARRAY;
                break;
            case "java.util.Map":
            case "java.util.HashMap":
            case "java.util.TreeMap":
            case "java.util.concurrent.ConcurrentHashMap":
                type = ParameterType.OBJECT;
                break;
            case "org.springframework.web.multipart.MultipartFile":
            case "org.springframework.web.multipart.commons.CommonsMultipartFile":
                type = ParameterType.FILE;
                break;
            default:
                type = getSupperType(ctTypeReference);
                break;
        }
        return type;
    }

    /**
     * 获取父类或父接口的类型
     *
     * @param ctTypeReference
     * @return
     */
    private ParameterType getSupperType(CtTypeReference<?> ctTypeReference) {
        ParameterType type;
        CtTypeReference<?> superclass = ctTypeReference.getSuperclass();
        if (superclass != null) {
            type = handle(superclass);
            if (type != null) {
                return type;
            }
        }

        Set<CtTypeReference<?>> superInterfaces = ctTypeReference.getSuperInterfaces();
        if (superInterfaces != null) {
            for (CtTypeReference<?> superInterface : superInterfaces) {
                type = handle(superInterface);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

}
