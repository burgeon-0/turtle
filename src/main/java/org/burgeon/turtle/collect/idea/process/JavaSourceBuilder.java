package org.burgeon.turtle.collect.idea.process;

import com.intellij.lang.jvm.JvmModifier;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import org.apache.commons.lang3.StringUtils;
import org.burgeon.turtle.core.model.source.JavaClass;
import org.burgeon.turtle.core.model.source.JavaComment;
import org.burgeon.turtle.core.model.source.JavaCommentTag;
import org.burgeon.turtle.core.model.source.JavaModifier;
import org.fest.util.Arrays;

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
        javaClass.setModifier(buildModifier());
        javaClass.setName(psiClass.getName());
        javaClass.setClass(!psiClass.isInterface() && !psiClass.isAnnotationType() && !psiClass.isEnum());
        javaClass.setInterface(psiClass.isInterface());
        javaClass.setAnnotation(psiClass.isAnnotationType());
        javaClass.setEnum(psiClass.isEnum());
        javaClass.setSuperClass(buildSupperClass());
        javaClass.setInterfaces(buildInterfaces());
        javaClass.setInnerClasses(buildAllInnerClass());
        return this;
    }

    /**
     * 构建修饰符
     *
     * @return
     */
    private JavaModifier buildModifier() {
        JavaModifier javaModifier = new JavaModifier();
        javaModifier.setPublic(psiClass.hasModifier(JvmModifier.PUBLIC));
        javaModifier.setPrivate(psiClass.hasModifier(JvmModifier.PRIVATE));
        javaModifier.setProtected(psiClass.hasModifier(JvmModifier.PROTECTED));
        javaModifier.setStatic(psiClass.hasModifier(JvmModifier.STATIC));
        javaModifier.setFinal(psiClass.hasModifier(JvmModifier.FINAL));
        javaModifier.setSynchronized(psiClass.hasModifier(JvmModifier.SYNCHRONIZED));
        javaModifier.setVolatile(psiClass.hasModifier(JvmModifier.VOLATILE));
        javaModifier.setTransient(psiClass.hasModifier(JvmModifier.TRANSIENT));
        javaModifier.setNative(psiClass.hasModifier(JvmModifier.NATIVE));
        javaModifier.setAbstract(psiClass.hasModifier(JvmModifier.ABSTRACT));
        javaModifier.setStrict(psiClass.hasModifier(JvmModifier.STRICTFP));
        return javaModifier;
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
        PsiClass[] interfaces = psiClass.getInterfaces();
        if (interfaces == null) {
            return null;
        }

        JavaClass[] javaClasses = new JavaClass[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            PsiClass psiInterface = interfaces[i];
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
    private JavaClass[] buildAllInnerClass() {
        PsiClass[] psiInnerClasses = psiClass.getAllInnerClasses();
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
        if (psiDocComment != null && StringUtils.isNotBlank(psiDocComment.getText())) {
            JavaComment javaComment = new JavaComment();
            javaComment.setText(psiDocComment.getText());

            PsiDocTag[] psiDocTags = psiDocComment.getTags();
            if (!Arrays.isNullOrEmpty(psiDocTags)) {
                JavaCommentTag[] javaCommentTags = new JavaCommentTag[psiDocTags.length];
                javaComment.setTags(javaCommentTags);
                for (int i = 0; i < psiDocTags.length; i++) {
                    PsiDocTag psiDocTag = psiDocTags[i];
                    JavaCommentTag javaCommentTag = new JavaCommentTag();
                    javaCommentTag.setName(psiDocTag.getName());
                    String text = psiDocTag.getText();
                    javaCommentTag.setValue(getCommentTagValue(javaCommentTag.getName(), text));
                    javaCommentTags[i] = javaCommentTag;
                }
            }
            javaClass.setComment(javaComment);
        }
        return this;
    }

    /**
     * 获取注释标签的值
     *
     * @param tagName
     * @param text
     * @return
     */
    private String getCommentTagValue(String tagName, String text) {
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
     * 构建注解
     *
     * @return
     */
    public JavaSourceBuilder buildAnnotations() {
        return this;
    }

    /**
     * 构建方法
     *
     * @return
     */
    public JavaSourceBuilder buildMethods() {
        return this;
    }

    /**
     * 构建属性
     *
     * @return
     */
    public JavaSourceBuilder buildFields() {
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

}
