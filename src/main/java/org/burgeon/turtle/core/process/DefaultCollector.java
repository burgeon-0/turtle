package org.burgeon.turtle.core.process;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.common.Constants;
import org.burgeon.turtle.common.StringUtils;
import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.api.HttpApi;
import org.burgeon.turtle.core.model.api.HttpMethod;
import org.burgeon.turtle.core.model.source.SourceProject;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtJavaDoc;
import spoon.reflect.code.CtJavaDocTag;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 默认收集器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Slf4j
public class DefaultCollector implements Collector {

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

    private static final String COMMENT_TAG_GROUP = "group";
    private static final String COMMENT_TAG_VERSION = "version";

    @Override
    public void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context) {
        log.debug("Collect by default collector.");

        List<ApiGroup> groups = new ArrayList<>();
        apiProject.setGroups(groups);

        CtModel model = sourceProject.getModel();
        List<CtClass<?>> ctClasses = findApiClass(model);
        for (CtClass<?> ctClass : ctClasses) {
            ApiGroup group = new ApiGroup();
            CtJavaDoc ctJavaDoc = getLastCtJavaDoc(ctClass);
            group.setName(getGroupName(ctClass, ctJavaDoc));
            if (ctJavaDoc != null) {
                group.setDescription(getGroupDescription(group, ctJavaDoc));
                group.setVersion(getApiVersion(ctJavaDoc));
            }
            groups.add(group);
            List<HttpApi> httpApis = new ArrayList<>();
            group.setHttpApis(httpApis);

            String basePath = getBasePath(ctClass);
            List<HttpApiMaterial> materials = findApiMethod(ctClass);
            for (HttpApiMaterial material : materials) {
                HttpApi httpApi = getHttpApi(basePath, material);
                httpApis.add(httpApi);
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
     * 获取API群组名称
     *
     * @param ctClass
     * @param ctJavaDoc
     * @return
     */
    private String getGroupName(CtClass<?> ctClass, CtJavaDoc ctJavaDoc) {
        if (ctJavaDoc != null) {
            String groupName = getCommentTagValue(ctJavaDoc, COMMENT_TAG_GROUP);
            if (groupName != null && !"".equals(groupName)) {
                return groupName;
            }
            String mainComment = getMainComment(ctJavaDoc);
            if (mainComment != null && !"".equals(mainComment)) {
                return mainComment;
            }
        }
        return ctClass.getSimpleName();
    }

    /**
     * 获取API群组描述
     *
     * @param apiGroup
     * @param ctJavaDoc
     * @return
     */
    private String getGroupDescription(ApiGroup apiGroup, CtJavaDoc ctJavaDoc) {
        String groupName = apiGroup.getName();
        String content = ctJavaDoc.getContent();
        if (content != null && groupName != null && content.startsWith(groupName)) {
            content = content.substring(groupName.length());
        }
        return StringUtils.leftStrip(content, Constants.SEPARATOR_LINE_BREAK);
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
        CtMethod<?> ctMethod = material.getCtMethod();
        CtJavaDoc ctJavaDoc = getLastCtJavaDoc(ctMethod);
        httpApi.setName(getApiName(ctMethod, ctJavaDoc));
        if (ctJavaDoc != null) {
            httpApi.setDescription(getApiDescription(httpApi, ctJavaDoc));
            httpApi.setVersion(getApiVersion(ctJavaDoc));
        }
        collectHttpPath(basePath, httpApi, material.getCtAnnotation());
        collectHttpMethod(httpApi, material.getCtAnnotation());
        return httpApi;
    }

    /**
     * 获取API名称
     *
     * @param ctJavaDoc
     * @return
     */
    private String getApiName(CtMethod<?> ctMethod, CtJavaDoc ctJavaDoc) {
        if (ctJavaDoc != null) {
            String mainComment = getMainComment(ctJavaDoc);
            if (mainComment != null && !"".equals(mainComment)) {
                return mainComment;
            }
        }
        return ctMethod.getSimpleName();
    }

    /**
     * 获取API描述
     *
     * @param httpApi
     * @param ctJavaDoc
     * @return
     */
    private String getApiDescription(HttpApi httpApi, CtJavaDoc ctJavaDoc) {
        String apiName = httpApi.getName() + Constants.SEPARATOR_LINE_BREAK;
        String content = ctJavaDoc.getContent();
        if (content != null && apiName != null && content.startsWith(apiName)) {
            content = content.substring(apiName.length());
        }
        return StringUtils.leftStrip(content, Constants.SEPARATOR_LINE_BREAK);
    }

    /**
     * 获取接口版本
     *
     * @param ctJavaDoc
     * @return
     */
    private String getApiVersion(CtJavaDoc ctJavaDoc) {
        String apiVersion = getCommentTagValue(ctJavaDoc, COMMENT_TAG_VERSION);
        return apiVersion;
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
                return getAnnotationValue(ctAnnotation, "value");
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
        path = basePath + path;
        httpApi.setPath(path);
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
                collectHttpMethodByRequestMethod(httpApi, method);
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
        }
    }

    /**
     * 通过RequestMethod收集HttpMethod
     *
     * @param httpApi
     * @param method
     */
    private void collectHttpMethodByRequestMethod(HttpApi httpApi, String method) {
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
        }
    }

    /**
     * 获取元素上的最后一个CtJavaDoc
     *
     * @param ctElement
     * @return
     */
    private CtJavaDoc getLastCtJavaDoc(CtElement ctElement) {
        List<CtComment> ctComments = ctElement.getComments();
        CtJavaDoc ctJavaDoc = null;
        for (int i = ctComments.size() - 1; i >= 0; i--) {
            CtComment ctComment = ctComments.get(i);
            if (ctComment instanceof CtJavaDoc) {
                ctJavaDoc = (CtJavaDoc) ctComment;
                break;
            }
        }
        return ctJavaDoc;
    }

    /**
     * 获取主注释：作为API群组和API接口的名称
     *
     * @param ctJavaDoc
     * @return
     */
    private String getMainComment(CtJavaDoc ctJavaDoc) {
        if (ctJavaDoc.getContent() != null) {
            String str = ctJavaDoc.getShortDescription();
            if (str.contains(Constants.SEPARATOR_LINE_BREAK)) {
                str = str.split(Constants.SEPARATOR_LINE_BREAK)[0];
            }
            return str;
        }
        return ctJavaDoc.getContent();
    }

    /**
     * 获取注释的标签值
     *
     * @param ctJavaDoc
     * @return
     */
    private String getCommentTagValue(CtJavaDoc ctJavaDoc, String key) {
        List<CtJavaDocTag> ctJavaDocTags = ctJavaDoc.getTags();
        for (CtJavaDocTag ctJavaDocTag : ctJavaDocTags) {
            if (key.equalsIgnoreCase(ctJavaDocTag.getRealName())) {
                return ctJavaDocTag.getContent();
            }
        }
        return null;
    }

    /**
     * 获取注解的属性值
     *
     * @param ctAnnotation
     * @param key
     * @return
     */
    private String getAnnotationValue(CtAnnotation<?> ctAnnotation, String key) {
        if (ctAnnotation.getValues().size() == 0) {
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
