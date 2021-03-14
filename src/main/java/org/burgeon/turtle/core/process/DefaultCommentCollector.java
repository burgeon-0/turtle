package org.burgeon.turtle.core.process;

import org.burgeon.turtle.common.Constants;
import org.burgeon.turtle.common.StringUtils;
import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.api.HttpApi;
import org.burgeon.turtle.core.model.source.SourceProject;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtJavaDoc;
import spoon.reflect.code.CtJavaDocTag;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

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

    @Override
    public void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context) {
        List<ApiGroup> apiGroups = apiProject.getGroups();
        for (ApiGroup group : apiGroups) {
            CtClass<?> ctClass = sourceProject.getCtClass(group.getId());
            CtJavaDoc classDoc = getLastCtJavaDoc(ctClass);
            group.setName(getGroupName(ctClass, classDoc));
            if (classDoc != null) {
                group.setDescription(getGroupDescription(group, classDoc));
                group.setVersion(getApiVersion(classDoc));
            }

            List<HttpApi> httpApis = group.getHttpApis();
            for (HttpApi httpApi : httpApis) {
                CtMethod<?> ctMethod = sourceProject.getCtMethod(httpApi.getId());
                CtJavaDoc methodDoc = getLastCtJavaDoc(ctMethod);
                httpApi.setName(getApiName(ctMethod, methodDoc));
                if (methodDoc != null) {
                    httpApi.setDescription(getApiDescription(httpApi, methodDoc));
                    httpApi.setVersion(getApiVersion(methodDoc));
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

}
