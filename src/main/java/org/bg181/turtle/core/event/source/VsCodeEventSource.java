package org.bg181.turtle.core.event.source;

/**
 * 事件来源：Visual Studio Code
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public class VsCodeEventSource extends EventSource {

    @Override
    public int getCode() {
        return 103;
    }

    @Override
    public String getName() {
        return "vscode";
    }

}
