package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.common.Constants;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.utils.EnvUtils;
import spoon.FluentLauncher;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;

import java.io.File;

/**
 * Maven项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class MavenProjectAnalysisStrategy implements AnalysisStrategy {

    private String srcPath = "src";
    private String pomName = "pom.xml";

    @Override
    public String name() {
        return "maven";
    }

    @Override
    public SourceProject analyze() throws AnalysisException {
        log.debug("Analyze by maven strategy.");

        String sourcePath = EnvUtils.getStringProperty(Constants.SOURCE_PATH);
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);

        // 检查项目根目录是否存在
        checkDirectoryExists(sourcePath);

        // 检查项目pom文件是否存在
        sourcePath = checkPomExists(sourcePath);

        // analyze by spoon
        MavenLauncher launcher = new MavenLauncher(sourcePath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        launcher.setSourceOutputDirectory(targetPath);
        launcher.setBinaryOutputDirectory(targetPath);
        CtModel model = new FluentLauncher(launcher).buildModel();
        SourceProject sourceProject = new SourceProject();
        sourceProject.setModel(model);

        return sourceProject;
    }

    /**
     * 检查项目根目录是否存在
     *
     * @param sourcePath
     * @throws AnalysisException
     */
    private void checkDirectoryExists(String sourcePath) throws AnalysisException {
        File directory = new File(sourcePath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new AnalysisException("Project directory not exists: " + sourcePath + ".");
        }
    }

    /**
     * 检查项目pom文件是否存在
     *
     * @param sourcePath
     * @throws AnalysisException
     */
    private String checkPomExists(String sourcePath) throws AnalysisException {
        File directory = new File(sourcePath);
        File pom = findPom(directory);
        if (pom == null) {
            if (directory.getName().equals(srcPath)) {
                sourcePath = sourcePath.substring(0, sourcePath.length() - srcPath.length());
                directory = new File(sourcePath);
                pom = findPom(directory);
            }
        }
        if (pom == null) {
            throw new AnalysisException("Pom file not found.");
        }
        return sourcePath;
    }

    /**
     * 查找项目pom文件
     *
     * @param directory
     * @return
     */
    private File findPom(File directory) {
        for (File file : directory.listFiles()) {
            if (file.getName().equals(pomName)) {
                return file;
            }
        }
        return null;
    }

}
