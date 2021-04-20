package org.burgeon.turtle.core.command;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令执行器工厂
 *
 * @author luxiaocong
 * @createdOn 2021/4/20
 */
public class CommandExecutorFactory {

    private static CommandExecutor commandExecutor = new DefaultCommandExecutor();

    private static Map<Integer, CommandExecutor> commandExecutorMap = new HashMap<>();

    /**
     * 获取命令执行器
     *
     * @return
     */
    public static CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    /**
     * 获取命令执行器
     *
     * @param code
     * @return
     */
    public static CommandExecutor getCommandExecutor(Integer code) {
        CommandExecutor executor = commandExecutorMap.get(code);
        if (executor == null) {
            executor = commandExecutor;
        }
        return executor;
    }

    /**
     * 注册命令执行器
     *
     * @param code
     * @param commandExecutor
     */
    public static void registerCommandExecutor(Integer code, CommandExecutor commandExecutor) {
        commandExecutorMap.put(code, commandExecutor);
    }

}
