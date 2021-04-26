package org.burgeon.turtle.export.blueprint;

import org.burgeon.turtle.core.model.api.Parameter;
import org.burgeon.turtle.core.model.api.ParameterType;
import org.burgeon.turtle.core.utils.StringUtils;

import java.util.List;

import static org.burgeon.turtle.export.blueprint.Constants.*;

/**
 * Http Path和Uti参数构建器
 *
 * @author Sam Lu
 * @createdOn 2021/4/9
 */
public class ParametersBuilder {

    private StringBuilder builder = new StringBuilder();

    /**
     * 构建Http Path和Uri参数
     *
     * @param parameters
     */
    public ParametersBuilder buildParameters(List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            builder.append(LINE_BREAK);
            builder.append(PARAMETERS).append(LINE_BREAK);
            for (Parameter parameter : parameters) {
                if (parameter.getParentParameter() == null
                        && parameter.getType() == ParameterType.OBJECT) {
                    List<Parameter> childParameters = parameter.getChildParameters();
                    for (Parameter childParameter : childParameters) {
                        buildParameter(childParameter, 1, null);
                    }
                } else {
                    buildParameter(parameter, 1, null);
                }
            }
        }
        return this;
    }

    /**
     * 构建Http参数
     *
     * @param parameter
     * @param indent
     * @param suffix
     */
    private void buildParameter(Parameter parameter, int indent, String suffix) {
        boolean isObject = parameter.getType() == ParameterType.OBJECT;
        boolean isObjectArray = parameter.getType() == ParameterType.ARRAY
                && parameter.getChildParameters().get(0).getType() == ParameterType.OBJECT;
        boolean object = isObject || isObjectArray;
        boolean hasChildParameters = parameter.getChildParameters() != null
                && !parameter.getChildParameters().isEmpty();
        if (object && hasChildParameters) {
            if (parameter.getParentParameter() != null
                    && parameter.getParentParameter().getType() != ParameterType.ARRAY) {
                if (suffix == null) {
                    suffix = parameter.getName();
                } else {
                    suffix = String.format("%s.%s", suffix, parameter.getName());
                }
            }
            List<Parameter> childParameters = parameter.getChildParameters();
            for (int i = 0; i < childParameters.size(); i++) {
                Parameter childParameter = childParameters.get(i);
                buildParameter(childParameter, indent, suffix);
            }
        } else {
            realBuildParameter(parameter, indent, suffix);
        }
    }

    /**
     * 构建Http参数
     *
     * @param parameter
     * @param indent
     * @param suffix
     */
    private void realBuildParameter(Parameter parameter, int indent, String suffix) {
        for (int i = 0; i < indent; i++) {
            builder.append(TAB);
        }
        String parameterName = parameter.getName();
        if (suffix != null) {
            parameterName = String.format("%s.%s", suffix, parameterName);
        }
        if (parameter.getParentParameter() != null
                && parameter.getParentParameter().getType() == ParameterType.ARRAY) {
            parameterName = suffix;
        }
        builder.append(PLUS).append(SPACE).append(parameterName);
        builder.append(SPACE).append(LEFT_PARENTHESES);
        if (parameter.isRequired()) {
            builder.append(REQUIRED);
        } else {
            builder.append(OPTIONAL);
        }
        builder.append(COMMA).append(SPACE);
        if (parameter.getParentParameter() != null
                && parameter.getParentParameter().getType() == ParameterType.ARRAY) {
            builder.append(ParameterType.ARRAY.toString().toLowerCase());
        } else {
            builder.append(parameter.getType().toString().toLowerCase());
        }
        builder.append(RIGHT_PARENTHESES);
        if (StringUtils.notBlank(parameter.getDescription())) {
            String description = parameter.getDescription();
            builder.append(SPACE).append(MINUS).append(SPACE).append(description);
        }
        builder.append(LINE_BREAK);
    }

    /**
     * 完成构建
     *
     * @return
     */
    public String build() {
        return builder.toString();
    }

    /**
     * 清空构建历史
     *
     * @return
     */
    public void reset() {
        builder.delete(0, builder.length());
    }

}
