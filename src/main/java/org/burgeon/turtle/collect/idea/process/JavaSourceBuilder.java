package org.burgeon.turtle.collect.idea.process;

import com.intellij.lang.jvm.JvmModifier;
import com.intellij.lang.jvm.JvmParameter;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import org.apache.commons.lang3.StringUtils;
import org.burgeon.turtle.collect.idea.utils.ExpressionResolveUtils;
import org.burgeon.turtle.core.model.source.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Java源文件构建器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
public class JavaSourceBuilder {

    private PsiJavaFile psiJavaFile;

    private PsiClass psiClass;

    private JavaClass javaClass;

    public JavaSourceBuilder(PsiJavaFile psiJavaFile, PsiClass psiClass) {
        this.psiJavaFile = psiJavaFile;
        this.psiClass = psiClass;
    }

    /**
     * 构建class文件
     *
     * @return
     */
    public JavaSourceBuilder buildClass() {
        javaClass = new JavaClass();
        javaClass.setPackageName(psiJavaFile.getPackageName());
        javaClass.setModifier(buildModifier(psiClass));
        javaClass.setName(psiClass.getName());
        javaClass.setClass(!psiClass.isInterface() && !psiClass.isAnnotationType() && !psiClass.isEnum());
        javaClass.setInterface(psiClass.isInterface());
        javaClass.setAnnotation(psiClass.isAnnotationType());
        javaClass.setEnum(psiClass.isEnum());
        javaClass.setSuperClass(buildSupperClass());
        javaClass.setInterfaces(buildInterfaces());
        javaClass.setInnerClasses(buildInnerClass());
        javaClass.setConstructors(buildConstructors());
        return this;
    }

    /**
     * 构建父类
     *
     * @return
     */
    private JavaClass buildSupperClass() {
        PsiClass psiSupperClass = psiClass.getSuperClass();
        if (psiSupperClass == null) {
            return null;
        }

        JavaClass javaClass = buildJavaClass(psiSupperClass);
        return javaClass;
    }

    /**
     * 构建父接口
     *
     * @return
     */
    private JavaClass[] buildInterfaces() {
        PsiClass[] psiInterfaces = psiClass.getInterfaces();
        if (psiInterfaces == null) {
            return null;
        }

        JavaClass[] javaClasses = new JavaClass[psiInterfaces.length];
        for (int i = 0; i < psiInterfaces.length; i++) {
            PsiClass psiInterface = psiInterfaces[i];
            JavaClass javaClass = buildJavaClass(psiInterface);
            javaClasses[i] = javaClass;
        }
        return javaClasses;
    }

    /**
     * 构建内部类
     *
     * @return
     */
    private JavaClass[] buildInnerClass() {
        PsiClass[] psiInnerClasses = psiClass.getInnerClasses();
        if (psiInnerClasses == null) {
            return null;
        }

        JavaClass[] javaClasses = new JavaClass[psiInnerClasses.length];
        for (int i = 0; i < psiInnerClasses.length; i++) {
            PsiClass psiInnerClass = psiInnerClasses[i];
            JavaClass javaClass = buildJavaClass(psiInnerClass);
            javaClasses[i] = javaClass;
        }
        return javaClasses;
    }

    /**
     * 构建构造方法
     *
     * @return
     */
    private JavaMethod[] buildConstructors() {
        PsiMethod[] constructors = psiClass.getConstructors();
        if (constructors == null) {
            return null;
        }

        JavaMethod[] javaMethods = new JavaMethod[constructors.length];
        for (int i = 0; i < constructors.length; i++) {
            PsiMethod psiMethod = constructors[i];
            JavaMethod javaMethod = buildJavaMethod(psiMethod);
            javaMethods[i] = javaMethod;
        }
        return javaMethods;
    }

    /**
     * 构建JavaClass
     *
     * @param psiClass
     * @return
     */
    private JavaClass buildJavaClass(PsiClass psiClass) {
        JavaSourceBuilder javaSourceBuilder = new JavaSourceBuilder(psiJavaFile, psiClass);
        JavaClass javaClass = javaSourceBuilder.buildClass()
                .buildComment()
                .buildAnnotations()
                .buildMethods()
                .buildFields()
                .build();
        return javaClass;
    }

    /**
     * 构建注释
     *
     * @return
     */
    public JavaSourceBuilder buildComment() {
        PsiDocComment psiDocComment = psiClass.getDocComment();
        javaClass.setComment(buildJavaComment(psiDocComment));
        return this;
    }

    /**
     * 构建注解
     *
     * @return
     */
    public JavaSourceBuilder buildAnnotations() {
        PsiAnnotation[] psiAnnotations = psiClass.getAnnotations();
        javaClass.setAnnotations(buildJavaAnnotations(psiAnnotations));
        return this;
    }

