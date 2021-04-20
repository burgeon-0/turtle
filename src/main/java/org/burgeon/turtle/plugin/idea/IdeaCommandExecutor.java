package org.burgeon.turtle.plugin.idea;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import org.burgeon.turtle.core.command.CommandExecutor;
import org.burgeon.turtle.core.command.ExecuteException;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.utils.EnvUtils;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * IDEA命令执行器
 *
 * @author luxiaocong
 * @createdOn 2021/4/20
 */
public class IdeaCommandExecutor implements CommandExecutor {

    @Override
    public void execute(String[] commands) throws ExecuteException {
        try {
            String sourcePath = EnvUtils.getStringProperty(Constants.SOURCE_PATH);
            GeneralCommandLine generalCommandLine = new GeneralCommandLine(Arrays.asList(commands));
            generalCommandLine.setCharset(Charset.forName("UTF-8"));
            generalCommandLine.setWorkDirectory(sourcePath);

            ProcessHandler processHandler = new OSProcessHandler(generalCommandLine);
            processHandler.startNotify();
        } catch (Exception e) {
            throw new ExecuteException(e);
        }
    }

}
