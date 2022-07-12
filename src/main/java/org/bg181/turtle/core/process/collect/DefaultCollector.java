package org.bg181.turtle.core.process.collect;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bg181.turtle.core.common.CtModelHelper;
import org.bg181.turtle.core.model.api.ApiGroup;
import org.bg181.turtle.core.model.api.ApiProject;
import org.bg181.turtle.core.model.api.HttpApi;
import org.bg181.turtle.core.model.api.HttpMethod;
import org.bg181.turtle.core.model.source.SourceProject;
import org.bg181.turtle.core.utils.StringUtils;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.path.CtRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 默认收集器
 *
 * @author Sam Lu
 * @createdOn 2021/2/27
 */
@Slf4j
public class DefaultCollector implements Collector {

    /**
     * TODO 通过配置的方式，控制是否收集：包含某个注解的类
     */
    private static final String CONTROLLER_ANNOTATION_TYPE = "org.springframework.stereotype.Controller";
    private static final String REST_CONTROLLER_ANNOTATION_TYPE = "org.springframework.web.bind.annotation.RestController";

    private static final String REQUEST_MAPPING_ANNOTATION_TYPE = "org.springframework.web.bind.annotation.RequestMapping";
    private static final String DELETE_MAPPING_ANNOTATION_TYPE = "org.springframework.web.bind.annotation.DeleteMapping";
    private static final String GET_MAPPING_ANNOTATION_TYPE = "org.springframework.web.bind.annotation.GetMapping";
    private static final String PATCH_MAPPING_ANNOTATION_TYPE = "org.springframework.web.bind.annotation.PatchMapping";
    private static final String POST_MAPPING_ANNOTATION_TYPE = "org.springframework.web.bind.annotation.PostMapping";
    private static final String PUT_MAPPING_ANNOTATION_TYPE = "org.springframework.web.bind.annotation.PutMapping";

    private static final String METHOD_GET = "org.springframework.web.bind.annotation.RequestMethod.GET";
    private static final String METHOD_HEAD = "org.springframework.web.bind.annotation.RequestMethod.HEAD";
    private static final String METHOD_POST = "org.springframework.web.bind.annotation.RequestMethod.POST";
    private static final String METHOD_PUT = "org.springframework.web.bind.annotation.RequestMethod.PUT";
    private static final String METHOD_PATCH = "org.springframework.web.bind.annotation.RequestMethod.PATCH";
    private static final String METHOD_DELETE = "org.springframework.web.bind.annotation.RequestMethod.DELETE";
    private static final String METHOD_OPTIONS = "org.springframework.web.bind.annotation.RequestMethod.OPTIONS";
    private static final String METHOD_TRACE = "org.springframework.web.bind.annotation.RequestMethod.TRACE";

