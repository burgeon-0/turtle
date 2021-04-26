package org.burgeon.turtle;

import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 测试使用ScriptEngine解析表达式
 *
 * @author Sam Lu
 * @createdOn 2021/4/13
 */
@Slf4j
public class ScriptEngineTest {

    public static void main(String[] arg) throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("js");
        engine.eval("var inclusive = true");
        System.out.println(engine.eval("inclusive == true ? 'or equal to ' : ''"));
        System.out.println(engine.eval("inclusive"));
    }

}
