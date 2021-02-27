package org.burgeon.turtle.collect.idea.process;

import com.intellij.psi.PsiClass;
import org.burgeon.turtle.core.model.source.JavaClass;

/**
 * Java源文件构建器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
public class JavaSourceBuilder {

    private PsiClass psiClass;

    private JavaClass javaClass;

    public JavaSourceBuilder(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    /**
     * 构建class文件
     *
     * @return
     */
    public JavaSourceBuilder buildClass() {
        javaClass = new JavaClass();

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
     * 构建方法
     *
     * @return
     */
    public JavaSourceBuilder buildMethods() {
        return this;
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
     * 构建注释
     *
     * @return
     */
    public JavaSourceBuilder buildComment() {
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
