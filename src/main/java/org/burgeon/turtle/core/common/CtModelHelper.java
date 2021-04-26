package org.burgeon.turtle.core.common;

import org.burgeon.turtle.core.model.source.ParameterPosition;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.FactoryImpl;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.DefaultCoreFactory;
import spoon.support.StandardEnvironment;

/**
 * spoon model helper
 * <ol>
 * <li>
 * 从SourceProject将API信息收集到ApiProject，通常不是一次就能收集完成的，有时候可能需要扩展Collector，执行特定的收集规则。
 * 比如在我接触过的项目中，错误码可能是通过异常的方式抛出的，这种情况需要使用方实现自定义的Collector才能将错误码信息收集起来。
 * 本类提供的一系列获取CtElement的Key的方法，是为了能够方便从Http元素找到CtElement，或从CtElement找到Http元素
 * （包含HttpRequest、HttpResponse、HttpParameter等）。
 * </li>
 * </ol>
 *
 * @author Sam Lu
 * @createdOn 2021/3/18
 */
public class CtModelHelper {

    private static final String METHOD_RETURN_PARAMETER_NAME = ":return";
    private static final String ARRAY_SUB_PARAMETER_NAME = "[]";

    private static final Factory DEFAULT_FACTORY = new FactoryImpl(new DefaultCoreFactory(),
            new StandardEnvironment());
    private static final CtTypeReference<?> OBJECT_REFERENCE = DEFAULT_FACTORY
            .createClass("java.lang.Object").getReference();

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

    /**
     * 获取Element的key
     *
     * @param parentKey
     * @param subKey
     * @return
     */
    public static String getElementKey(String parentKey, String subKey) {
        return String.format("%s.%s", parentKey, subKey);
    }

    /**
     * 获取java.lang.Object的反射模型
     *
     * @return
     */
    public static CtTypeReference<?> getObjectReference() {
        return OBJECT_REFERENCE;
    }

}
