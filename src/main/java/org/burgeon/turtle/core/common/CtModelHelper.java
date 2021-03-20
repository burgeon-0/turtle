package org.burgeon.turtle.core.common;

import org.burgeon.turtle.core.process.ParameterPosition;
import spoon.reflect.declaration.*;

/**
 * spoon model helper
 *
 * @author luxiaocong
 * @createdOn 2021/3/18
 */
public class CtModelHelper {

    private static final String METHOD_RETURN_PARAMETER_NAME = ":return";
    private static final String ARRAY_SUB_PARAMETER_NAME = "[]";

    /**
     * 获取CtClass的key
     *
     * @param ctClass
     * @return
     */
    public static String getCtClassKey(CtClass<?> ctClass) {
        return ctClass.getQualifiedName();
    }

    /**
     * 获取CtMethod的key
     *
     * @param ctMethod
     * @return
     */
    public static String getCtMethodKey(CtMethod<?> ctMethod) {
        CtClass<?> ctClass = (CtClass<?>) ctMethod.getParent();
        return String.format("%s.%s", ctClass.getQualifiedName(), ctMethod.getSignature());
    }

    /**
     * 获取Parameter的key
     *
     * @param parentKey
     * @param parameterPosition
     * @param ctNamedElement
     * @return
     */
    public static String getParameterKey(String parentKey, ParameterPosition parameterPosition,
                                         CtNamedElement ctNamedElement) {
        switch (parameterPosition) {
            case METHOD_RETURN:
            case ARRAY_SUB:
                return String.format("%s%s", parentKey,
                        getParameterName(parameterPosition, ctNamedElement));
            default:
                return String.format("%s.%s", parentKey,
                        getParameterName(parameterPosition, ctNamedElement));
        }
    }

    /**
     * 获取Parameter的name
     *
     * @param parameterPosition
     * @param ctNamedElement
     * @return
     */
    public static String getParameterName(ParameterPosition parameterPosition,
                                          CtNamedElement ctNamedElement) {

        switch (parameterPosition) {
            case METHOD_RETURN:
                return METHOD_RETURN_PARAMETER_NAME;
            case ARRAY_SUB:
                String qualifiedName = ((CtTypedElement<?>) ctNamedElement).getType().getQualifiedName();
                int index = qualifiedName.indexOf(ARRAY_SUB_PARAMETER_NAME);
                if (index > -1) {
                    return qualifiedName.substring(index);
                }
                return ARRAY_SUB_PARAMETER_NAME;
            default:
                return ctNamedElement.getSimpleName();
        }
    }

}
