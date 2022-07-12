package org.bg181.turtle.core.process.analysis;

import lombok.extern.slf4j.Slf4j;
import org.bg181.turtle.core.common.Constants;
import org.bg181.turtle.core.model.source.SourceProject;
import org.bg181.turtle.core.utils.EnvUtils;
import spoon.IncrementalLauncher;
import spoon.reflect.CtModel;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义项目分析策略
 * <ol>
 * <li>有时候项目的依赖管理，用的既不是maven，也不是gradle，jar包的依赖可能是以自定义的方式指定的，这种类型的项目
 * 就需要用自定义的项目分析策略进行分析，指定项目的源文件目录、依赖jar包的目录和classpath</li>
 * </ol>
 *
 * @author Sam Lu
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
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH) + Constants.SPOON_DIR;
        String[] classpath = EnvUtils.getStringArrayProperty(Constants.CLASSPATH,
                Constants.SEPARATOR_SEMICOLON, new String[]{"."});

        // 检查项目根目录是否存在
        checkDirectoryExists(sourcePath);

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

}
