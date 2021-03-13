package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.common.Constants;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.utils.EnvUtils;
import spoon.FluentLauncher;
import spoon.reflect.CtModel;

/**
 * 自定义项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class CustomProjectAnalysisStrategy extends AbstractAnalysisStrategy {

    @Override
    public String name() {
        return "custom";
    }

    @Override
    public SourceProject analyze() throws AnalysisException {
        log.debug("Analyze by custom strategy.");

        String sourcePath = EnvUtils.getStringProperty(Constants.SOURCE_PATH);
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);
        String[] classpath = EnvUtils.getStringArrayProperty(Constants.CLASSPATH,
                Constants.SEPARATOR_SEMICOLON, new String[]{"."});

        // 检查项目根目录是否存在
        checkDirectoryExists(sourcePath);

        // analyze by spoon
        FluentLauncher launcher = new FluentLauncher()
                .inputResource(sourcePath)
                .sourceClassPath(classpath)
                .outputDirectory(targetPath);
        CtModel model = launcher.buildModel();
        SourceProject sourceProject = new SourceProject();
        sourceProject.setModel(model);

        return sourceProject;
    }

}
