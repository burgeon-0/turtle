package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.model.source.SourceProject;

import java.io.File;

/**
 * Maven项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class MavenProjectAnalysisStrategy implements AnalysisStrategy {

    /**
     * test project
     */
    private String userDir = "/Users/xiaoconglu/code/java/simple-tomcat";

    private String pomName = "pom.xml";

    @Override
    public String name() {
        return "maven";
    }

    @Override
    public SourceProject analyze() throws AnalysisException {
        log.debug("Analyze by maven strategy.");

        // 判断项目根目录是否存在
        File directory = new File(userDir);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new AnalysisException("Project directory not exists.");
        }

        // 获取pom文件
        File pom = null;
        for (File file : directory.listFiles()) {
            if (file.getName().equals(pomName)) {
                pom = file;
                break;
            }
        }
        if (pom == null) {
            throw new AnalysisException("Pom file not found.");
        }

        // TODO analyze by maven

        return null;
    }

}
