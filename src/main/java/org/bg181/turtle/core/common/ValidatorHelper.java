package org.bg181.turtle.core.common;

import lombok.extern.slf4j.Slf4j;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.path.CtRole;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证器工具
 *
 * @author Sam Lu
 * @createdOn 2021/4/13
 */
@Slf4j
public class ValidatorHelper {

    private static final Properties VALIDATION_MESSAGES;
    private static final String REGEX = "\\{[^\\}]*\\}";

    static {
        InputStream inputStream = VersionHelper.class.getClassLoader()
                .getResourceAsStream("ValidationMessages.properties");
        VALIDATION_MESSAGES = new Properties();
        try {
            VALIDATION_MESSAGES.load(inputStream);
        } catch (Exception e) {
            log.error("Could not load ValidationMessages.properties.", e);
        }
    }

    /**
     * 获取验证信息
     *
     * @return
     */
    public static String getMessage(CtAnnotation<?> ctAnnotation) {
        String result = ctAnnotation.getValue("message").partiallyEvaluate().getValueByRole(CtRole.VALUE);
        if (result.startsWith(Constants.LEFT_BRACE) && result.endsWith(Constants.RIGHT_BRACE)) {
            result = result.substring(1, result.length() - 1);
            String message = VALIDATION_MESSAGES.getProperty(result);
            if (message != null) {
                result = message;

                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    ScriptEngineManager engineManager = new ScriptEngineManager();
                    ScriptEngine engine = engineManager.getEngineByName("js");
                    do {
                        result = parseMessage(engine, ctAnnotation, matcher, result);
                    } while (matcher.find());
                }
            }
        }
        return result;
    }

    /**
     * 获取key
     *
     * @param script
     * @return
     */
    private static String getKey(String script) {
        String key = script;
        if (key.contains(Constants.SEPARATOR_SPACE)) {
            int index = key.indexOf(Constants.SEPARATOR_SPACE);
            key = key.substring(0, index);
        }
        return key;
    }

    /**
     * 解析validation message
     *
     * @param engine
     * @param ctAnnotation
     * @param matcher
     * @param result
     * @return
     */
    private static String parseMessage(ScriptEngine engine, CtAnnotation<?> ctAnnotation, Matcher matcher,
                                       String result) {
        try {
            String text = matcher.group();
            String script = text.substring(1, text.length() - 1);
            String key = getKey(script);
            Object value = ctAnnotation.getValue(key).partiallyEvaluate().getValueByRole(CtRole.VALUE).toString();
            String init = String.format("var %s = '%s'", key, value);
            engine.eval(init);
            value = engine.eval(script).toString();
            result = result.replace(text, value.toString());
        } catch (Exception e) {
            log.error("Parse validation message fail.", e);
        }
        return result;
    }

}
