package org.burgeon.turtle.core.process.analysis;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.core.utils.EnvUtils;
import spoon.IncrementalLauncher;
import spoon.reflect.CtModel;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Gradle项目分析策略
 *
 * @author Sam Lu
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
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH) + Constants.SPOON_DIR;
        String[] classpath = EnvUtils.getStringArrayProperty(Constants.CLASSPATH,
                Constants.SEPARATOR_SEMICOLON, new String[]{"."});

        // 检查项目根目录是否存在
        checkDirectoryExists(sourcePath);

        // 检查项目gradle配置文件是否存在
        checkGradleConfigExists(sourcePath);

        // 清除目标文件夹
        cleanTargetDirectory(targetPath);

        // analyze by spoon
        Set<File> inputResources = new HashSet<>();
        inputResources.add(new File(sourcePath));
        Set<String> sourceClasspath = new HashSet<>();
        sourceClasspath.addAll(Arrays.asList(classpath));
        File cacheDirectory = new File(targetPath);
        IncrementalLauncher launcher = new IncrementalLauncher(inputResources, sourceClasspath,
                cacheDirectory);
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
            if (srcPath.equals(directory.getName())) {
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
            if (gradleConfigName.equals(file.getName())) {
                return file;
            }
        }
        return null;
    }

}
