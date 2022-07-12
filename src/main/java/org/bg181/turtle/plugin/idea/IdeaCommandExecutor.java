package org.bg181.turtle.plugin.idea;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import org.bg181.turtle.core.command.CommandExecutor;
import org.bg181.turtle.core.command.ExecuteException;
import org.bg181.turtle.core.common.Constants;
import org.bg181.turtle.core.utils.EnvUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * IDEA命令执行器
 *
 * @author Sam Lu
 * @createdOn 2021/4/20
 */
public class IdeaCommandExecutor implements CommandExecutor {

    @Override
    public void execute(String command) throws ExecuteException {
        try {
            List<String> commandList = new ArrayList<>();
            String[] arr = command.split(" ");
            commandList.addAll(Arrays.asList(arr));

            String sourcePath = EnvUtils.getStringProperty(Constants.SOURCE_PATH);
            GeneralCommandLine generalCommandLine = new GeneralCommandLine(commandList);
            generalCommandLine.setCharset(Charset.forName("UTF-8"));
            generalCommandLine.setWorkDirectory(sourcePath);

            ProcessHandler processHandler = new OSProcessHandler(generalCommandLine);
            processHandler.startNotify();
        } catch (Exception e) {
            throw new ExecuteException(e);
        }
    }

}
