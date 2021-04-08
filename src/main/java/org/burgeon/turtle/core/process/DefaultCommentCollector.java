package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.common.CtModelHelper;
import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.core.utils.StringUtils;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtJavaDoc;
import spoon.reflect.code.CtJavaDocTag;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认注释收集器
 *
 * @author luxiaocong
 * @createdOn 2021/3/14
 */
public class DefaultCommentCollector implements Collector {

    private static final String COMMENT_TAG_GROUP = "group";
    private static final String COMMENT_TAG_VERSION = "version";
    private static final String COMMENT_TAG_HEADER = "header";
    private static final String COMMENT_TAG_PARAM = "param";

    @Override
    public void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context) {
        List<ApiGroup> apiGroups = apiProject.getGroups();
        for (ApiGroup group : apiGroups) {
            CtClass<?> ctClass = sourceProject.getCtClass(group.getId());
            CtJavaDoc classDoc = getLastCtJavaDoc(ctClass.getComments());
            group.setName(getGroupName(ctClass, classDoc));
            if (classDoc != null) {
                group.setDescription(getGroupDescription(group, classDoc));
                group.setVersion(getApiVersion(classDoc));
            }

            List<HttpApi> httpApis = group.getHttpApis();
            for (HttpApi httpApi : httpApis) {
                CtMethod<?> ctMethod = sourceProject.getCtMethod(httpApi.getId());
                CtJavaDoc methodDoc = getLastCtJavaDoc(ctMethod.getComments());
                httpApi.setName(getApiName(ctMethod, methodDoc));
                if (methodDoc != null) {
                    httpApi.setDescription(getApiDescription(httpApi, methodDoc));
                    httpApi.setVersion(getApiVersion(methodDoc));
                }
                collectMethodComment(apiProject, httpApi, ctMethod);

                collectParametersComment(httpApi.getPathParameters(), sourceProject);
                collectParametersComment(httpApi.getUriParameters(), sourceProject);
                if (httpApi.getHttpRequest() != null) {
                    collectParametersComment(httpApi.getHttpRequest().getBody(), sourceProject);
                }
                if (httpApi.getHttpResponse() != null) {
                    collectParametersComment(httpApi.getHttpResponse().getBody(), sourceProject);
                }
            }
        }

        context.collectNext(apiProject, sourceProject);
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
            if (StringUtils.notBlank(groupName)) {
                return groupName;
            }
            String mainComment = getMainComment(ctJavaDoc);
            if (StringUtils.notBlank(mainComment)) {
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
        String groupName = apiGroup.getName() + Constants.SEPARATOR_LINE_BREAK;
        String content = ctJavaDoc.getContent();
        if (content != null && groupName != null && content.startsWith(groupName)) {
            content = content.substring(groupName.length());
        }
        return StringUtils.leftStrip(content, Constants.SEPARATOR_LINE_BREAK);
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
            if (StringUtils.notBlank(mainComment)) {
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
     * 获取主注释：作为API群组和API接口的名称
     *
     * @param ctJavaDoc
     * @return
     */
    private String getMainComment(CtJavaDoc ctJavaDoc) {
        if (ctJavaDoc.getContent() != null) {
            String str = ctJavaDoc.getShortDescription();
            str = StringUtils.leftStrip(str, Constants.SEPARATOR_LINE_BREAK);
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
     * 收集方法注释
     *
     * @param apiProject
     * @param httpApi
     * @param ctMethod
     */
    private void collectMethodComment(ApiProject apiProject, HttpApi httpApi, CtMethod<?> ctMethod) {
        CtJavaDoc ctJavaDoc = getLastCtJavaDoc(ctMethod.getComments());
        if (ctJavaDoc != null) {
            List<CtJavaDocTag> ctJavaDocTags = ctJavaDoc.getTags();
            for (CtJavaDocTag ctJavaDocTag : ctJavaDocTags) {
                String content = ctJavaDocTag.getContent().trim();
                if (COMMENT_TAG_HEADER.equalsIgnoreCase(ctJavaDocTag.getRealName())) {
                    initHttpRequestHeaders(httpApi);
                    HttpHeader httpHeader = new HttpHeader();
                    httpApi.getHttpRequest().getHeaders().add(httpHeader);
                    if (content.contains(Constants.SEPARATOR_SPACE)) {
                        int index = content.indexOf(Constants.SEPARATOR_SPACE);
                        httpHeader.setName(content.substring(0, index));
                        httpHeader.setDescription(content.substring(index + 1).trim());
                    } else {
                        httpHeader.setName(content);
                    }
                } else if (COMMENT_TAG_PARAM.equalsIgnoreCase(ctJavaDocTag.getRealName())) {
                    if (content.contains(Constants.SEPARATOR_SPACE)) {
                        int index = content.indexOf(Constants.SEPARATOR_SPACE);
                        String parentKey = CtModelHelper.getCtMethodKey(ctMethod);
                        String key = CtModelHelper.getElementKey(parentKey, content.substring(0, index));
                        Parameter parameter = apiProject.getParameter(key);
                        if (parameter != null) {
                            parameter.setDescription(content.substring(index + 1).trim());
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化请求Headers
     *
     * @param httpApi
     */
    private void initHttpRequestHeaders(HttpApi httpApi) {
        HttpRequest httpRequest = httpApi.getHttpRequest();
        if (httpRequest == null) {
            httpRequest = new HttpRequest();
            httpApi.setHttpRequest(httpRequest);
            httpRequest.setHeaders(new ArrayList<>());
        } else if (httpRequest.getHeaders() == null) {
            httpRequest.setHeaders(new ArrayList<>());
        }
    }

    /**
     * 收集参数上的注释
     *
     * @param parameters
     * @param sourceProject
     */
    private void collectParametersComment(List<Parameter> parameters, SourceProject sourceProject) {
        if (parameters == null) {
            return;
        }

        for (Parameter parameter : parameters) {
            if (parameter.getParentParameter() != null
                    && parameter.getParentParameter().getType() == ParameterType.ARRAY) {
                collectParametersComment(parameter.getChildParameters(), sourceProject);
                continue;
            }
            CtElement ctElement = sourceProject.getCtElement(parameter.getId());
            if (!(ctElement instanceof CtMethod<?>)) {
                collectParameterComment(parameter, ctElement);
            } else if (parameter.getType() == ParameterType.OBJECT) {
                // TODO collect returnType comment
            }
            collectParametersComment(parameter.getChildParameters(), sourceProject);
        }
    }

    /**
     * 收集参数上的注释
     *
     * @param parameter
     * @param ctElement
     */
    private void collectParameterComment(Parameter parameter, CtElement ctElement) {
        List<CtComment> ctComments = ctElement.getComments();
        if (ctComments == null || ctComments.isEmpty()) {
            return;
        }
        String desc = parameter.getDescription();
        CtJavaDoc ctJavaDoc = getLastCtJavaDoc(ctComments);
        if (ctJavaDoc != null) {
            String str = ctJavaDoc.getContent();
            desc = appendDescription(desc, str);
        } else {
            String str = "";
            for (CtComment ctComment : ctComments) {
                str += ctComment.getContent();
            }
            desc = appendDescription(desc, str);
        }
        parameter.setDescription(desc);
    }

    /**
     * 获取元素上的最后一个CtJavaDoc
     *
     * @param ctComments
     * @return
     */
    private CtJavaDoc getLastCtJavaDoc(List<CtComment> ctComments) {
        if (ctComments == null) {
            return null;
        }
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
            desc = String.format("%s; %s", str, desc);
        }
        return desc;
    }

}
