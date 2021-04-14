package org.burgeon.turtle.core.utils;

import com.sun.javafx.PlatformUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 命令行工具
 *
 * @author luxiaocong
 * @createdOn 2021/4/14
 */
@Slf4j
public class CommandLineUtils {

    /**
     * 运行命令行的命令
     *
     * @param commands
     * @throws IOException
     * @throws InterruptedException
     */
    public static void executeCommands(String[] commands) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(getBaseCommand());
        Process process = builder.start();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        for (int i = 0; i < commands.length; i++) {
            executeCommand(writer, commands[i]);
        }
        executeCommand(writer, "exit");
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (reader.ready()) {
            String line = reader.readLine();
            log.debug(line);
        }
        reader.close();
    }

    /**
     * 获取基础命令
     *
     * @return
     */
    private static String getBaseCommand() {
        if (PlatformUtil.isWindows()) {
            return "C:/Windows/System32/cmd.exe";
        } else {
            return "/bin/bash";
        }
    }

    /**
     * 运行命令行的命令
     *
     * @param writer
     * @param command
     * @throws IOException
     */
    private static void executeCommand(BufferedWriter writer, String command) throws IOException {
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

}
