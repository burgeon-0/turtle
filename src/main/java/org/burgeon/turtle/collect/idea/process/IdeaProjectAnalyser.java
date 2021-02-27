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
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.core.model.source.JavaClass;
import org.burgeon.turtle.core.process.Analyser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
        SourceProject sourceProject = new SourceProject();
        sourceProject.setName(project.getName());
        List<JavaClass> javaClasses = new ArrayList<>();
        sourceProject.setJavaClasses(javaClasses);

        List<PsiClass> psiClasses = getPsiClasses();
        for (PsiClass psiClass : psiClasses) {
            log.info("PsiClass's name: {}", psiClass.getName());

            JavaSourceBuilder javaSourceBuilder = new JavaSourceBuilder(psiClass);
            JavaClass javaClass = javaSourceBuilder.buildClass()
                    .buildFields()
                    .buildMethods()
                    .buildAnnotations()
                    .buildComment()
                    .build();
            javaClasses.add(javaClass);
        }
        return sourceProject;
    }

    /**
     * 查找项目中的Java源文件
     *
     * @return
     */
    private List<PsiClass> getPsiClasses() {
        PsiManager psiManager = PsiManager.getInstance(project);
        List<PsiClass> psiClasses = new ArrayList<>();
        VfsUtilCore.visitChildrenRecursively(virtualFile, new VirtualFileVisitor<VirtualFile>() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                if (!file.isDirectory() && file.getName().endsWith(Constants.JAVA_FILE_SUFFIX)) {
                    PsiFile psiFile = psiManager.findFile(file);
                    psiClasses.addAll(Arrays.asList(((PsiJavaFile) psiFile).getClasses()));
                }
                return true;
            }
        });
        return psiClasses;
    }

}
