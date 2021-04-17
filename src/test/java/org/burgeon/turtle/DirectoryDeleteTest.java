package org.burgeon.turtle;

import java.io.File;
import java.nio.file.Files;

/**
 * 测试删除目录
 *
 * @author luxiaocong
 * @createdOn 2021/4/17
 */
public class DirectoryDeleteTest {

    public static void main(String[] args) {
        File directory = new File("/Users/xiaoconglu/code/java/room/out");
        if (directory.exists() && directory.isDirectory()) {
            deleteDir(directory);
        }
    }

    private static void deleteDir(File file) {
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
