package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.model.source.SourceProject;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.reference.CtArrayTypeReferenceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 默认参数收集器
 *
 * @author luxiaocong
 * @createdOn 2021/3/14
 */
public class DefaultParameterCollector implements Collector {

    private static final String PRIVATE = "private";

    private static final String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";
    private static final String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
    private static final String MODEL_ATTRIBUTE = "org.springframework.web.bind.annotation.ModelAttribute";
    private static final String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";

    private static final String NOT_NULL = "NotNull";
    private static final String NOT_BLANK = "NotBlank";
    private static final String NOT_EMPTY = "NotEmpty";

    private static final String JAVAX_VALIDATION = "javax.validation.constraints";
    private static final String HIBERNATE_VALIDATOR = "org.hibernate.validator.constraints";

    /**
     * 排除项，无需解析、收集的参数
     */
    private List<String> exclusions = new ArrayList<>();
    private List<String> exclusionPatterns = new ArrayList<>();

    {
        exclusions.add("org.springframework.validation.*");
        exclusions.add("javax.servlet.**");
    }

    private Processor processor;

    public DefaultParameterCollector(Processor processor) {
        this.processor = processor;
    }

    /**
     * 添加排除项
     *
     * @param exclusion
     */
    public void addExclusion(String exclusion) {
        exclusions.add(exclusion);
    }

    /**
     * 移除排除项
     *
     * @param exclusion
     */
    public void removeExclusion(String exclusion) {
        exclusions.remove(exclusion);
    }

    /**
     * 初始化排除项的正则表达式
     */
    private void initExclusionPatterns() {
        for (String exclusion : exclusions) {
            if (exclusion.endsWith("**")) {
                exclusion = exclusion.replace("**", "[\\$|_|A-Z|a-z]([\\$|_|0-9|A-Z|a-z])*(\\.[\\$|_|A-Z|a-z]([\\$|_|0-9|A-Z|a-z])*)*");
            } else if (exclusion.endsWith("*")) {
                exclusion = exclusion.replace("*", "[\\$|_|A-Z|a-z]([\\$|_|0-9|A-Z|a-z])*");
            }
            exclusion = String.format("^%s$", exclusion);
            exclusionPatterns.add(exclusion);
        }
    }

