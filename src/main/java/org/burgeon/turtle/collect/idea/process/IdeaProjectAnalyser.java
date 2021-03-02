package org.burgeon.turtle.collect.idea.process;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.collect.Constants;
import org.burgeon.turtle.core.model.source.JavaClass;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.core.process.Analyser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * IDEA项目分析器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Slf4j
public class IdeaProjectAnalyser implements Analyser {

    private Project project;

    private VirtualFile virtualFile;

    public IdeaProjectAnalyser(Project project, VirtualFile virtualFile) {
        this.project = project;
        this.virtualFile = virtualFile;
    }

    @Override
    public SourceProject analyse() {
        long startTime = System.currentTimeMillis();

        SourceProject sourceProject = new SourceProject();
        sourceProject.setName(project.getName());
        List<JavaClass> javaClasses = new ArrayList<>();
        sourceProject.setJavaClasses(javaClasses);

        // TODO 提高分析效率，思路如下：
        // 1. 使用多线程，充分利用多核CPU的能力
        // 2. 预分析，项目启动时预先分析，项目文件变更时重新分析变更文件
        // 3. 去掉不必要的分析，如：对superClass、interfaces、innerClasses等的分析【经测试，发现作用不大；发现现象：第一次加载慢，后面越来越快】
        // 4. 改变分析策略，只分析存在目标注解的类，如：只分析存在@RestController等注解的类
        List<PsiJavaFile> psiJavaFiles = getPsiJavaFiles();
        for (PsiJavaFile psiJavaFile : psiJavaFiles) {
            log.info("PsiJavaFile: {}.{}", psiJavaFile.getPackageName(), psiJavaFile.getName());

            for (PsiClass psiClass : psiJavaFile.getClasses()) {
                JavaSourceBuilder javaSourceBuilder = new JavaSourceBuilder(psiJavaFile, psiClass);
                JavaClass javaClass = javaSourceBuilder.buildClass()
                        .buildComment()
                        .buildAnnotations()
                        .buildMethods()
                        .buildFields()
                        .build();
                javaClasses.add(javaClass);
            }
        }

        log.info("Analyse finished, use {} ms", System.currentTimeMillis() - startTime);
        return sourceProject;
    }

    /**
     * 查找项目中的Java源文件
     *
     * @return
     */
    private List<PsiJavaFile> getPsiJavaFiles() {
        PsiManager psiManager = PsiManager.getInstance(project);
        List<PsiJavaFile> psiJavaFiles = new ArrayList<>();
        VfsUtilCore.visitChildrenRecursively(virtualFile, new VirtualFileVisitor<VirtualFile>() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                if (!file.isDirectory() && file.getName().endsWith(Constants.JAVA_FILE_SUFFIX)) {
                    PsiFile psiFile = psiManager.findFile(file);
                    psiJavaFiles.add((PsiJavaFile) psiFile);
                }
                return true;
            }
        });
        return psiJavaFiles;
    }

}
