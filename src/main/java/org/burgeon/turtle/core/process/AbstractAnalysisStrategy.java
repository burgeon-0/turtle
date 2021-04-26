package org.burgeon.turtle.core.process;

import java.io.File;
import java.nio.file.Files;

/**
 * 分析策略抽象类
 *
 * @author Sam Lu
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

    /**
     * 清除目标文件夹
     *
     * @param targetPath
     */
    protected void cleanTargetDirectory(String targetPath) {
        File directory = new File(targetPath);
        if (directory.exists() && directory.isDirectory()) {
            deleteDir(directory);
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

}
