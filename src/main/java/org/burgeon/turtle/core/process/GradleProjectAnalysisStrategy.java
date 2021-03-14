package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.common.Constants;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.utils.EnvUtils;
import spoon.FluentLauncher;
import spoon.reflect.CtModel;

import java.io.File;

/**
 * Gradle项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class GradleProjectAnalysisStrategy extends AbstractAnalysisStrategy {

    private String srcPath = "src";
    private String gradleConfigName = "build.gradle";

    @Override
    public String name() {
        return "gradle";
    }

    @Override
    public SourceProject analyze() throws AnalysisException {
        log.debug("Analyze by gradle strategy.");

        String sourcePath = EnvUtils.getStringProperty(Constants.SOURCE_PATH);
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);

        // 检查项目根目录是否存在
        checkDirectoryExists(sourcePath);

        // 检查项目gradle配置文件是否存在
        checkGradleConfigExists(sourcePath);

        // analyze by spoon
        FluentLauncher launcher = new FluentLauncher()
                .inputResource(sourcePath)
                .outputDirectory(targetPath);
        CtModel model = launcher.buildModel();
        SourceProject sourceProject = new SourceProject();
        sourceProject.setModel(model);

        return sourceProject;
    }

    /**
     * 检查项目gradle配置文件是否存在
     *
     * @param sourcePath
     * @throws AnalysisException
     */
    private void checkGradleConfigExists(String sourcePath) throws AnalysisException {
        File directory = new File(sourcePath);
        File gradleConfig = findGradleConfig(directory);
        if (gradleConfig == null) {
            if (directory.getName().equals(srcPath)) {
                sourcePath = sourcePath.substring(0, sourcePath.length() - srcPath.length());
                directory = new File(sourcePath);
                gradleConfig = findGradleConfig(directory);
            }
        }
        if (gradleConfig == null) {
            throw new AnalysisException("Gradle config file not found.");
        }
    }

    /**
     * 查找项目gradle配置文件
     *
     * @param directory
     * @return
     */
    private File findGradleConfig(File directory) {
        for (File file : directory.listFiles()) {
            if (file.getName().equals(gradleConfigName)) {
                return file;
            }
        }
        return null;
    }

}