    @Override
    public void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context) {
        log.debug("Collect by default collector.");

        List<ApiGroup> groups = new ArrayList<>();
        apiProject.setGroups(groups);

        CtModel model = sourceProject.getModel();
        List<CtClass<?>> ctClasses = findApiClass(model);
        for (CtClass<?> ctClass : ctClasses) {
            String classKey = CtModelHelper.getCtClassKey(ctClass);
            ApiGroup group = new ApiGroup();
            group.setId(classKey);
            groups.add(group);
            List<HttpApi> httpApis = new ArrayList<>();
            group.setHttpApis(httpApis);

            apiProject.putApiGroup(classKey, group);
            sourceProject.putCtClass(classKey, ctClass);

            String basePath = getBasePath(ctClass);
            List<HttpApiMaterial> materials = findApiMethod(ctClass);
            for (HttpApiMaterial material : materials) {
                CtMethod<?> ctMethod = material.getCtMethod();
                String methodKey = CtModelHelper.getCtMethodKey(ctMethod);
                HttpApi httpApi = getHttpApi(basePath, material);
                httpApi.setId(methodKey);
                httpApis.add(httpApi);

                apiProject.putHttpApi(methodKey, httpApi);
                sourceProject.putCtMethod(methodKey, ctMethod);
            }
        }

        context.collectNext(apiProject, sourceProject);
    }

    /**
     * 查找接口类
     *
     * @param model
     * @return
     */
    private List<CtClass<?>> findApiClass(CtModel model) {
        List<CtClass<?>> results = new ArrayList<>();
        Collection<CtType<?>> ctTypes = model.getAllTypes();
        for (CtType<?> ctType : ctTypes) {
            if (ctType.isClass()) {
                CtClass<?> ctClass = (CtClass<?>) ctType;
                List<CtAnnotation<?>> ctAnnotations = ctClass.getAnnotations();
                if (ctAnnotations != null) {
                    for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
                        String qualifiedName = ctAnnotation.getType().getQualifiedName();
                        if (CONTROLLER_ANNOTATION_TYPE.equals(qualifiedName)
                                || REST_CONTROLLER_ANNOTATION_TYPE.equals(qualifiedName)) {
                            results.add(ctClass);
                            break;
                        }
                    }
                }
            }
        }
        return results;
    }

    /**
     * 查找接口方法
     *
     * @return
     */
    private List<HttpApiMaterial> findApiMethod(CtClass<?> ctClass) {
        List<HttpApiMaterial> results = new ArrayList<>();
        Set<CtMethod<?>> ctMethods = ctClass.getMethods();
        for (CtMethod<?> ctMethod : ctMethods) {
            List<CtAnnotation<?>> ctAnnotations = ctMethod.getAnnotations();
            if (ctAnnotations != null) {
                for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
                    String qualifiedName = ctAnnotation.getType().getQualifiedName();
                    if (REQUEST_MAPPING_ANNOTATION_TYPE.equals(qualifiedName)
                            || DELETE_MAPPING_ANNOTATION_TYPE.equals(qualifiedName)
                            || GET_MAPPING_ANNOTATION_TYPE.equals(qualifiedName)
                            || PATCH_MAPPING_ANNOTATION_TYPE.equals(qualifiedName)
                            || POST_MAPPING_ANNOTATION_TYPE.equals(qualifiedName)
                            || PUT_MAPPING_ANNOTATION_TYPE.equals(qualifiedName)) {
                        HttpApiMaterial httpApiMaterial = new HttpApiMaterial();
                        httpApiMaterial.setCtMethod(ctMethod);
                        httpApiMaterial.setCtAnnotation(ctAnnotation);
                        results.add(httpApiMaterial);
                        break;
                    }
                }
            }
        }
        return results;
    }

    /**
     * 获取HttpApi
     *
     * @param basePath
     * @param material
     * @return
     */
    private HttpApi getHttpApi(String basePath, HttpApiMaterial material) {
        HttpApi httpApi = new HttpApi();
        collectHttpMethod(httpApi, material.getCtAnnotation());
        collectHttpPath(basePath, httpApi, material.getCtAnnotation());
        return httpApi;
    }

    /**
     * 收集HttpMethod
     *
     * @param httpApi
     * @param ctAnnotation
     */
    private void collectHttpMethod(HttpApi httpApi, CtAnnotation<?> ctAnnotation) {
        String qualifiedName = ctAnnotation.getType().getQualifiedName();
        switch (qualifiedName) {
            case REQUEST_MAPPING_ANNOTATION_TYPE:
                String method = ctAnnotation.getValue("method").toString();
                collectHttpMethodByRequestMapping(httpApi, method);
                break;
            case DELETE_MAPPING_ANNOTATION_TYPE:
                httpApi.setHttpMethod(HttpMethod.DELETE);
                break;
            case GET_MAPPING_ANNOTATION_TYPE:
                httpApi.setHttpMethod(HttpMethod.GET);
                break;
            case PATCH_MAPPING_ANNOTATION_TYPE:
                httpApi.setHttpMethod(HttpMethod.PATCH);
                break;
            case POST_MAPPING_ANNOTATION_TYPE:
                httpApi.setHttpMethod(HttpMethod.POST);
                break;
            case PUT_MAPPING_ANNOTATION_TYPE:
                httpApi.setHttpMethod(HttpMethod.PUT);
                break;
            default:
                httpApi.setHttpMethod(HttpMethod.GET);
                break;
        }
    }

    /**
     * 通过RequestMapping收集HttpMethod
     *
     * @param httpApi
     * @param method
     */
    private void collectHttpMethodByRequestMapping(HttpApi httpApi, String method) {
        switch (method) {
            case METHOD_GET:
                httpApi.setHttpMethod(HttpMethod.GET);
                break;
            case METHOD_HEAD:
                httpApi.setHttpMethod(HttpMethod.HEAD);
                break;
            case METHOD_POST:
                httpApi.setHttpMethod(HttpMethod.POST);
                break;
            case METHOD_PUT:
                httpApi.setHttpMethod(HttpMethod.PUT);
                break;
            case METHOD_PATCH:
                httpApi.setHttpMethod(HttpMethod.PATCH);
                break;
            case METHOD_DELETE:
                httpApi.setHttpMethod(HttpMethod.DELETE);
                break;
            case METHOD_OPTIONS:
                httpApi.setHttpMethod(HttpMethod.OPTIONS);
                break;
            case METHOD_TRACE:
                httpApi.setHttpMethod(HttpMethod.TRACE);
                break;
            default:
                httpApi.setHttpMethod(HttpMethod.GET);
                break;
        }
    }

    /**
     * 获取接口根路径
     *
     * @param ctClass
     * @return
     */
    private String getBasePath(CtClass<?> ctClass) {
        List<CtAnnotation<?>> ctAnnotations = ctClass.getAnnotations();
        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String qualifiedName = ctAnnotation.getType().getQualifiedName();
            if (REQUEST_MAPPING_ANNOTATION_TYPE.equals(qualifiedName)) {
                String path = getAnnotationValue(ctAnnotation, "value");
                if (StringUtils.isBlank(path)) {
                    path = getAnnotationValue(ctAnnotation, "name");
                    if (StringUtils.isBlank(path)) {
                        path = getAnnotationValue(ctAnnotation, "path");
                    }
                }
                return path;
            }
        }
        return "";
    }

    /**
     * 收集接口path
     *
     * @param basePath
     * @param httpApi
     * @param ctAnnotation
     */
    private void collectHttpPath(String basePath, HttpApi httpApi, CtAnnotation<?> ctAnnotation) {
        String path = getAnnotationValue(ctAnnotation, "value");
        if (StringUtils.isBlank(path)) {
            path = getAnnotationValue(ctAnnotation, "name");
            if (StringUtils.isBlank(path)) {
                path = getAnnotationValue(ctAnnotation, "path");
            }
        }
        path = basePath + path;
        httpApi.setPath(path);
    }

    /**
     * 获取注解的属性值
     *
     * @param ctAnnotation
     * @param key
     * @return
     */
    private String getAnnotationValue(CtAnnotation<?> ctAnnotation, String key) {
        if (ctAnnotation.getValues().isEmpty()) {
            return "";
        }
        if (!ctAnnotation.getValues().containsKey(key)) {
            return "";
        }
        return ctAnnotation.getValue(key).partiallyEvaluate().getValueByRole(CtRole.VALUE);
    }

    /**
     * 将CtMethod和CtAnnotation放在一起，以做HttpApi分析
     */
    @Data
    private class HttpApiMaterial {

        private CtMethod<?> ctMethod;
        private CtAnnotation<?> ctAnnotation;

    }

}