    /**
     * 构建方法
     *
     * @return
     */
    public JavaSourceBuilder buildMethods() {
        PsiMethod[] psiMethods = psiClass.getMethods();
        if (psiMethods != null) {
            List<PsiMethod> psiMemberMethods = new ArrayList<>();
            for (PsiMethod psiMethod : psiMethods) {
                if (psiMethod.getReturnType() != null) {
                    psiMemberMethods.add(psiMethod);
                }
            }

            JavaMethod[] javaMethods = new JavaMethod[psiMemberMethods.size()];
            for (int i = 0; i < psiMemberMethods.size(); i++) {
                PsiMethod psiMethod = psiMemberMethods.get(i);
                JavaMethod javaMethod = buildJavaMethod(psiMethod);
                javaMethods[i] = javaMethod;
            }
            javaClass.setMethods(javaMethods);
        }
        return this;
    }

    /**
     * 构建属性
     *
     * @return
     */
    public JavaSourceBuilder buildFields() {
        PsiField[] psiFields = psiClass.getFields();
        if (psiFields != null) {
            JavaField[] javaFields = new JavaField[psiFields.length];
            for (int i = 0; i < psiFields.length; i++) {
                PsiField psiField = psiFields[i];
                JavaField javaField = new JavaField();
                PsiDocComment psiDocComment = psiField.getDocComment();
                javaField.setComment(buildJavaComment(psiDocComment));
                PsiAnnotation[] psiAnnotations = psiField.getAnnotations();
                javaField.setAnnotations(buildJavaAnnotations(psiAnnotations));
                javaField.setModifier(buildModifier(psiField));
                javaField.setName(psiField.getName());
                PsiType psiType = psiField.getType();
                javaField.setType(buildJavaType(psiType));
                javaFields[i] = javaField;
            }
            javaClass.setFields(javaFields);
        }
        return this;
    }

    /**
     * 完成构建
     *
     * @return
     */
    public JavaClass build() {
        return javaClass;
    }

    /**
     * 构建修饰符
     *
     * @return
     */
    private JavaModifier buildModifier(PsiModifierListOwner psiModifierListOwner) {
        JavaModifier javaModifier = new JavaModifier();
        javaModifier.setPublic(psiModifierListOwner.hasModifier(JvmModifier.PUBLIC));
        javaModifier.setPrivate(psiModifierListOwner.hasModifier(JvmModifier.PRIVATE));
        javaModifier.setProtected(psiModifierListOwner.hasModifier(JvmModifier.PROTECTED));
        javaModifier.setStatic(psiModifierListOwner.hasModifier(JvmModifier.STATIC));
        javaModifier.setFinal(psiModifierListOwner.hasModifier(JvmModifier.FINAL));
        javaModifier.setSynchronized(psiModifierListOwner.hasModifier(JvmModifier.SYNCHRONIZED));
        javaModifier.setVolatile(psiModifierListOwner.hasModifier(JvmModifier.VOLATILE));
        javaModifier.setTransient(psiModifierListOwner.hasModifier(JvmModifier.TRANSIENT));
        javaModifier.setNative(psiModifierListOwner.hasModifier(JvmModifier.NATIVE));
        javaModifier.setAbstract(psiModifierListOwner.hasModifier(JvmModifier.ABSTRACT));
        javaModifier.setStrict(psiModifierListOwner.hasModifier(JvmModifier.STRICTFP));
        return javaModifier;
    }

    /**
     * 构建JavaComment
     *
     * @param psiDocComment
     * @return
     */
    private JavaComment buildJavaComment(PsiDocComment psiDocComment) {
        if (psiDocComment == null) {
            return null;
        }

        JavaComment javaComment = new JavaComment();
        javaComment.setText(psiDocComment.getText());
        PsiDocTag[] psiDocTags = psiDocComment.getTags();
        if (psiDocTags != null) {
            JavaCommentTag[] javaCommentTags = new JavaCommentTag[psiDocTags.length];
            for (int i = 0; i < psiDocTags.length; i++) {
                PsiDocTag psiDocTag = psiDocTags[i];
                JavaCommentTag javaCommentTag = new JavaCommentTag();
                javaCommentTag.setName(psiDocTag.getName());
                String text = psiDocTag.getText();
                javaCommentTag.setValue(getJavaCommentTagValue(javaCommentTag.getName(), text));
                javaCommentTags[i] = javaCommentTag;
            }
            javaComment.setTags(javaCommentTags);
        }
        return javaComment;
    }

    /**
     * 获取JavaComment标签值
     *
     * @param tagName
     * @param text
     * @return
     */
    private String getJavaCommentTagValue(String tagName, String text) {
        String value;
        int firstIndex = text.indexOf(tagName) + tagName.length();
        int lastIndex = text.indexOf("\n");
        if (lastIndex != -1) {
            value = text.substring(firstIndex, lastIndex);
        } else {
            value = text.substring(firstIndex);
        }
        value = StringUtils.trim(value);
        return value;
    }

