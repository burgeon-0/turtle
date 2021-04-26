package org.burgeon.turtle;

import com.sun.javafx.PlatformUtil;

import java.io.*;

/**
 * 测试运行命令行的命令
 *
 * @author Sam Lu
 * @createdOn 2021/4/14
 */
public class CommandLineTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        executeCommands(new String[]{"cd out", "pwd",
                "aglio -i api-blueprint.apib -o api-blueprint.html", "ls -al"});
    }

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
            System.out.println(line);
        }
        reader.close();
    }

    private static String getBaseCommand() {
        if (PlatformUtil.isWindows()) {
            return "C:/Windows/System32/cmd.exe";
        } else {
            return "/bin/bash";
        }
    }

    private static void executeCommand(BufferedWriter writer, String command) throws IOException {
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

}
