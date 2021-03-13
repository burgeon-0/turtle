package org.burgeon.turtle.core.process;

import java.io.File;

/**
 * 分析策略抽象类
 *
 * @author luxiaocong
 * @createdOn 2021/3/13
 */
public abstract class AbstractAnalysisStrategy implements AnalysisStrategy {

    /**
     * 检查项目根目录是否存在
     *
     * @param sourcePath
     */
    protected void checkDirectoryExists(String sourcePath) {
        File directory = new File(sourcePath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException("Project directory not exists: " + sourcePath + ".");
        }
    }

}
