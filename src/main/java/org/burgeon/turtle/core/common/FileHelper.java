package org.burgeon.turtle.core.common;

import org.burgeon.turtle.core.model.api.Parameter;
import org.burgeon.turtle.core.model.api.ParameterType;

import java.util.List;

/**
 * 文件工具
 *
 * @author Sam Lu
 * @createdOn 2021/4/12
 */
public class FileHelper {

    /**
     * 参数中是否存在文件参数
     *
     * @param parameters
     * @return
     */
    public static boolean hasFile(List<Parameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return false;
        }
        for (Parameter parameter : parameters) {
            if (hasFile(parameter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 参数中是否存在文件参数
     *
     * @param parameter
     * @return
     */
    public static boolean hasFile(Parameter parameter) {
        if (parameter != null) {
            if (parameter.getType() == ParameterType.FILE) {
                return true;
            }
            if (parameter.getType() == ParameterType.ARRAY
                    || parameter.getType() == ParameterType.OBJECT) {
                if (hasFile(parameter.getChildParameters())) {
                    return true;
                }
            }
        }
        return false;
    }

}
