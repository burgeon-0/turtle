package org.bg181.turtle.core.command;

/**
 * 命令执行器
 *
 * @author Sam Lu
 * @createdOn 2021/4/20
 */
public interface CommandExecutor {

    /**
     * 执行命令
     *
     * @param command
     * @throws ExecuteException
     */
    void execute(String command) throws ExecuteException;

}
