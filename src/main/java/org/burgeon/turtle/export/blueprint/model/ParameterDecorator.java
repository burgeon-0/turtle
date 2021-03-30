package org.burgeon.turtle.export.blueprint.model;

import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.model.api.HttpParameterPosition;
import org.burgeon.turtle.core.model.api.Parameter;
import org.burgeon.turtle.core.model.api.ParameterType;
import org.burgeon.turtle.export.blueprint.FilterHelper;

import java.util.List;

/**
 * 参数【装饰器】
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class ParameterDecorator extends Parameter {

    private Parameter parameter;
    private Parameter parentParameter;
    private List<Parameter> childParameters;

    private String name;

    public ParameterDecorator(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getId() {
        return parameter.getId();
    }

    @Override
    public String getOriginType() {
        return parameter.getOriginType();
    }

    @Override
    public ParameterType getType() {
        return parameter.getType();
    }

    @Override
    public HttpParameterPosition getPosition() {
        return parameter.getPosition();
    }

    /**
     * 在API Blueprint的规范中，参数名称不允许有"-"，如有，在此替换为"_"
     *
     * @return
     */
    @Override
    public String getName() {
        if (name == null) {
            name = FilterHelper.filterParameterName(parameter.getName());
            if (name.contains(Constants.MINUS)) {
                name = name.replaceAll(Constants.MINUS, Constants.UNDERLINE);
            }
        }
        return name;
    }

    @Override
    public boolean isRequired() {
        return parameter.isRequired();
    }

    @Override
    public String getDescription() {
        return FilterHelper.filterParameterDescription(parameter.getDescription());
    }

    @Override
    public String getExampleValue() {
        return parameter.getExampleValue();
    }

    @Override
    public void setParentParameter(Parameter parentParameter) {
        this.parentParameter = parentParameter;
    }

    @Override
    public Parameter getParentParameter() {
        return parentParameter;
    }

    @Override
    public void setChildParameters(List<Parameter> childParameters) {
        this.childParameters = childParameters;
    }

    @Override
    public List<Parameter> getChildParameters() {
        return childParameters;
    }

}
