package org.bg181.turtle.core.command;

import com.sun.javafx.PlatformUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 默认命令执行器
 *
 * @author Sam Lu
 * @createdOn 2021/4/20
 */
@Slf4j
public class DefaultCommandExecutor implements CommandExecutor {

    @Override
    public void execute(String command) throws ExecuteException {
        try {
            executeCommands(new String[]{command});
        } catch (Exception e) {
            throw new ExecuteException(e);
        }
    }

    /**
     * 批量执行命令
     *
     * @param commands
     * @throws IOException
     * @throws InterruptedException
     */
    public void executeCommands(String[] commands) throws IOException, InterruptedException {
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
    private String getBaseCommand() {
        if (PlatformUtil.isWindows()) {
            return "C:/Windows/System32/cmd.exe";
        } else {
            return "/bin/bash";
        }
    }

    /**
     * 执行命令
     *
     * @param writer
     * @param command
     * @throws IOException
     */
    private void executeCommand(BufferedWriter writer, String command) throws IOException {
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

}