    /**
     * 构建JavaAnnotation
     *
     * @param psiAnnotations
     * @return
     */
    private JavaAnnotation[] buildJavaAnnotations(PsiAnnotation[] psiAnnotations) {
        if (psiAnnotations != null) {
            return null;
        }

        JavaAnnotation[] javaAnnotations = new JavaAnnotation[psiAnnotations.length];
        for (int i = 0; i < psiAnnotations.length; i++) {
            PsiAnnotation psiAnnotation = psiAnnotations[i];
            JavaAnnotation javaAnnotation = buildJavaAnnotation(psiAnnotation);
            javaAnnotations[i] = javaAnnotation;
        }
        return javaAnnotations;
    }

    /**
     * 构建JavaAnnotation
     *
     * @param psiAnnotation
     * @return
     */
    private JavaAnnotation buildJavaAnnotation(PsiAnnotation psiAnnotation) {
        JavaAnnotation javaAnnotation = new JavaAnnotation();
        javaAnnotation.setName(psiAnnotation.getNameReferenceElement().getReferenceName());

        List<JvmAnnotationAttribute> jvmAnnotationAttributes = psiAnnotation.getAttributes();
        if (jvmAnnotationAttributes != null) {
            JavaAnnotationAttribute[] javaAnnotationAttributes =
                    new JavaAnnotationAttribute[jvmAnnotationAttributes.size()];
            for (int i = 0; i < jvmAnnotationAttributes.size(); i++) {
                JvmAnnotationAttribute jvmAnnotationAttribute = jvmAnnotationAttributes.get(i);
                JavaAnnotationAttribute javaAnnotationAttribute = new JavaAnnotationAttribute();
                String attributeName = jvmAnnotationAttribute.getAttributeName();
                Object[] attributeValues = getJavaAnnotationAttributeValues(psiAnnotation, attributeName);
                javaAnnotationAttribute.setName(attributeName);
                javaAnnotationAttribute.setValues(attributeValues);
                javaAnnotationAttributes[i] = javaAnnotationAttribute;
            }
            javaAnnotation.setAttributes(javaAnnotationAttributes);
        }
        return javaAnnotation;
    }

    /**
     * 获取JavaAnnotation属性值
     *
     * @param psiAnnotation
     * @param attributeName
     * @return
     */
    private Object[] getJavaAnnotationAttributeValues(PsiAnnotation psiAnnotation, String attributeName) {
        PsiAnnotationMemberValue psiAnnotationMemberValue = psiAnnotation.findAttributeValue(attributeName);
        if (psiAnnotationMemberValue instanceof PsiArrayInitializerMemberValue) {
            PsiArrayInitializerMemberValue psiArrayInitializerMemberValue = (PsiArrayInitializerMemberValue)
                    psiAnnotationMemberValue;
            PsiAnnotationMemberValue[] psiAnnotationMemberValues = psiArrayInitializerMemberValue
                    .getInitializers();
            Object[] attributeValues = new Object[psiAnnotationMemberValues.length];
            for (int i = 0; i < psiAnnotationMemberValues.length; i++) {
                attributeValues[i] = getJavaAnnotationAttributeValue(psiAnnotationMemberValues[i]);
            }
            return attributeValues;
        } else {
            Object attributeValue = getJavaAnnotationAttributeValue(psiAnnotationMemberValue);
            return new Object[]{attributeValue};
        }
    }

    /**
     * 获取JavaAnnotation属性值
     *
     * @param psiAnnotationMemberValue
     * @return
     */
    private Object getJavaAnnotationAttributeValue(PsiAnnotationMemberValue psiAnnotationMemberValue) {
        Object attributeValue = StringUtils.deleteWhitespace(psiAnnotationMemberValue.getText());
        if (psiAnnotationMemberValue instanceof PsiExpression) {
            Object value = ExpressionResolveUtils.resolve((PsiExpression) psiAnnotationMemberValue);
            if (value != null) {
                attributeValue = value;
            }
        }
        return attributeValue;
    }

    /**
     * 构建JavaMethod
     *
     * @param psiMethod
     * @return
     */
    private JavaMethod buildJavaMethod(PsiMethod psiMethod) {
        JavaMethod javaMethod = new JavaMethod();
        PsiDocComment psiDocComment = psiMethod.getDocComment();
        javaMethod.setComment(buildJavaComment(psiDocComment));
        PsiAnnotation[] psiAnnotations = psiMethod.getAnnotations();
        javaMethod.setAnnotations(buildJavaAnnotations(psiAnnotations));
        javaMethod.setModifier(buildModifier(psiMethod));
        javaMethod.setName(psiMethod.getName());
        PsiType psiType = psiMethod.getReturnType();
        javaMethod.setReturnType(buildJavaType(psiType));
        JvmParameter[] psiParameters = psiMethod.getParameters();
        javaMethod.setParameters(buildJavaMethodParameters(psiParameters));
        return javaMethod;
    }

    /**
     * 构建JavaType
     *
     * @param psiType
     * @return
     */
    private JavaType buildJavaType(PsiType psiType) {
        return null;
    }

    /**
     * 构建JavaMethod参数
     *
     * @param psiParameters
     * @return
     */
    private JavaMethodParameter[] buildJavaMethodParameters(JvmParameter[] psiParameters) {
        return null;
    }

}