    @Override
    public void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context) {
        initExclusionPatterns();

        List<ApiGroup> apiGroups = apiProject.getGroups();
        for (ApiGroup group : apiGroups) {
            List<HttpApi> httpApis = group.getHttpApis();
            for (HttpApi httpApi : httpApis) {
                CtMethod<?> ctMethod = sourceProject.getCtMethod(httpApi.getId());
                List<CtParameter<?>> ctParameters = ctMethod.getParameters();
                for (CtParameter<?> ctParameter : ctParameters) {
                    parseRequestParameter(httpApi, ctParameter);
                }
            }
        }

        context.collectNext(apiProject, sourceProject);
    }

    /**
     * 解析Request参数
     *
     * @param httpApi
     * @param ctParameter
     */
    private void parseRequestParameter(HttpApi httpApi, CtParameter<?> ctParameter) {
        String type = ctParameter.getType().getQualifiedName();
        for (String exclusion : exclusionPatterns) {
            if (Pattern.matches(exclusion, type)) {
                return;
            }
        }

        List<CtAnnotation<?>> ctAnnotations = ctParameter.getAnnotations();
        Parameter parameter = buildParameter(ctParameter.getSimpleName(), ctParameter.getType(),
                ctAnnotations);
        switch (getHttpParameterType(httpApi, ctAnnotations)) {
            case PATH_PARAMETER:
                initPathParameters(httpApi);
                httpApi.getPathParameters().add(parameter);
                break;
            case URI_PARAMETER:
                initUriParameters(httpApi);
                httpApi.getUriParameters().add(parameter);
                break;
            case BODY_PARAMETER:
                initHttpRequest(httpApi);
                httpApi.getHttpRequest().getBody().add(parameter);
                break;
            default:
                break;
        }
    }

    /**
     * 判断HTTP参数类型
     *
     * @param httpApi
     * @param ctAnnotations
     * @return
     * @see HttpParameterType
     */
    private HttpParameterType getHttpParameterType(HttpApi httpApi, List<CtAnnotation<?>> ctAnnotations) {
        if (ctAnnotations == null) {
            if (httpApi.getHttpMethod().equals(HttpMethod.POST)
                    || httpApi.getHttpMethod().equals(HttpMethod.PUT)
                    || httpApi.getHttpMethod().equals(HttpMethod.DELETE)
                    || httpApi.getHttpMethod().equals(HttpMethod.PATCH)) {
                return HttpParameterType.BODY_PARAMETER;
            } else {
                return HttpParameterType.URI_PARAMETER;
            }
        }

        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String name = ctAnnotation.getType().getQualifiedName();
            switch (name) {
                case PATH_VARIABLE:
                    return HttpParameterType.PATH_PARAMETER;
                case REQUEST_PARAM:
                case MODEL_ATTRIBUTE:
                    return HttpParameterType.URI_PARAMETER;
                case REQUEST_BODY:
                    return HttpParameterType.BODY_PARAMETER;
                default:
                    break;
            }
        }
        return HttpParameterType.BODY_PARAMETER;
    }

    /**
     * 初始化请求路径参数
     *
     * @param httpApi
     */
    private void initPathParameters(HttpApi httpApi) {
        List<Parameter> parameters = httpApi.getPathParameters();
        if (parameters == null) {
            parameters = new ArrayList<>();
            httpApi.setPathParameters(parameters);
        }
    }

    /**
     * 初始化请求URI参数
     *
     * @param httpApi
     */
    private void initUriParameters(HttpApi httpApi) {
        List<Parameter> parameters = httpApi.getUriParameters();
        if (parameters == null) {
            parameters = new ArrayList<>();
            httpApi.setUriParameters(parameters);
        }
    }

    /**
     * 初始化请求Body参数
     *
     * @param httpApi
     */
    private void initHttpRequest(HttpApi httpApi) {
        HttpRequest httpRequest = httpApi.getHttpRequest();
        if (httpRequest == null) {
            httpRequest = new HttpRequest();
            httpApi.setHttpRequest(httpRequest);
            httpRequest.setBody(new ArrayList<>());
        } else if (httpRequest.getBody() == null) {
            httpRequest.setBody(new ArrayList<>());
        }
    }

    /**
     * 构建请求参数
     *
     * @param fieldName
     * @param ctTypeReference
     * @param ctAnnotations
     * @return
     */
    private Parameter buildParameter(String fieldName, CtTypeReference<?> ctTypeReference,
                                     List<CtAnnotation<?>> ctAnnotations) {
        Parameter parameter = new Parameter();
        parameter.setName(fieldName);
        ParameterTypeHandlerChain handlerChain = processor.getParameterTypeHandlerChain();
        ParameterType type = handlerChain.handle(ctTypeReference);
        parameter.setType(type);
        parameter.setRequired(isRequired(ctAnnotations));
        collectParameterDescription(parameter, ctAnnotations);

        if (type == ParameterType.ARRAY) {
            CtTypeReference<?> actualType;
            if (ctTypeReference instanceof CtArrayTypeReferenceImpl) {
                actualType = ((CtArrayTypeReferenceImpl) ctTypeReference).getArrayType();
            } else {
                actualType = ctTypeReference.getActualTypeArguments().get(0);
            }
            List<Parameter> childParameters = new ArrayList<>();
            Parameter childParameter = buildParameter(null, actualType, null);
            childParameters.add(childParameter);
            parameter.setChildParameters(childParameters);
        } else if (type == ParameterType.OBJECT) {
            Collection<CtFieldReference<?>> ctFieldReferences = ctTypeReference.getDeclaredFields();
            List<Parameter> childParameters = new ArrayList<>();
            for (CtFieldReference<?> ctFieldReference : ctFieldReferences) {
                CtField<?> ctField = ctFieldReference.getFieldDeclaration();
                if (isChildParameter(ctField)) {
                    Parameter childParameter = buildParameter(ctField.getSimpleName(),
                            ctField.getType(), ctField.getAnnotations());
                    childParameters.add(childParameter);
                }
            }
            parameter.setChildParameters(childParameters);
        }

        return parameter;
    }

    /**
     * 是否是子参数：只有private一个修饰符的参数才为子参数
     *
     * @param ctField
     * @return
     */
    private boolean isChildParameter(CtField<?> ctField) {
        if (ctField.getModifiers().size() == 1) {
            ModifierKind modifierKind = ctField.getModifiers().iterator().next();
            if (modifierKind.toString().equals(PRIVATE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断参数是否是必须的
     *
     * @param ctAnnotations
     * @return
     */
    private boolean isRequired(List<CtAnnotation<?>> ctAnnotations) {
        if (ctAnnotations == null) {
            return false;
        }

        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String qualifiedName = ctAnnotation.getType().getQualifiedName();
            if (qualifiedName.equals(PATH_VARIABLE) || qualifiedName.equals(REQUEST_PARAM)) {
                return ctAnnotation.getValue("required").getValueByRole(CtRole.VALUE);
            }

            String simpleName = ctAnnotation.getType().getSimpleName();
            if (simpleName.equals(NOT_NULL) || simpleName.equals(NOT_BLANK)
                    || simpleName.equals(NOT_EMPTY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 收集参数描述
     *
     * @param parameter
     * @param ctAnnotations
     */
    private void collectParameterDescription(Parameter parameter, List<CtAnnotation<?>> ctAnnotations) {
        if (ctAnnotations == null) {
            return;
        }

        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String qualifiedName = ctAnnotation.getType().getQualifiedName();
            String desc = parameter.getDescription();
            if (qualifiedName.startsWith(JAVAX_VALIDATION)
                    || qualifiedName.startsWith(HIBERNATE_VALIDATOR)) {
                String str = ctAnnotation.getValue("message").getValueByRole(CtRole.VALUE);
                desc = appendDescription(desc, str);
            }
            parameter.setDescription(desc);
        }
    }

    /**
     * 处理参数描述
     *
     * @param desc
     * @param str
     * @return
     */
    private String appendDescription(String desc, String str) {
        if (desc == null) {
            desc = str;
        } else {
            desc += " " + str;
        }
        return desc;
    }

}
