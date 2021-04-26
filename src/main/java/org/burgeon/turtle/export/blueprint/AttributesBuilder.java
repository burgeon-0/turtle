package org.burgeon.turtle.export.blueprint;

import org.burgeon.turtle.core.model.api.Parameter;
import org.burgeon.turtle.core.model.api.ParameterType;
import org.burgeon.turtle.core.utils.StringUtils;

import java.util.List;

import static org.burgeon.turtle.export.blueprint.Constants.*;

/**
 * Http请求和返回参数构建器
 *
 * @author Sam Lu
 * @createdOn 2021/4/9
 */
public class AttributesBuilder {

    private StringBuilder builder = new StringBuilder();

    /**
     * 构建Http请求和返回参数
     *
     * @param parameters
     */
    public AttributesBuilder buildAttributes(List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            if (parameters.get(0).getType() == ParameterType.ARRAY) {
                builder.append(TAB).append(ATTRIBUTES);
                builder.append(SPACE).append(LEFT_PARENTHESES).append(ARRAY);
                if (parameters.get(0).getChildParameters().get(0).getType() == ParameterType.OBJECT) {
                    builder.append(COMMA).append(SPACE).append(FIXED_TYPE);
                }
                builder.append(RIGHT_PARENTHESES).append(LINE_BREAK);
            } else {
                builder.append(TAB).append(ATTRIBUTES).append(LINE_BREAK);
            }
            for (Parameter parameter : parameters) {
                if (parameter.getParentParameter() == null
                        && parameter.getType() == ParameterType.OBJECT) {
                    List<Parameter> childParameters = parameter.getChildParameters();
                    for (Parameter childParameter : childParameters) {
                        buildParameter(childParameter, 2);
                    }
                } else {
                    buildParameter(parameter, 2);
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
     */
    private void buildParameter(Parameter parameter, int indent) {
        if (parameter.getParentParameter() != null
                && parameter.getParentParameter().getType() == ParameterType.ARRAY) {
            if (parameter.getType() == ParameterType.OBJECT
                    && parameter.getChildParameters() != null
                    && !parameter.getChildParameters().isEmpty()) {
                boolean isFirstParameter = parameter.getParentParameter().getParentParameter() == null;
                if (!isFirstParameter) {
                    indent++;
                }
                for (int i = 0; i < indent; i++) {
                    builder.append(TAB);
                }
                builder.append(PLUS).append(SPACE).append(LEFT_PARENTHESES).append(OBJECT);
                builder.append(RIGHT_PARENTHESES).append(LINE_BREAK);
            }
        } else if (!(parameter.getParentParameter() == null && parameter.getType() == ParameterType.ARRAY)) {
            for (int i = 0; i < indent; i++) {
                builder.append(TAB);
            }
            builder.append(PLUS).append(SPACE).append(parameter.getName());
            builder.append(SPACE).append(LEFT_PARENTHESES);
            if (parameter.isRequired()) {
                builder.append(REQUIRED);
            } else {
                builder.append(OPTIONAL);
            }
            builder.append(COMMA).append(SPACE);
            builder.append(parameter.getType().toString().toLowerCase());
            if (parameter.getType() == ParameterType.ARRAY
                    && parameter.getChildParameters().get(0).getType() == ParameterType.OBJECT
                    && parameter.getChildParameters().get(0).getChildParameters() != null
                    && !parameter.getChildParameters().get(0).getChildParameters().isEmpty()) {
                builder.append(COMMA).append(SPACE).append(FIXED_TYPE);
            }
            builder.append(RIGHT_PARENTHESES);
            if (StringUtils.notBlank(parameter.getDescription())) {
                String description = parameter.getDescription();
                builder.append(SPACE).append(MINUS).append(SPACE).append(description);
            }
            builder.append(LINE_BREAK);

            // 处理没有Properties的Object
            boolean noChildParameters = parameter.getChildParameters() == null
                    || parameter.getChildParameters().isEmpty();
            if (parameter.getType() == ParameterType.OBJECT && noChildParameters) {
                for (int i = 0; i < indent; i++) {
                    builder.append(TAB);
                }
                builder.append(TAB).append(OBJECT_DEFAULT_PROPERTIES).append(LINE_BREAK);
            }
        }

        if (parameter.getType() == ParameterType.ARRAY
                || parameter.getType() == ParameterType.OBJECT) {
            buildChildParameters(parameter, indent);
        }
    }

    /**
     * 构建Http子参数
     *
     * @param parameter
     * @param indent
     */
    private void buildChildParameters(Parameter parameter, int indent) {
        List<Parameter> childParameters = parameter.getChildParameters();
        if (childParameters != null && !childParameters.isEmpty()) {
            if (parameter.getType() != ParameterType.ARRAY) {
                indent++;
            }
            for (Parameter childParameter : childParameters) {
                buildParameter(childParameter, indent);
            }
        }
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
